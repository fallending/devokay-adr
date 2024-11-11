package com.devokay.and.vendors.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

public class JobAsync extends Job {
  private final Runnable task;

  public JobAsync(Runnable task, int priority, long delayMillis) {
    super(new Params(priority).delayInMs(delayMillis));
    this.task = task;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun() throws Throwable {
    task.run();
  }

  @Override
  protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int i, int i1) {
    return null;
  }
}
