package com.nandity.disastersystem.app;

import android.app.Application;
import android.content.Context;

import com.orm.SugarApp;
import com.orm.SugarContext;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by ChenPeng on 2017/2/28.
 */

public class MyApplication extends SugarApp {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //instant run
        SugarContext.init(this);
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

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
