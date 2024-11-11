package com.devokay.and.vendors.sls;

import com.aliyun.sls.android.producer.Log;
import com.aliyun.sls.android.producer.LogProducerCallback;
import com.aliyun.sls.android.producer.LogProducerClient;
import com.aliyun.sls.android.producer.LogProducerException;
import com.aliyun.sls.android.producer.LogProducerResult;
import com.devokay.and.utils.TagUtil;

public class SLSMgr implements TagUtil.Taggable {
  LogProducerClient logProducerClient;

  public void build (final SLSConfig config) {
    try {
      // callback为可选配置, 如果不需要关注日志的发送成功或失败状态, 可以不注册 callback
      final LogProducerCallback callback = new LogProducerCallback() {
        @Override
        public void onCall(int resultCode, String reqId, String errorMessage, int logBytes, int compressedBytes) {
          // resultCode: 状态码, 详见 LogProducerResult
          // reqId: 请求Id, 已经废弃
          // errorMessage: 失败信息
          // logBytes: 日志原始字节数
          // compressedBytes: 日志压缩字节数
          android.util.Log.e(TAG(), String.format("resultCode: %d, reqId: %s, errorMessage: %s, logBytes: %d, compressedBytes: %d", resultCode, reqId, errorMessage, logBytes, compressedBytes));

          final LogProducerResult result = LogProducerResult.fromInt(resultCode);
          if (LogProducerResult.LOG_PRODUCER_SEND_UNAUTHORIZED == result || LogProducerResult.LOG_PRODUCER_PARAMETERS_INVALID == result) {
            // 需要更新 AK 或者 SDK 的初始化参数
          }
        }
      };
      // 需要关注日志的发送成功或失败状态时, 第二个参数需要传入一个 callbak
      logProducerClient = new LogProducerClient(config.getProducerConfig(), callback);
    } catch (LogProducerException e) {
      e.printStackTrace();

      android.util.Log.e(TAG(), "log producer exception: "+e.toString());
    }
  }

  public void sendMockLog() {
    com.aliyun.sls.android.producer.Log log = SLSMock.createLog();
    LogProducerResult result = logProducerClient.addLog(log);

    android.util.Log.d(TAG(), "addLog result: " + result);
  }

  public void sendLog(String level, String log_session_id, String tag, String message) {
    Log log = new Log();
    log.putContent("log_message", message);
    log.putContent("log_session_id", log_session_id);
    log.putContent("log_level", level);
    log.putContent("log_tag", tag);
    log.putContent("os", "android");

    LogProducerResult result = logProducerClient.addLog(log);

    android.util.Log.d(TAG(), "addLog result: " + result);
  }
}
