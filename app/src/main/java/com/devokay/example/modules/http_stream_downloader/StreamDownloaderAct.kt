package com.devokay.example.modules.http_stream_downloader

package com.example.streamdownloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import kotlin.math.roundToInt

class StreamDownloaderAct : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      DownloaderUI()
    }
  }

  @Composable
  fun DownloaderUI() {
    var url by remember { mutableStateOf("https://speed.hetzner.de/100MB.bin") }
    var speedKb by remember { mutableStateOf("128") } // KB/s
    var progress by remember { mutableStateOf(0f) }
    var percent by remember { mutableStateOf(0) }
    var isDownloading by remember { mutableStateOf(false) }
    var job: Job? by remember { mutableStateOf(null) }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text("Stream Downloader Demo", style = MaterialTheme.typography.titleMedium)

      OutlinedTextField(
        value = url,
        onValueChange = { url = it },
        label = { Text("URL") },
        modifier = Modifier.fillMaxWidth()
      )

      OutlinedTextField(
        value = speedKb,
        onValueChange = { speedKb = it.filter { ch -> ch.isDigit() } },
        label = { Text("Speed KB/s") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
      )

      LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
      Text("$percent%")

      Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(enabled = !isDownloading, onClick = {
          isDownloading = true
          val kbps = speedKb.toLongOrNull() ?: 0L
          val targetFile = File(cacheDir, "downloaded_file.bin")
          job = CoroutineScope(Dispatchers.IO).launch {
            try {
              streamDownload(url, targetFile, kbps * 1024L) { downloaded, total ->
                val p = if (total > 0) downloaded.toFloat() / total else 0f
                val pct = if (total > 0) ((downloaded * 100) / total).toInt() else 0
                withContext(Dispatchers.Main) {
                  progress = p
                  percent = pct
                }
              }
            } finally {
              withContext(Dispatchers.Main) { isDownloading = false }
            }
          }
        }) {
          Text("Start")
        }

        Button(enabled = isDownloading, onClick = {
          job?.cancel()
          isDownloading = false
        }) {
          Text("Stop")
        }
      }
    }
  }

  // ----------------- 核心下载函数 -----------------
  private suspend fun streamDownload(
    urlStr: String,
    destFile: File,
    limitBps: Long?,
    progressCallback: suspend (downloaded: Long, total: Long) -> Unit
  ) {
    val url = URL(urlStr)
    val conn = url.openConnection()
    val totalBytes = conn.contentLengthLong
    val inputStream = conn.getInputStream()
    val limiter = limitBps?.let { TokenBucketLimiter(it) }
    limiter?.reset()

    ThrottledInputStream(inputStream, limiter).use { stream ->
      FileOutputStream(destFile).use { out ->
        val buf = ByteArray(64 * 1024)
        var downloaded = 0L
        var read: Int
        while (true) {
          read = stream.read(buf)
          if (read <= 0) break
          out.write(buf, 0, read)
          downloaded += read
          progressCallback(downloaded, totalBytes)
        }
      }
    }
  }
}

