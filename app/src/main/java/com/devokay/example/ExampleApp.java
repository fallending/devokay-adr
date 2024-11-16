package com.devokay.example;

import com.devokay.and.Devo;
import com.devokay.and.ui.page.BaseApp;
import com.devokay.and.utils.TagUtil;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ExampleApp
  extends BaseApp // 继承 BaseApp
  implements TagUtil.Taggable // 自动生成 TAG()
{
  @Override
  public void onCreate() {
    super.onCreate();

    testSLS();
  }

  private void testSLS() {
    Devo.shared.appendSLSLogger( // 追加 sls 日志
      "https://cn-shanghai.log.aliyuncs.com",
      "your-project",
      "your-project-log-store",
      "your-access-key-id",
      "your-access-key-secret",
      "com.devokay.example",
      List.of("tag1", "tag2"));

    Timber.tag(TAG()).e("android sls test"); // 使用 timber 打日志

    Devo.shared.job.async(() -> { // 异步任务
      Timber.tag(TAG()).e("android sls test 2");
    }, 3);
  }
}
