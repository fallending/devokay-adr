package com.devokay.and;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.BuildConfig;
import com.devokay.and.tasks.AnonStartTask
import com.devokay.and.tasks.TaskA
import com.devokay.and.tasks.TaskB
import com.devokay.and.tasks.TaskC
import com.devokay.and.tasks.TaskData

import com.devokay.and.utils.AppCtxUtil;
import com.devokay.and.utils.LogU;
import com.devokay.and.utils.StrU;
import com.devokay.and.utils.ToastUtil;
import com.devokay.and.utils.U;
import com.devokay.and.vendors.job.JobMgr;
import com.devokay.and.vendors.sls.SLSConfig;
import com.devokay.and.vendors.sls.SLSMgr;
import com.effective.android.anchors.AnchorsManager;
import com.effective.android.anchors.taskFactory

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

object Devo {
  // MARK:
  lateinit var job: JobMgr
  lateinit var sls: SLSMgr

  var logSid: String

  // MARK: Constructor
  init {
    logSid = StrU.generateLogSessionId();
  }

  // MARK: Observe Application / Activity Lifecycle
  fun onAppOnCreate(context: Context) {
    AppCtxUtil.init(context);

//    if (LeakCanary.isInAnalyzerProcess(this)) {
//      // 该进程专门用于堆分析，不初始化 app
//      return
//    }
//    LeakCanary.install(this)

    // https://github.com/DSAppTeam/Anchors
    // https://github.com/DSAppTeam/Anchors/blob/master/app/src/main/kotlin/com/effective/android/sample/SampleApplication.kt
    TaskData().startFromApplicationOnMainProcessByDsl()


    initExecutor();

    initLogger();
  }

  // MARK: Executor

  fun initExecutor() {
    job = JobMgr();
  }

  // MARK: logger
  fun initLogger() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree());
    }
  }

  fun appendSLSLogger( endpoint: String,
                               project: String,
                              logStore: String,
                              accessKeyId: String,
                              accessKeySecret: String,
                              topic: String,
                               acceptedTags: List<String>) {
    if (Objects.isNull(sls)) {
      val conf = SLSConfig(endpoint, project, logStore, accessKeyId, accessKeySecret, topic);

      sls = SLSMgr()
      sls.build(conf)

//      Timber.plant(Timber.Tree() {
//        fun log(priority: Int, tag: String?,  message: String, t: Throwable?) {
//          if (acceptedTags != null && acceptedTags.contains(tag)) {
//            sls.sendLog(LogU.getLogLevelString(priority), logSid, tag, message);
//          }
//        }
//      });
    }
  }

  fun appendCustomLogger() {

  }
}
