package com.devokay.sample.data.repo;

import com.devokay.sample.data.api.APIs;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepo {

  private static final DataRepo S_REQUEST_MANAGER = new DataRepo();

  private DataRepo() {
  }

  public static DataRepo getInstance() {
    return S_REQUEST_MANAGER;
  }

  private final Retrofit retrofit;

  {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder()
      .connectTimeout(8, TimeUnit.SECONDS)
      .readTimeout(8, TimeUnit.SECONDS)
      .writeTimeout(8, TimeUnit.SECONDS)
      .addInterceptor(logging)
      .build();
    retrofit = new Retrofit.Builder()
      .baseUrl(APIs.BASE_URL)
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .build();
  }
}
