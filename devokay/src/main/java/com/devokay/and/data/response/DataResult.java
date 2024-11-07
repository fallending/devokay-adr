package com.devokay.and.data.response;

public class DataResult<T> {

  private final T mEntity;
  private final ResponseStatus mResponseStatus;

  public DataResult(T entity, ResponseStatus responseStatus) {
    mEntity = entity;
    mResponseStatus = responseStatus;
  }

  public DataResult(T entity) {
    mEntity = entity;
    mResponseStatus = new ResponseStatus();
  }

  public T getResult() {
    return mEntity;
  }

  public ResponseStatus getResponseStatus() {
    return mResponseStatus;
  }

  public interface Result<T> {
    void onResult(DataResult<T> dataResult);
  }
}

