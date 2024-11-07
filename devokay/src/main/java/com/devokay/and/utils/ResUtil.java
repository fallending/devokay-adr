package com.devokay.and.utils;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.util.Objects;

public class ResUtil {
  public static Drawable getDrawable(int resId) {
    return Objects.requireNonNull(ContextCompat.getDrawable(U.getApp(), resId));
  }
}
