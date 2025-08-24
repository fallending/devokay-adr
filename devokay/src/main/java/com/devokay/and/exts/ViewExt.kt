package com.devokay.and.exts

import android.view.View

/**
 * 给 View 添加防抖点击事件
 * @param interval 防抖间隔，默认 600ms
 * @param onSafeClick 点击回调
 */
public fun View.onSingleClick(onClick: (View) -> Unit) {
  var lastClickTime = 0L

  setOnClickListener { v ->
    val currentTime = System.currentTimeMillis()
    if (currentTime - lastClickTime >= 500) {
      lastClickTime = currentTime
      onClick(v)
    }
  }
}
