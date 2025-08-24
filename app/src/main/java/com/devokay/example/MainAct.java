package com.devokay.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.drawerlayout.widget.DrawerLayout;
import com.devokay.and.ui.page.BaseAct;
import com.devokay.example.domain.message.PageMessenger;
import com.devokay.example.modules.httpsample.DownloadAct;

public class MainAct extends BaseAct {
  private PageMessenger mMessenger;
  private boolean mIsListened = false;
  private Button btnTestPopup1;
  private Button btnHttpDownload;

  @Override
  protected void initViewModel() {
//    mStates = getActivityScopeViewModel(MainActivityStates.class);
//    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    btnTestPopup1 = findViewById(R.id.btn_test_popup1);
    btnTestPopup1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    });

    btnHttpDownload = findViewById(R.id.btn_http_download);
    btnHttpDownload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(), DownloadAct.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (!mIsListened) {
//      mMessenger.input(new Messages(Messages.EVENT_ADD_SLIDE_LISTENER));
      mIsListened = true;
    }
  }

  @Override
  public void onBackPressed() {
//    mMessenger.input(new Messages(Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED));
  }

  public static class ListenerHandler extends DrawerLayout.SimpleDrawerListener {
    @Override
    public void onDrawerOpened(View drawerView) {
      super.onDrawerOpened(drawerView);

    }

    @Override
    public void onDrawerClosed(View drawerView) {
      super.onDrawerClosed(drawerView);
    }
  }
}
