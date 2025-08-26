package com.devokay.and.core.http

import java.io.InputStream

class ThrottledInputStream(
  private val origin: InputStream,
  private val limiter: SpeedLimiter? = null
) : InputStream() {

  override fun read(): Int {
    limiter?.acquire(1)
    return origin.read()
  }

  override fun read(b: ByteArray, off: Int, len: Int): Int {
    val readCount = origin.read(b, off, len)
    if (readCount > 0) limiter?.acquire(readCount)
    return readCount
  }

  override fun close() {
    origin.close()
  }
}

