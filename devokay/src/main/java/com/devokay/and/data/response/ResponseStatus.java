package com.devokay.and.data.response;

import com.devokay.and.consts.DataSourceEnum;

public class ResponseStatus {

  private String responseCode = "";
  private boolean success = true;
  private Enum<DataSourceEnum> source = DataSourceEnum.NETWORK;

  public ResponseStatus() {
  }

  public ResponseStatus(String responseCode, boolean success) {
    this.responseCode = responseCode;
    this.success = success;
  }

  public ResponseStatus(String responseCode, boolean success, Enum<DataSourceEnum> source) {
    this(responseCode, success);
    this.source = source;
  }

  public String getResponseCode() {
    return responseCode;
  }

  public boolean isSuccess() {
    return success;
  }

  public Enum<DataSourceEnum> getSource() {
    return source;
  }
}
