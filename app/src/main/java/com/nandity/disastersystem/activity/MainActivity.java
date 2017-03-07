package com.nandity.disastersystem.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.BasicCustomPushNotification;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;
import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.MyFragmentPagerAdapter;
import com.nandity.disastersystem.app.MyApplication;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private Toolbar myToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        //初始化视图
        initViews();
        // setListeners();
    }

    private void setListeners() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                //Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
                one.setIcon(R.mipmap.workbench);
                two.setIcon(R.mipmap.directory);
                three.setIcon(R.mipmap.setup);
                switch (position) {
                    case 0:
                        tab.setIcon(R.mipmap.workbench1);
                        mViewPager.setCurrentItem(0);
                        break;
                    case 1:
                        tab.setIcon(R.mipmap.directory1);
                        mViewPager.setCurrentItem(1);
                        break;
                    case 2:
                        tab.setIcon(R.mipmap.setup1);
                        mViewPager.setCurrentItem(2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);
        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);


        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        //设置Tab的图标，假如不需要则把下面的代码删去
        one.setIcon(R.drawable.selected_tab_image_workbench);
        two.setIcon(R.drawable.selected_tab_image_directory);
        three.setIcon(R.drawable.selected_tab_image_setup);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            signOut();
            return true;
        }
        return false;

    }

    public  void signOut(){
            new AlertDialog.Builder(this)
                    .setTitle("退出程序")
                    .setMessage("确定退出程序吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initPushOut();
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

    }


    private void initPushOut(){
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.unbindAccount(new CommonCallback() {
           @Override
           public void onSuccess(String s) {
               Log.e("onSuccess",""+s);
           }

           @Override
           public void onFailed(String s, String s1) {
               Log.e("onFailed",""+s+s1);
           }
       });
    }
}
