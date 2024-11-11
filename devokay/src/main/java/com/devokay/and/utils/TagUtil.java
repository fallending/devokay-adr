package com.devokay.and.utils;

public class TagUtil {
  public interface Taggable {
    default String TAG() {
      return this.getClass().getSimpleName();
    }
  }
}
