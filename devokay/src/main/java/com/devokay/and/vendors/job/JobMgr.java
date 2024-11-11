package com.devokay.and.vendors.job;

import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.JobManager;

import com.birbit.android.jobqueue.messaging.PriorityMessageQueue;
import com.devokay.and.utils.AppCtxUtil;
import com.devokay.and.utils.TagUtil;

import java.util.concurrent.CountDownLatch;

import timber.log.Timber;

public class JobMgr implements TagUtil.Taggable {
  private final JobManager defaultJobManager;

  public JobMgr() {
    Configuration config = new Configuration.Builder(AppCtxUtil.getAppContext())
      .minConsumerCount(1)  // 最少同时执行的任务数量
      .maxConsumerCount(3)  // 最多同时执行的任务数量
      .loadFactor(3)        // 每个任务轮询获取的任务数
      .build();
    defaultJobManager = new JobManager(config);
  }

  public void sync(Runnable task, int secondsToStart) {
    try {
      CountDownLatch latch = new CountDownLatch(1);
      JobSync job = new JobSync(task, 0, secondsToStart * 1000L, latch);

      defaultJobManager.addJobInBackground(job);
      latch.await(); // 阻塞等待直到任务完成
    } catch (InterruptedException e) {
      Timber.tag(TAG()).d( "job sync exception: %s", e.toString());
    }

  }

  public void async(Runnable task, int secondsToStart) {
    JobAsync job = new JobAsync(task, 0, secondsToStart * 1000L);
    defaultJobManager.addJobInBackground(job);
  }
}
