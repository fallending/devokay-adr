package com.devokay.and.core.http

/** 限速器接口 */
interface SpeedLimiter {
  val limitKBps: Int?   // null 表示不限速

  fun reset()
  fun acquire(n: Int)
}
