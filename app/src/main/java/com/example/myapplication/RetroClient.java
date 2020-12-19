package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static final  String BASE_URL="http://192.168.1.5:8080/";
    private static RetroClient myClient;
    private Retrofit retrofit;

    private RetroClient(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized RetroClient getInstance(){
        if(myClient == null){
            myClient = new RetroClient();
        }
        return myClient;
    }

    public Api getApi(){
        return retrofit.create((Api.class));
    }
}
