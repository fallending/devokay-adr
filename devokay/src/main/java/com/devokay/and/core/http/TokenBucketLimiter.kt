package com.devokay.and.core.http

/** 令牌桶实现 */
class TokenBucketLimiter(private val limitBytesPerSec: Long) : SpeedLimiter {
  override val limitKBps: Int
    get() = (limitBytesPerSec / 1024).toInt()

  private var tokens: Long = 0
  private var lastRefillMs: Long = 0

  override fun reset() {
    tokens = 0
    lastRefillMs = System.currentTimeMillis()
  }

  override fun acquire(n: Int) {
    while (true) {
      maybeRefillTokens()
      if (tokens >= n) {
        tokens -= n
        return
      }
      Thread.sleep(5)
    }
  }

  private fun maybeRefillTokens() {
    val now = System.currentTimeMillis()
    val deltaMs = now - lastRefillMs
    if (deltaMs <= 0) return
    val add = (limitBytesPerSec * deltaMs) / 1000
    tokens = (tokens + add).coerceAtMost(limitBytesPerSec)
    lastRefillMs = now
  }
}
