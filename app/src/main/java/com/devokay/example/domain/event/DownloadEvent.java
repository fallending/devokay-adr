package com.devokay.example.domain.event;

public class DownloadEvent {
  public final static int EVENT_DOWNLOAD = 1;
  public final static int EVENT_DOWNLOAD_GLOBAL = 2;

  public final int eventId;

  public DownloadEvent(int eventId) {
    this.eventId = eventId;
  }
}
