package com.devokay.and.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class ImgUtil {

  /**
   * Return bitmap.
   *
   * @param filePath The path of file.
   * @return bitmap
   */
  public static Bitmap getBitmap(final String filePath) {
    if (isSpace(filePath)) {
      return null;
    }
    return BitmapFactory.decodeFile(filePath);
  }

  private static boolean isSpace(final String s) {
    if (s == null) {
      return true;
    }
    for (int i = 0, len = s.length(); i < len; ++i) {
      if (!Character.isWhitespace(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
