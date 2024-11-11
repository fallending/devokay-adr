package com.devokay.and.utils;

import android.app.Application;
import android.content.Context;

import java.io.File;

/**
 * @brief App context
 * @usage 1. Invoke AppCtxUtil.Init() in Application.onCreate
 *        2. Put Provider configurations to Manifest.xml
 *          <application
 *            ... >
 *          <provider
 *            android:name=".AppContextProvider"
 *            android:authorities="${applicationId}.appcontextprovider"
 *            android:exported="false" />
 *          </application>
 *        3. reflection
 */
public class AppCtxUtil {
  private static Context appContext;

  private static void loadAppContextReflect() {
    try {
      Application app = (Application) Class.forName("android.app.ActivityThread")
        .getMethod("currentApplication").invoke(null, (Object[]) null);
      if (app != null) {
        appContext = app.getApplicationContext();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 初始化方法
  public static void init(Context context) {
    if (appContext == null) {
      appContext = context.getApplicationContext();
    }
  }

  public static Context getAppContext() {
    if (appContext == null) {

    }
    if (appContext == null) {
      throw new IllegalStateException("AppContextUtil is not initialized!");
    }
    return appContext;
  }

  public static File getFilesDir() {
    return getAppContext().getFilesDir();
  }
}
