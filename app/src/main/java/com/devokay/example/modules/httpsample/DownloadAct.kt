package com.devokay.example.modules.httpsample

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.devokay.example.R
import kotlinx.coroutines.launch
import java.io.File

/**
 * 输入下载链接、限速值
 *
 * 点击按钮开始下载
 *
 * 显示实时下载进度、下载速度
 *
 * 下载完成 toast 提示
 */
class DownloadAct: AppCompatActivity() {
  private lateinit var etUrl: EditText
  private lateinit var etLimit: EditText
  private lateinit var btnDownload: Button
  private lateinit var progressBar: ProgressBar
  private lateinit var tvProgress: TextView
  private lateinit var tvSpeed: TextView

  private val downloader = Downloader()

  val SAMPLE_URL_1G = "http://nbg1-speed.hetzner.com/1GB.bin"
  val SAMPLE_URL_100M = "http://nbg1-speed.hetzner.com/100MB.bin"
  val SAMPLE_URL_234M = "https://fakeme.oss-cn-shanghai.aliyuncs.com/lim/cc_libs_v1.zip"

  val MAX_SPEED = 200 // KB/s

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.act_download_layout)

    etUrl = findViewById(R.id.etUrl)
    etLimit = findViewById(R.id.etLimit)
    btnDownload = findViewById(R.id.btnDownload)
    progressBar = findViewById(R.id.progressBar)
    tvProgress = findViewById(R.id.tvProgress)
    tvSpeed = findViewById(R.id.tvSpeed)

    btnDownload.setOnClickListener {
      val url = etUrl.text.toString()
      val limit = etLimit.text.toString().toIntOrNull()
      if (url.isBlank()) {
        Toast.makeText(this, "请输入下载链接", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      val destFile = File(cacheDir, "downloaded_file")
      lifecycleScope.launch {
        downloader.downloadFile(url, destFile, limit, object : Downloader.DownloadListener {
          override fun onProgress(progress: Int, speedKbps: Int) {
            runOnUiThread {
              progressBar.progress = progress
              tvProgress.text = "进度: $progress%"
              tvSpeed.text = "速度: $speedKbps KB/s"
            }
          }

          override fun onCompleted(file: File) {
            runOnUiThread {
              Toast.makeText(this@DownloadAct, "下载完成: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            }
          }

          override fun onError(e: Exception) {
            runOnUiThread {
              Toast.makeText(this@DownloadAct, "下载失败: ${e.message}", Toast.LENGTH_LONG).show()
            }
          }
        })
      }
    }

    initData()

    initViewV2()
  }

  private fun initData() {
    etUrl.setText(SAMPLE_URL_234M)

    etLimit.setText(MAX_SPEED.toString())
  }


  /////////
  private fun initViewV2() {
    val etUrl = findViewById<EditText>(R.id.etUrlV2)
    val etLimit = findViewById<EditText>(R.id.etLimitV2) // KB/s
    val btnStart = findViewById<Button>(R.id.btnStart)
    val btnPause = findViewById<Button>(R.id.btnPause)
    val btnResume = findViewById<Button>(R.id.btnResume)
    val progressBar = findViewById<ProgressBar>(R.id.progressBarV2)
    val tvProgress = findViewById<TextView>(R.id.tvProgressV2)
    val tvSpeed = findViewById<TextView>(R.id.tvSpeedV2)
    val tvEta = findViewById<TextView>(R.id.tvEta)

    progressBar.max = 100

    btnStart.setOnClickListener {
      val url = etUrl.text.toString().trim()
      val kbps = etLimit.text.toString().toIntOrNull()
      if (url.isBlank()) {
        Toast.makeText(this, "请输入下载链接", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      val dest = File(getExternalFilesDir(null), "test.bin")
      downloader.start(url, dest, kbps, object : Downloader.Listener {
        override fun onStart(totalBytes: Long, resumeFromBytes: Long) {
          tvProgress.text = if (totalBytes > 0)
            "开始，大小：${"%.2f".format(totalBytes / 1024f / 1024f)} MB，已续传：${resumeFromBytes / 1024} KB"
          else "开始，大小未知"
        }

        override fun onProgress(progress: Downloader.Progress) {
          progressBar.progress = progress.percent
          tvProgress.text = "进度：${progress.percent}% (${progress.downloadedBytes / 1024} / ${if (progress.totalBytes>0) progress.totalBytes/1024 else 0} KB)"
          tvSpeed.text = "瞬时：${progress.instantKbps} KB/s  平均：${progress.avgKbps} KB/s"
          tvEta.text = if (progress.etaSeconds >= 0) "剩余约：${progress.etaSeconds}s" else "剩余：--"
        }

        override fun onPaused(downloadedBytes: Long) {
          Toast.makeText(this@DownloadAct, "已暂停，进度 ${progressBar.progress}%", Toast.LENGTH_SHORT).show()
        }

        override fun onCompleted(file: File) {
          Toast.makeText(this@DownloadAct, "下载完成：${file.absolutePath}", Toast.LENGTH_LONG).show()
        }

        override fun onError(e: Exception) {
          Toast.makeText(this@DownloadAct, "错误：${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onCanceled() {
          Toast.makeText(this@DownloadAct, "已取消", Toast.LENGTH_SHORT).show()
        }
      })
    }

    btnPause.setOnClickListener { downloader.pause() }

    btnResume.setOnClickListener {
      downloader.resume(object : Downloader.Listener {
        override fun onStart(totalBytes: Long, resumeFromBytes: Long) {}
        override fun onProgress(progress: Downloader.Progress) {
          progressBar.progress = progress.percent
          tvSpeed.text = "瞬时：${progress.instantKbps} KB/s  平均：${progress.avgKbps} KB/s"
        }
        override fun onPaused(downloadedBytes: Long) {}
        override fun onCompleted(file: File) {
          Toast.makeText(this@DownloadAct, "下载完成：${file.absolutePath}", Toast.LENGTH_LONG).show()
        }
        override fun onError(e: Exception) {
          Toast.makeText(this@DownloadAct, "错误：${e.message}", Toast.LENGTH_LONG).show()
        }
        override fun onCanceled() {}
      })
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    downloader.cancel() // 避免 Activity 关闭后泄漏任务
  }
}
