package com.devokay.and.ui.page;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devokay.and.utils.ScreenAdaptUtil;
import com.devokay.and.utils.BarUtil;
import com.devokay.and.utils.ScreenUtil;

public abstract class BaseAct extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {

    BarUtil.setStatusBarColor(this, Color.TRANSPARENT);
    BarUtil.setStatusBarLightMode(this, true);

    super.onCreate(savedInstanceState);
  }

  @Override
  public Resources getResources() {
    if (ScreenUtil.isPortrait()) {
      return ScreenAdaptUtil.adaptWidth(super.getResources(), 360);
    } else {
      return ScreenAdaptUtil.adaptHeight(super.getResources(), 640);
    }
  }

  protected void initViewModel() {}

  protected void toggleSoftInput() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  protected void openUrlInBrowser(String url) {
    Uri uri = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    startActivity(intent);
  }
}
