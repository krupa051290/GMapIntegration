package com.steven.mapintegrationassign;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by kshah on 8/19/2016.
 */
public class OkHttpConnection extends Application {

    public static OkHttpClient httpClient;
    @Override
    public void onCreate() {
        super.onCreate();

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
