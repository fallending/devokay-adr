package com.devokay.and.core.http

interface DownloadCall {
  fun execute(): DownloadResponse
  fun cancel()
}

