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
  }

  private fun initData() {
    etUrl.setText(SAMPLE_URL_234M)

    etLimit.setText(MAX_SPEED.toString())
  }
}
