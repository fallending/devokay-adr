package com.devokay.and.utils;

import android.util.Log;

public class LogU {
    public static String getLogLevelString(int logLevel) {
      switch (logLevel) {
        case Log.VERBOSE:
          return "VERBOSE";
        case Log.DEBUG:
          return "DEBUG";
        case Log.INFO:
          return "INFO";
        case Log.WARN:
          return "WARN";
        case Log.ERROR:
          return "ERROR";
        case Log.ASSERT:
          return "ASSERT";
        default:
          return "UNKNOWN";
      }
    }
}
