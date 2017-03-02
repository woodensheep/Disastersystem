package com.nandity.disastersystem.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lemon on 2017/3/1.
 */

public class MyUtils {

    public static String getSystemTime(){
        SimpleDateFormat formatter   =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        return str;
    }
}
