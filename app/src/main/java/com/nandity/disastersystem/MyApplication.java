package com.nandity.disastersystem;

import android.app.Application;
import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by ChenPeng on 2017/2/28.
 */

public class MyApplication extends Application{
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("Disaster"))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
