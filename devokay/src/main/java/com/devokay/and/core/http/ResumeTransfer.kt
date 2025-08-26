package com.devokay.and.core.http

// 定义 ResumeTransfer 行为接口
interface ResumeTransfer {
  suspend fun probeContentLength(url: String): Long
  fun buildRequest(url: String, downloadedBytes: Long): okhttp3.Request.Builder
  fun isSupported(): Boolean = true
}
