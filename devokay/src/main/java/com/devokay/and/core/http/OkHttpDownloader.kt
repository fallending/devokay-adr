package com.devokay.and.core.http

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.roundToInt

class OkHttpDownloader(
  private val client: OkHttpClient = OkHttpClient.Builder()
    .protocols(listOf(Protocol.HTTP_1_1))
    .retryOnConnectionFailure(true)
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(0, TimeUnit.SECONDS)
    .build(),
  private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
): Downloader {
  // 注入策略
  override var resumeTransfer: ResumeTransfer? = DefaultResumeTransfer(client)
  override var listener: DownloaderListener? = null
  override var speedLimiter: SpeedLimiter? = null

  // 状态控制
  @Volatile private var isPaused = false
  @Volatile private var isCanceled = false
  @Volatile private var runningJob: Job? = null
  @Volatile private var currentCall: Call? = null

  // 下载参数
  private var currentUrl: String? = null
  private var finalFile: File? = null
  private var partFile: File? = null
  private var totalBytes: Long = -1L
  private var downloadedBytes: Long = 0L
  private var startNano: Long = 0L


  // --- 实现接口 ---
  override fun start(url: String, destFile: File, limitKBps: Int?) {
    cancel() // 保守：清理旧任务
    currentUrl = url
    finalFile = destFile
    partFile = File(destFile.absolutePath + ".part")
    isPaused = false
    isCanceled = false
    speedLimiter = limitKBps?.takeIf { it > 0 }?.let { TokenBucketLimiter(it.toLong() * 1024) }

    runningJob = externalScope.launch {
      try {
        downloadedBytes = if (partFile!!.exists()) partFile!!.length() else 0L
        totalBytes = if (resumeTransfer != null) {
          resumeTransfer?.probeContentLength(url) ?: -1L
        } else -1L

        withContext(Dispatchers.Main) { listener?.onStart(totalBytes, downloadedBytes) }

        startNano = System.nanoTime()
        speedLimiter?.reset()
        performDownload(url, listener)
      } catch (e: CancellationException) {
        withContext(Dispatchers.Main) { listener?.onCanceled() }
      } catch (e: Exception) {
        withContext(Dispatchers.Main) { listener?.onError(e) }
      }
    }
  }

  override fun pause() {
    isPaused = true
    currentCall?.cancel()
    runningJob?.cancel()
    runningJob = null
    listener?.onPaused(downloadedBytes)
  }

  override fun resume() {
    if (resumeTransfer == null) {
      listener?.onError(IllegalStateException("Resume disabled"))
      return
    }

    val url = currentUrl ?: return listener?.onError(IllegalStateException("No url")) ?: Unit
    val dest = finalFile ?: return listener?.onError(IllegalStateException("No file")) ?: Unit
    val limit = speedLimiter?.limitKBps
    start(url, dest, limit)
  }

  override fun stop() {
    isPaused = true
    currentCall?.cancel()
    runningJob?.cancel()
    runningJob = null

    listener?.onStop()
  }

  override fun cancel(deletePart: Boolean) {
    isCanceled = true
    currentCall?.cancel()
    runningJob?.cancel()
    runningJob = null

    if (deletePart) partFile?.delete()
  }

  // ---------------- 内部实现 ----------------
  private suspend fun performDownload(url: String, listener: DownloaderListener?) = withContext(Dispatchers.IO) {
    val reqBuilder = if (resumeTransfer != null) {
      resumeTransfer!!.buildRequest(url, downloadedBytes)
    } else {
      okhttp3.Request.Builder().url(url)
    }

    currentCall = client.newCall(reqBuilder.build())
    val resp = currentCall!!.execute()

    if (downloadedBytes > 0 && resp.code == 200) {
      partFile!!.delete()
      downloadedBytes = 0
    }

    val body = resp.body ?: throw IOException("Empty body")
    val bodyLen = body.contentLength()
    if (totalBytes <= 0 && bodyLen > 0) {
      totalBytes = bodyLen + downloadedBytes
    }

    RandomAccessFile(partFile, "rw").use { raf ->
      if (downloadedBytes > 0) raf.seek(downloadedBytes) else raf.setLength(0)

      val buf = ByteArray(64 * 1024)
      var secBytes = 0L
      var secStart = System.currentTimeMillis()

      while (true) {
        val read = body.source().read(buf).takeIf { it > 0 } ?: break
        speedLimiter?.acquire(read)
        raf.write(buf, 0, read)
        downloadedBytes += read
        secBytes += read

        val now = System.currentTimeMillis()
        if (now - secStart >= 1000L) {
          val instantKbps = (secBytes / 1024.0).roundToInt()
          val elapsedSec = max(1.0, (System.nanoTime() - startNano) / 1e9)
          val avgKbps = ((downloadedBytes) / 1024.0 / elapsedSec).roundToInt()
          val percent = if (totalBytes > 0) ((downloadedBytes * 100) / totalBytes).toInt() else 0
          val eta = if (totalBytes > 0 && avgKbps > 0)
            ((totalBytes - downloadedBytes) / 1024.0 / avgKbps).roundToInt().toLong()
          else -1L

          withContext(Dispatchers.Main) {
            listener?.onProgress(
              Progress(downloadedBytes, totalBytes, percent, instantKbps, avgKbps, eta)
            )
          }
          secStart = now
          secBytes = 0
        }

        if (isPaused) { withContext(Dispatchers.Main) { listener?.onPaused(downloadedBytes) }; return@withContext }
        if (isCanceled) { withContext(Dispatchers.Main) { listener?.onCanceled() }; return@withContext }
      }
    }

    if (!isPaused && !isCanceled) {
      finalFile!!.takeIf { it.exists() }?.delete()
      if (!finalFile?.let { partFile!!.renameTo(it) }!!) {
        partFile!!.copyTo(finalFile!!, overwrite = true)
        partFile!!.delete()
      }
      withContext(Dispatchers.Main) {
        listener?.onProgress(Progress(totalBytes, totalBytes, 100, 0, 0, 0))
        listener?.onCompleted(finalFile!!)
      }
    }
  }
}
