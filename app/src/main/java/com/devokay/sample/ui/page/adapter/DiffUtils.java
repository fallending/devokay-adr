package com.devokay.sample.ui.page.adapter;

import androidx.recyclerview.widget.DiffUtil;
import com.devokay.sample.data.bean.LibraryInfo;

public class DiffUtils {

  private DiffUtil.ItemCallback<LibraryInfo> mLibraryInfoItemCallback;

//  private DiffUtil.ItemCallback<TestAlbum.TestMusic> mTestMusicItemCallback;

  private DiffUtils() {
  }

  private static final DiffUtils S_DIFF_UTILS = new DiffUtils();

  public static DiffUtils getInstance() {
    return S_DIFF_UTILS;
  }

  public DiffUtil.ItemCallback<LibraryInfo> getLibraryInfoItemCallback() {
    if (mLibraryInfoItemCallback == null) {

    }
    return mLibraryInfoItemCallback;
  }
}
