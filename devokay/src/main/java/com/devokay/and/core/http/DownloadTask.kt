package com.devokay.and.core.http

import java.io.File

data class DownloadTask(
  val url: String,
  val destFile: File,
  val enableResume: Boolean = true
)
