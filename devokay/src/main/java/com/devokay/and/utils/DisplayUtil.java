package com.devokay.and.utils;

public class DisplayUtil {

  /**
   * convert px to its equivalent dp
   * <p>
   * 将px转换为与之相等的dp
   */
  public static int px2dp(float pxValue) {
    final float scale = U.getApp().getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * convert dp to its equivalent px
   * <p>
   * 将dp转换为与之相等的px
   */
  public static int dp2px(float dipValue) {
    final float scale = U.getApp().getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  /**
   * convert px to its equivalent sp
   * <p>
   * 将px转换为sp
   */
  public static int px2sp(float pxValue) {
    final float fontScale = U.getApp().getResources().getDisplayMetrics().scaledDensity;
    return (int) (pxValue / fontScale + 0.5f);
  }

  /**
   * convert sp to its equivalent px
   * <p>
   * 将sp转换为px
   */
  public static int sp2px(float spValue) {
    final float fontScale = U.getApp().getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }
}
