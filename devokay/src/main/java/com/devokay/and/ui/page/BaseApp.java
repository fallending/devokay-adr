package com.devokay.and.ui.page;

import android.app.Application;

import com.devokay.and.Devo;

public class BaseApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Devo.shared.onAppOnCreate(this);
  }
}
