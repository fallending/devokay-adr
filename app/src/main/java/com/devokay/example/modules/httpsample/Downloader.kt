package com.devokay.example.modules.httpsample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class Downloader {
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
