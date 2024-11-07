package com.devokay.sample.domain.message;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

public class DrawerCoordinateManager implements DefaultLifecycleObserver {

  private static final DrawerCoordinateManager S_HELPER = new DrawerCoordinateManager();

  private DrawerCoordinateManager() {
  }

  public static DrawerCoordinateManager getInstance() {
    return S_HELPER;
  }

  private final List<String> tagOfSecondaryPages = new ArrayList<>();

  private boolean isNoneSecondaryPage() {
    return tagOfSecondaryPages.size() == 0;
  }

  public void requestToUpdateDrawerMode(boolean pageOpened, String pageName) {
    if (pageOpened) tagOfSecondaryPages.add(pageName);
    else tagOfSecondaryPages.remove(pageName);
//    enableSwipeDrawer.setValue(isNoneSecondaryPage());
  }

  @Override
  public void onCreate(@NonNull LifecycleOwner owner) {
    tagOfSecondaryPages.add(owner.getClass().getSimpleName());
//    enableSwipeDrawer.setValue(isNoneSecondaryPage());
  }

  @Override
  public void onDestroy(@NonNull LifecycleOwner owner) {
    tagOfSecondaryPages.remove(owner.getClass().getSimpleName());
//    enableSwipeDrawer.setValue(isNoneSecondaryPage());
  }
}
