package com.devokay.and.core.http

interface Client {
    fun newCall(request: DownloadRequest): DownloadCall

}

//val request = DownloadRequest(
//  url = "http://example.com/big.zip",
//  rangeStart = localFile.length(),
//  enableResume = true,
//  limitSpeedBytesPerSec = 128 * 1024 // 128KB/s
//)
//
//val call = okHttpDownloader.newCall(request)
//val response = call.execute()
//
//val throttledStream = request.limitSpeedBytesPerSec?.let {
//  ThrottledInputStream(response.inputStream, it)
//} ?: response.inputStream
//
//FileOutputStream(localFile, request.enableResume).use { out ->
//  throttledStream.copyTo(out)
//}
