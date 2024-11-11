package com.devokay.and;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.BuildConfig;

import com.devokay.and.utils.AppCtxUtil;
import com.devokay.and.utils.LogU;
import com.devokay.and.utils.StrU;
import com.devokay.and.utils.ToastUtil;
import com.devokay.and.utils.U;
import com.devokay.and.vendors.job.JobMgr;
import com.devokay.and.vendors.sls.SLSConfig;
import com.devokay.and.vendors.sls.SLSMgr;

import java.util.Objects;

import timber.log.Timber;

public class Devo {
  public static Devo shared;
  static { shared = new Devo(); }

  // MARK:

  public JobMgr job;
  public SLSMgr sls;

  public final String logSid;

  // MARK: Constructor

  private Devo() {
    this.logSid = StrU.generateLogSessionId();
  }

  // MARK: Observe Application / Activity Lifecycle

  public void onAppOnCreate(Context context) {
    AppCtxUtil.init(context);

    initExecutor();

    initLogger();
  }

  // MARK: Executor

  private void initExecutor() {
    job = new JobMgr();
  }

  // MARK: logger

  private void initLogger() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  public void appendSLSLogger(String endpoint,
                              String project,
                              String logStore,
                              String accessKeyId,
                              String accessKeySecret,
                              String topic) {
    if (Objects.isNull(sls)) {
      SLSConfig conf = new SLSConfig(endpoint, project, logStore, accessKeyId, accessKeySecret, topic);

      sls = new SLSMgr();
      sls.build(conf);

      Timber.plant(new Timber.Tree() {
        @Override
        protected void log(int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable t) {
          shared.sls.sendLog(LogU.getLogLevelString(priority), shared.logSid, tag, message);
        }
      });
    }
  }

  public void appendCustomLogger() {

  }

  // MARK:
}
