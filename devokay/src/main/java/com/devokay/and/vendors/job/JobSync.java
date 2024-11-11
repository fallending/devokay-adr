package com.devokay.and.vendors.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.concurrent.CountDownLatch;

public class JobSync extends Job {
  private final Runnable task;
  private final CountDownLatch latch;

  public JobSync(Runnable task, int priority, long delayMillis, CountDownLatch latch) {
    super(new Params(priority).delayInMs(delayMillis));
    this.task = task;
    this.latch = latch;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun() throws Throwable {
    try {
      task.run(); // 执行 Lambda 任务
    } finally {
      latch.countDown(); // 任务完成后解除阻塞
    }
  }

  @Override
  protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
    latch.countDown(); // 任务取消时解除阻塞
  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int i, int i1) {
    return null;
  }
}
