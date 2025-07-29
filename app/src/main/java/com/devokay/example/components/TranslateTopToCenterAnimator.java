package com.devokay.example.components;

import android.animation.ObjectAnimator;
import android.view.View;

import com.lxj.xpopup.animator.PopupAnimator;

public class TranslateTopToCenterAnimator extends PopupAnimator {

  private View target;

//  @Override
//  public void initAnimator(View target) {
//    this.target = target;
//    // 初始化时，把View放在屏幕顶部外面（上方）
//    target.setTranslationY(-target.getHeight());
//  }

  @Override
  public void initAnimator() {

  }

  @Override
  public void animateShow() {
    // 从上方滑动到Y=0（中间），你可以根据需要调整动画属性
    ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationY", -target.getHeight(), 0);
    animator.setDuration(300);
    animator.start();
  }

  @Override
  public void animateDismiss() {
    // 反向动画，滑回上方
    ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationY", 0, -target.getHeight());
    animator.setDuration(300);
    animator.start();
  }
}
