package com.devokay.sample.data.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AccountService {

  @POST("xxx/login")
  @FormUrlEncoded
  Call<String> login(
    @Field("username") String username,
    @Field("password") String password
  );

}
