package com.devokay.sample.domain.request;

import android.annotation.SuppressLint;

import com.devokay.and.domain.request.Requester;

public class InfoRequester extends Requester {

  @SuppressLint("CheckResult")
  public void requestLibraryInfo() {
//    if (mLibraryResult.getValue() == null)
//      DataRepository.getInstance().getLibraryInfo()
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(mLibraryResult::setValue);
  }
}
