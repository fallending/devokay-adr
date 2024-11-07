package com.devokay.and.data.response.manager;

import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.devokay.and.utils.U;

public class NetworkStateManager implements DefaultLifecycleObserver {

  private static final NetworkStateManager S_MANAGER = new NetworkStateManager();
  private final NetworkStateReceive mNetworkStateReceive = new NetworkStateReceive();

  private NetworkStateManager() {
  }

  public static NetworkStateManager getInstance() {
    return S_MANAGER;
  }

  @Override
  public void onResume(@NonNull LifecycleOwner owner) {
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    U.getApp().getApplicationContext().registerReceiver(mNetworkStateReceive, filter);
  }

  @Override
  public void onPause(@NonNull LifecycleOwner owner) {
    U.getApp().getApplicationContext().unregisterReceiver(mNetworkStateReceive);
  }
}
