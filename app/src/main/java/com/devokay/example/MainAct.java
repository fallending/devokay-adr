package com.devokay.example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.MainThread;
import androidx.drawerlayout.widget.DrawerLayout;
import com.devokay.and.ui.page.BaseAct;
import com.devokay.example.components.MyCustomPopup;
import com.devokay.example.domain.message.PageMessenger;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;

import java.util.ArrayList;
import java.util.List;

public class MainAct extends BaseAct {
  private PageMessenger mMessenger;
  private boolean mIsListened = false;
  private Button btnTestPopup1;

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
        // 在Activity或者Fragment中使用
//        List<String> xxx = new ArrayList<>{"上", "下", "左", "右", "中"};
//        XPopup popup = XPopup.Builder(this)
//          .atView(view) // 以 anchorView 为参考点弹出
//          .asAttachList(xxx, null) { position, text ->
//          Toast.makeText(this, "点击了 $text", Toast.LENGTH_SHORT).show();
//        }
//        .show();

        new XPopup.Builder(MainAct.this)
          .popupPosition(PopupPosition.Top)
          .popupAnimation(PopupAnimation.TranslateFromTop)  // 从顶部滑入动画
          .asCustom(new MyCustomPopup(MainAct.this))
          .show();

      }
    });

//    mMessenger.output(this, messages -> {
//      switch (messages.eventId) {
//        case Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED:
//          NavController nav = Navigation.findNavController(this, R.id.main_fragment_host);
//          if (nav.getCurrentDestination() != null && nav.getCurrentDestination().getId() != R.id.mainFragment) {
//            nav.navigateUp();
//          } else if (Boolean.TRUE.equals(mStates.isDrawerOpened.get())) {
//            mStates.openDrawer.set(false);
//          } else {
//            super.onBackPressed();
//          }
//          break;
//        case Messages.EVENT_OPEN_DRAWER:
//          mStates.openDrawer.set(true);
//          break;
//      }
//    });
//
//    DrawerCoordinateManager.getInstance().isEnableSwipeDrawer().observe(this, aBoolean -> {
//      mStates.allowDrawerOpen.set(aBoolean);
//    });
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
