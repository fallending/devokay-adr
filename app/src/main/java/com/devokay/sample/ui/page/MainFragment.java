package com.devokay.sample.ui.page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devokay.and.ui.page.BaseFragment;
import com.devokay.sample.domain.message.PageMessenger;

public class MainFragment extends BaseFragment {
  private PageMessenger mMessenger;

  @Override
  protected void initViewModel() {}

  @SuppressLint("NotifyDataSetChanged")
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


  }
}
