package com.devokay.and.core.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class DefaultResumeTransfer(private val client: OkHttpClient) : ResumeTransfer {
  override suspend fun probeContentLength(url: String): Long {
    return try {
      val headReq = okhttp3.Request.Builder()
        .url(url)
        .header("Accept-Encoding", "identity")
        .head()
        .build()
      client.newCall(headReq).execute().use { resp ->
        resp.header("Content-Length")?.toLongOrNull() ?: -1L
      }
    } catch (_: Throwable) {
      -1L
    }
  }

  override fun buildRequest(url: String, downloadedBytes: Long): okhttp3.Request.Builder {
    val builder = okhttp3.Request.Builder()
      .url(url)
      .header("Accept-Encoding", "identity")
      .header("Connection", "close")
    if (downloadedBytes > 0) {
      builder.header("Range", "bytes=$downloadedBytes-")
    }
    return builder
  }
}
