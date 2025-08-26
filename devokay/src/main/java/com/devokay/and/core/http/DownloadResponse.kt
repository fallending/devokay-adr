package com.devokay.and.core.http

data class DownloadResponse(
  val contentLength: Long,
  val inputStream: InputStream
)
