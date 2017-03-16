package com.nandity.disastersystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nandity.disastersystem.R;

import java.util.Observable;

import static android.R.attr.duration;
import static com.taobao.accs.utl.ALog.Level.I;

public class MySplashActivity extends AppCompatActivity {

    private ImageView ivSplash;
    private SharedPreferences sp;
    private boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_splash);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        isLogin=sp.getBoolean("isLogin",false);
        ivSplash= (ImageView) findViewById(R.id.iv_splash);
        Glide.with(this)
                .load(R.mipmap.mysplash)
                .into(ivSplash);
//                .into(new GlideDrawableImageViewTarget(ivSplash,1));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent=null;
                if (isLogin){
                    intent=new Intent(MySplashActivity.this,MainActivity.class);
                }else {
                    intent=new Intent(MySplashActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2500);


    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode==KeyEvent.KEYCODE_BACK){
//            Intent i= new Intent(Intent.ACTION_MAIN);  //主启动，不期望接收数据
//
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);       //新的activity栈中开启，或者已经存在就调到栈前
//
//            i.addCategory(Intent.CATEGORY_HOME);            //添加种类，为设备首次启动显示的页面
//
//            startActivity(i);
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
