package com.devokay.example.ui.page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devokay.and.ui.page.BaseFrag;
import com.devokay.example.domain.message.PageMessenger;

public class MainFrag extends BaseFrag {
  private PageMessenger mMessenger;

  @Override
  protected void initViewModel() {}

  @SuppressLint("NotifyDataSetChanged")
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


  }
}
