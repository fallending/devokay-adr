package com.devokay.sample.ui.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.devokay.sample.R;

public class PlayerService extends Service {

  public static final String NOTIFY_PREVIOUS = "pure_music.devokay.previous";
  public static final String NOTIFY_CLOSE = "pure_music.devokay.close";
  public static final String NOTIFY_PAUSE = "pure_music.devokay.pause";
  public static final String NOTIFY_PLAY = "pure_music.devokay.play";
  public static final String NOTIFY_NEXT = "pure_music.devokay.next";
  private static final String GROUP_ID = "group_001";
  private static final String CHANNEL_ID = "channel_001";

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    return START_NOT_STICKY;
  }

  @SuppressLint("UnspecifiedImmutableFlag")
  public void setListeners(RemoteViews view) {
    int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
      ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
      : PendingIntent.FLAG_UPDATE_CURRENT;
    try {
      PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
        0, new Intent(NOTIFY_PREVIOUS).setPackage(getPackageName()), flags);
      view.setOnClickPendingIntent(R.id.player_previous, pendingIntent);
      pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
        0, new Intent(NOTIFY_CLOSE).setPackage(getPackageName()), flags);
      view.setOnClickPendingIntent(R.id.player_close, pendingIntent);
      pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
        0, new Intent(NOTIFY_PAUSE).setPackage(getPackageName()), flags);
      view.setOnClickPendingIntent(R.id.player_pause, pendingIntent);
      pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
        0, new Intent(NOTIFY_NEXT).setPackage(getPackageName()), flags);
      view.setOnClickPendingIntent(R.id.player_next, pendingIntent);
      pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
        0, new Intent(NOTIFY_PLAY).setPackage(getPackageName()), flags);
      view.setOnClickPendingIntent(R.id.player_play, pendingIntent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }


}
