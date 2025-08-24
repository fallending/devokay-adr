package com.devokay.example.modules.httpsample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class Downloader (
  private val client: OkHttpClient = OkHttpClient.Builder()
    // 某些测速/对象存储站点跟 HTTP/2 兼容性不佳，强制 HTTP/1.1 更稳
    .protocols(listOf(Protocol.HTTP_1_1))
    .retryOnConnectionFailure(true)
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(0, TimeUnit.SECONDS)       // 大文件下载不设读超时
    .build(),
  private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

) {
  /** 进度数据 */
  data class Progress(
    val downloadedBytes: Long,
    val totalBytes: Long,
    val percent: Int,            // 0..100（未知总长时为 0）
    val instantKbps: Int,        // 过去 1s 的瞬时速度
    val avgKbps: Int,            // 平均速度
    val etaSeconds: Long         // 预计剩余秒数（未知总长时为 -1）
  )

  interface Listener {
    fun onStart(totalBytes: Long, resumeFromBytes: Long)
    fun onProgress(progress: Progress)
    fun onPaused(downloadedBytes: Long)
    fun onCompleted(file: File)
    fun onError(e: Exception)
    fun onCanceled()
  }

  // --- 下载状态 ---
  @Volatile private var isPaused = false
  @Volatile private var isCanceled = false
  @Volatile private var runningJob: Job? = null
  @Volatile private var currentCall: Call? = null

  // --- 断点数据（用于 resume） ---
  private var currentUrl: String? = null
  private var finalFile: File? = null
  private var partFile: File? = null
  private var totalBytes: Long = -1L
  private var downloadedBytes: Long = 0L
  private var startNano: Long = 0L

  // --- 限速（字节/秒），token bucket 更平滑 ---
  private var speedLimitBytesPerSec: Long? = null
  private var tokens: Long = 0L
  private var lastRefillMs: Long = 0L

  /** 开始或重新开始下载（如果存在 .part，将自动从断点续传） */
  fun start(
    url: String,
    destFile: File,
    limitKBps: Int?,             // null/<=0 表示不限速
    listener: Listener
  ) {
    cancel() // 保守：启动前取消可能的旧任务

    currentUrl = url
    finalFile = destFile
    partFile = File(destFile.absolutePath + ".part")
    isPaused = false
    isCanceled = false
    speedLimitBytesPerSec = limitKBps?.takeIf { it > 0 }?.toLong()?.times(1024)

    runningJob = externalScope.launch {
      try {
        // 续传点：已有 part 文件时，从现有长度继续
        downloadedBytes = if (partFile!!.exists()) partFile!!.length() else 0L

        // 发起 HEAD 或 GET 以探测长度（优先 HEAD，失败再用 GET）
        totalBytes = probeContentLength(url)

        withContext(Dispatchers.Main) {
          listener.onStart(totalBytes, downloadedBytes)
        }

        startNano = System.nanoTime()
        resetLimiter()

        // 正式下载
        performDownload(url, listener)
      } catch (ce: CancellationException) {
        withContext(Dispatchers.Main) { listener.onCanceled() }
      } catch (e: Exception) {
        withContext(Dispatchers.Main) { listener.onError(e) }
      }
    }
  }

  /** 暂停：会保存当前进度，后续可 resume() */
  fun pause(listener: Listener? = null) {
    isPaused = true
    currentCall?.cancel()
    runningJob?.cancel()
    runningJob = null
    listener?.onPaused(downloadedBytes)
  }

  /** 继续：从 .part 续传；如服务器忽略 Range，会自动从头重下 */
  fun resume(listener: Listener) {
    val url = currentUrl ?: run {
      listener.onError(IllegalStateException("No previous url to resume"))
      return
    }
    val dest = finalFile ?: run {
      listener.onError(IllegalStateException("No previous target file to resume"))
      return
    }
    start(url, dest, (speedLimitBytesPerSec?.div(1024))?.toInt(), listener)
  }

  /** 取消下载并删除 .part */
  fun cancel(deletePart: Boolean = false) {
    isCanceled = true
    currentCall?.cancel()
    runningJob?.cancel()
    runningJob = null
    if (deletePart) {
      try { partFile?.takeIf { it.exists() }?.delete() } catch (_: Throwable) {}
    }
  }

  // ---------------- 内部实现 ----------------

  private suspend fun probeContentLength(url: String): Long = withContext(Dispatchers.IO) {
    // 先尝试 HEAD
    try {
      val headReq = Request.Builder()
        .url(url)
        .header("Accept-Encoding", "identity") // 禁用 gzip，长度更可靠
        .head()
        .build()
      client.newCall(headReq).execute().use { resp ->
        if (resp.isSuccessful) {
          val len = resp.header("Content-Length")?.toLongOrNull()
          if (len != null) return@withContext len
        }
      }
    } catch (_: Throwable) { /* fallback 到 GET */ }

    // 再用 GET 拿 contentLength（不读 body）
    val getReq = Request.Builder()
      .url(url)
      .header("Accept-Encoding", "identity")
      .get()
      .build()
    client.newCall(getReq).execute().use { resp ->
      if (!resp.isSuccessful) return@withContext -1L
      return@withContext resp.body?.contentLength() ?: -1L
    }
  }

  private suspend fun performDownload(url: String, listener: Listener) = withContext(Dispatchers.IO) {
    // 构造 Range 请求（续传）
    val reqBuilder = Request.Builder()
      .url(url)
      .header("Accept-Encoding", "identity") // 避免 gzip 导致长度与实际不符
      .header("Connection", "close")         // 某些服务器连接复用不稳定
    if (downloadedBytes > 0) {
      reqBuilder.header("Range", "bytes=$downloadedBytes-")
    }

    currentCall = client.newCall(reqBuilder.build())
    val resp = currentCall!!.execute()

    // 服务器可能忽略 Range（返回 200），那么需要从头重下
    if (downloadedBytes > 0 && resp.code == 200) {
      // 抛弃旧 .part
      partFile!!.delete()
      downloadedBytes = 0
    }

    // 计算可用的 total
    val body = resp.body ?: throw IOException("Empty body")
    val bodyLen = body.contentLength()
    if (totalBytes <= 0 && bodyLen > 0) {
      totalBytes = bodyLen + downloadedBytes // 当首次用 GET 才知道长度
    }

    // 打开 RandomAccessFile，定位到续传点
    RandomAccessFile(partFile, "rw").use { raf ->
      if (downloadedBytes > 0) raf.seek(downloadedBytes) else raf.setLength(0)

      val buf = ByteArray(64 * 1024)
      var read: Int
      var secWindowBytes = 0L
      var secWindowStart = System.currentTimeMillis()

      while (true) {
        // 限速：令牌桶（按 50ms 增发 tokens）
        maybeRefillTokens()
        val chunk = min(buf.size.toLong(), allowedToWriteNow()).toInt()
        read = body.source().read(buf, 0, if (chunk <= 0) 1 else chunk) // 至少尝试 1 字节避免卡死
        if (read == -1) break

        raf.write(buf, 0, read)
        downloadedBytes += read
        secWindowBytes += read
        consumeTokens(read)

        // 每 1 秒回调一次瞬时 & 平均速度
        val now = System.currentTimeMillis()
        if (now - secWindowStart >= 1000L) {
          val instantKbps = (secWindowBytes / 1024.0).roundToInt()
          val elapsedSec = max(1.0, (System.nanoTime() - startNano) / 1_000_000_000.0)
          val avgKbps = ((downloadedBytes) / 1024.0 / elapsedSec).roundToInt()

          val percent = if (totalBytes > 0) {
            ((downloadedBytes * 100) / totalBytes).toInt().coerceIn(0, 100)
          } else 0
          val eta = if (totalBytes > 0 && avgKbps > 0) {
            val remain = max(0L, totalBytes - downloadedBytes)
            (remain / 1024.0 / avgKbps).roundToInt().toLong()
          } else -1L

          withContext(Dispatchers.Main) {
            listener.onProgress(
              Progress(
                downloadedBytes,
                totalBytes,
                percent,
                instantKbps,
                avgKbps,
                eta
              )
            )
          }
          secWindowStart = now
          secWindowBytes = 0
        }

        // 暂停/取消处理
        if (isPaused) {
          withContext(Dispatchers.Main) { listener.onPaused(downloadedBytes) }
          return@withContext
        }
        if (isCanceled) {
          withContext(Dispatchers.Main) { listener.onCanceled() }
          return@withContext
        }
      }
    }

    // 下载完成，重命名 .part -> 正式文件
    if (!isPaused && !isCanceled) {
      // 若已存在同名目标，先删
      finalFile!!.takeIf { it.exists() }?.delete()
      if (!partFile!!.renameTo(finalFile)) {
        // 某些文件系统上 renameTo 可能失败，退化为 copy
        partFile!!.copyTo(finalFile!!, overwrite = true)
        partFile!!.delete()
      }

      withContext(Dispatchers.Main) {
        listener.onProgress(
          Progress(
            downloadedBytes = totalBytes,
            totalBytes = totalBytes,
            percent = 100,
            instantKbps = 0,
            avgKbps = ((totalBytes) / 1024.0 / max(1.0, (System.nanoTime() - startNano) / 1e9)).roundToInt(),
            etaSeconds = 0
          )
        )
        listener.onCompleted(finalFile!!)
      }
    }
  }

  // ---------------- 令牌桶限速 ----------------

  private fun resetLimiter() {
    tokens = 0
    lastRefillMs = System.currentTimeMillis()
  }

  private fun maybeRefillTokens() {
    val limit = speedLimitBytesPerSec ?: return
    val now = System.currentTimeMillis()
    val deltaMs = now - lastRefillMs
    if (deltaMs <= 0) return
    // 每毫秒补发 limit/1000 个 token
    val add = (limit * deltaMs) / 1000
    tokens = (tokens + add).coerceAtMost(limit) // 桶容量 = 1 秒配额
    lastRefillMs = now
    if (tokens <= 0) {
      // 等待部分时间，避免 busy spin
      val sleepMs = min(50, (1000 * 1.0 / max(1, (limit / 8192))).toInt()).toLong()
      Thread.sleep(max(5L, sleepMs))
    }
  }

  private fun allowedToWriteNow(): Long {
    val limit = speedLimitBytesPerSec ?: return Long.MAX_VALUE
    return max(0L, tokens.coerceAtMost(limit))
  }

  private fun consumeTokens(n: Int) {
    val limit = speedLimitBytesPerSec ?: return
    tokens = (tokens - n).coerceAtLeast(0)
    if (tokens <= 0) {
      // 轻微等待，给下次 refill 留点时间
      Thread.sleep(5)
    }
  }








  ///////////////
  interface DownloadListener {
    fun onProgress(progress: Int, speedKbps: Int)
    fun onCompleted(file: File)
    fun onError(e: Exception)
  }

  suspend fun downloadFile(
    url: String,
    destFile: File,
    speedLimitKBps: Int?, // null = 不限速
    listener: DownloadListener
  ) {
    withContext(Dispatchers.IO) {
      try {
        val client = OkHttpClient.Builder()
//          .protocols(listOf(Protocol.HTTP_1_1))
//          .readTimeout(0, TimeUnit.SECONDS) // 长连接下载大文件
          .retryOnConnectionFailure(true)
          .build()

        val request = Request.Builder()
          .url(url)
          .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("HTTP error: ${response.code}")

        val body = response.body ?: throw Exception("Empty body")
        val input: InputStream = body.byteStream()
        val output = FileOutputStream(destFile)

        val buffer = ByteArray(8192)
        var bytesRead: Int
        var totalBytesRead = 0L
        val contentLength = body.contentLength()

        var lastTime = System.currentTimeMillis()
        var bytesInSecond = 0L

        while (input.read(buffer).also { bytesRead = it } != -1) {
          output.write(buffer, 0, bytesRead)
          totalBytesRead += bytesRead
          bytesInSecond += bytesRead

          val now = System.currentTimeMillis()
          if (now - lastTime >= 1000) {
            val speedKbps = (bytesInSecond / 1024.0).roundToInt()
            val progress = if (contentLength > 0) {
              (totalBytesRead * 100 / contentLength).toInt()
            } else 0
            listener.onProgress(progress, speedKbps)
            lastTime = now
            bytesInSecond = 0
          }

          // 限速控制
          speedLimitKBps?.let { limit ->
            val maxBytesPerSec = limit * 1024
            if (bytesInSecond > maxBytesPerSec) {
              val sleepMs = 100
              Thread.sleep(sleepMs.toLong())
            }
          }
        }

        output.flush()
        output.close()
        input.close()

        listener.onProgress(100, 0)

        listener.onCompleted(destFile)
      } catch (e: Exception) {
        listener.onError(e)
      }
    }
  }
}
