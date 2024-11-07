package com.devokay.and.data.response.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.devokay.and.utils.NetUtil;
import com.devokay.and.R;

import java.util.Objects;

public class NetworkStateReceive extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
      if (!NetUtil.isConnected()) {
        Toast.makeText(context, context.getString(R.string.network_not_good), Toast.LENGTH_SHORT).show();
      }
    }
  }

}
