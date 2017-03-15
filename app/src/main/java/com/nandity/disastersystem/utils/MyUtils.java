package com.nandity.disastersystem.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.versionName;

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
    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,                                        int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath,int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  //只返回图片的大小信息
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    /**
     * 检测当前版本
     *
     * @param context
     * @return
     */
    public static String  getVerCode(Context context) {
        int versionNumber = -1;
        try {
            versionNumber = context.getPackageManager().getPackageInfo("com.nandity.disastersystem", 0).versionCode;
            System.out.println("当前版本" + versionNumber);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("version", e.getMessage());
        }
        return versionNumber+"";
    }
    /**
     * 检测当前版本name
     *
     * @param context
     * @return
     */
    public static String  getVerName(Context context) {
        String versionName="";
        try {
            String pkName = context.getPackageName();
            versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("version", e.getMessage());
        }
        return versionName;
    }
    /**
     * apk自动安装
     * @param context
     * @param
     */
    public void openFile( Context context ) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile( new File("/sdcard/Download/app-release.apk")); //这里是APK路径
        intent.setDataAndType( uri , "application/vnd.android.package-archive" ) ;
        context.startActivity(intent);
    }


    /**
     * 时间比大小,i相差天数
     * 半天为单位
     */
    public static int TimeCompare(int i,String date1,String date2){
        //格式化时间yyyy-MM-dd HH:mm:ss
        SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int bl=-1;
        Date beginTime= null;
        try {
            beginTime = CurrentTime.parse(date1);
            Date endTime=CurrentTime.parse(date2);
            //判断是否大于两天
            if(((endTime.getTime() - beginTime.getTime()))>=(i*(12*60*60*1000))) {
                Log.d("hi", "大于i/2天");
                bl=1;
            }else{
                Log.d("hi", "小于i/2天");
                bl=0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return bl;
    }
}
