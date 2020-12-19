package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("pic")
    Call<Response_POJO> uploadImage(
            @Field("file") String encodedImage,
            @Field("user_id") String user_id
    );
}
