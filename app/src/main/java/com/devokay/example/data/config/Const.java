package com.devokay.example.data.config;

import android.os.Environment;

import com.devokay.and.utils.U;
import com.devokay.example.R;

public class Const {
  public static final String COVER_PATH = U.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
  public static final String COLUMN_LINK = U.getApp().getString(R.string.article_navigation);
  public static final String PROJECT_LINK = U.getApp().getString(R.string.github_project);
}
