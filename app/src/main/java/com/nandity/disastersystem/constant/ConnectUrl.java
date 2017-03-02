package com.nandity.disastersystem.constant;

import android.content.Context;
import android.content.SharedPreferences;

import com.nandity.disastersystem.app.MyApplication;

/**
 * Created by ChenPeng on 2017/3/1.
 */

public class ConnectUrl {
    private SharedPreferences sp;
    private String port;
    private String ip;

    public ConnectUrl() {
        sp = MyApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        ip = sp.getString("IP", "");
        port = sp.getString("PORT", "");
    }

    public String getLoginUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/androidLogin.do";
    }

    public String getDirectoryUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getAddressList.do";
    }

}
