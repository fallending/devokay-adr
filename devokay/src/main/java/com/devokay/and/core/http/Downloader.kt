package com.devokay.and.core.http

import java.io.File

interface Downloader {
  var resumeTransfer: ResumeTransfer? // 断点续传器
  var listener: DownloaderListener?   // 事件监听器
  var speedLimiter: SpeedLimiter?     // 限速器

  fun start(url: String, destFile: File, limitKBps: Int? = null)
  fun pause()
  fun resume()
  fun stop()   // 停止但不删除 part 文件
  fun cancel(deletePart: Boolean = false)
}
