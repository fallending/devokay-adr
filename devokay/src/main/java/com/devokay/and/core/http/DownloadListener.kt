package com.devokay.and.core.http

import java.io.File

interface DownloaderListener {
  fun onStart(totalBytes: Long, resumeFromBytes: Long)
  fun onProgress(progress: Progress)
  fun onPaused(downloadedBytes: Long)
  fun onCompleted(file: File)
  fun onError(e: Exception)
  fun onCanceled()
  fun onStop()
}
