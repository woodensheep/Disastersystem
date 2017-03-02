package com.nandity.disastersystem.constant;

import android.content.Context;
import android.content.SharedPreferences;

import com.nandity.disastersystem.app.MyApplication;

/**
 * Created by ChenPeng on 2017/3/1.
 */

public class ConnectUrl {
    private static SharedPreferences sp=MyApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
    private static final String IP=sp.getString("IP","");
    private static final String PORT=sp.getString("PORT","");
    public static final String LOGIN_URL="http://"+IP+":"+PORT+"/cmdapp/androidLogin.do";
}
