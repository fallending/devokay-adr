package com.devokay.and.core.http

data class Progress(
  val downloadedBytes: Long,
  val totalBytes: Long,
  val percent: Int,
  val instantKbps: Int,
  val avgKbps: Int,
  val etaSeconds: Long
)
