package com.devokay.sample.ui.bind;

import androidx.databinding.BindingAdapter;

import com.devokay.sample.ui.view.PlayPauseView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

public class IconBindingAdapter {

  @BindingAdapter(value = {"isPlaying"}, requireAll = false)
  public static void isPlaying(PlayPauseView pauseView, boolean isPlaying) {
    if (isPlaying) {
      pauseView.play();
    } else {
      pauseView.pause();
    }
  }

  @BindingAdapter(value = {"mdIcon"}, requireAll = false)
  public static void setIcon(MaterialIconView view, MaterialDrawableBuilder.IconValue iconValue) {
    view.setIcon(iconValue);
  }

  @BindingAdapter(value = {"circleAlpha"}, requireAll = false)
  public static void circleAlpha(PlayPauseView pauseView, int circleAlpha) {
    pauseView.setCircleAlpha(circleAlpha);
  }

  @BindingAdapter(value = {"drawableColor"}, requireAll = false)
  public static void drawableColor(PlayPauseView pauseView, int drawableColor) {
    pauseView.setDrawableColor(drawableColor);
  }
}
