package com.devokay.and.core.http

interface DownloadTaskListener {
  fun onTaskAdded(task: DownloadTask)
  fun onTaskRemoved(task: DownloadTask)
  fun onTaskCompleted(task: DownloadTask)
}
