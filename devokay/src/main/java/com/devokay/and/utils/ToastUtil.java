package com.devokay.and.utils;

import android.widget.Toast;

public class ToastUtil {

  public static void showLongToast(String text) {
    Toast.makeText(U.getApp().getApplicationContext(), text, Toast.LENGTH_LONG).show();
  }

  public static void showShortToast(String text) {
    Toast.makeText(U.getApp().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
  }
}
