package com.devokay.and.core.http

data class DownloadRequest(
  val url: String,
  val headers: Map<String, String> = emptyMap(),
  val rangeStart: Long? = null,
  val rangeEnd: Long? = null,
  val enableResume: Boolean = false,
  val limitSpeedBytesPerSec: Long? = null
)
