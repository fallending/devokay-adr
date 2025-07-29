package com.devokay.example.components;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;

import com.devokay.example.R;
import com.lxj.xpopup.core.BasePopupView;

public class MyCustomPopup extends BasePopupView {

  public MyCustomPopup(@NonNull Context context) {
    super(context);
  }

  @Override
  protected int getInnerLayoutId() {
    return R.layout.layout_custom_popup;
  }

//  @Override
//  protected int getImplLayoutId() {
//    return R.layout.layout_custom_popup; // 你的布局文件
//  }

  @Override
  protected void onCreate() {
    super.onCreate();
    // 初始化布局内控件，绑定事件等
  }

//  @Override
  protected Animation getShowAnimation() {
    // 创建从顶部到中间的动画，距离和时间根据需求调整
    TranslateAnimation translateAnimation = new TranslateAnimation(
      Animation.RELATIVE_TO_SELF, 0,
      Animation.RELATIVE_TO_SELF, 0,
      Animation.RELATIVE_TO_SELF, -1,  // 从上面开始，-1表示整个控件高度以上
      Animation.RELATIVE_TO_SELF, 0    // 到正常位置
    );
    translateAnimation.setDuration(300);
    translateAnimation.setInterpolator(new DecelerateInterpolator());
    return translateAnimation;
  }

//  @Override
  protected Animation getExitAnimation() {
    // 反向动画
    TranslateAnimation translateAnimation = new TranslateAnimation(
      Animation.RELATIVE_TO_SELF, 0,
      Animation.RELATIVE_TO_SELF, 0,
      Animation.RELATIVE_TO_SELF, 0,
      Animation.RELATIVE_TO_SELF, -1
    );
    translateAnimation.setDuration(300);
    translateAnimation.setInterpolator(new AccelerateInterpolator());
    return translateAnimation;
  }
}
