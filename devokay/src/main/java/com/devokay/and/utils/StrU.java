package com.devokay.and.utils;


import java.util.UUID;

public class StrU {

  /**
   * Generate log session id
   * @return non null string
   */
  public static String generateLogSessionId() {
    return UUID.randomUUID().toString();  // 返回一个随机生成的 UUID
  }
}
