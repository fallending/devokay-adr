package com.devokay.and.core.http

//维护一个 任务队列 + 最大并发数（比如 3 个任务同时下载）。
//OkHttpDownloader 只负责具体下载，调度层负责分配/排队。
//有些服务器不支持 Range，可以检测 206 Partial Content，否则回退成全量下载。
object DownloadMgr {

 // 更强的错误处理
  // 网络错误 / 磁盘写入错误 / MD5 校验失败（如果有）最好分类。
  //
  //DownloadCallback.onError(e: Throwable) 可以扩展成带 错误码 的模型。

  // 下载进度优化
  //
  //Progress 回调目前是单次调用，可能太频繁，可以加 节流（throttle），比如 500ms 才回调一次 UI。
  //
  //下载速度（B/s）、剩余时间预估也可以计算出来，增强用户体验。


  // 扩展协议
  //
  //OkHttpDownloader 是基于 HTTP 的，你还可以考虑扩展：
  //
  //FileDownloader（本地文件复制）
  //
  //FtpDownloader
  //
  //S3Downloader
  //这样 Downloader 接口就能真正做到多实现。
}
