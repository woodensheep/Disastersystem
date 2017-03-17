package com.nandity.disastersystem.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.MyFragmentPagerAdapter;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.receiver.UpdataService;
import com.nandity.disastersystem.utils.MyUtils;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity", status, msg;
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
        //动态获取权限
        initBuild();
        //初始化视图
        initViews();
        updateManager();
        // setListeners();
    }

    private void initBuild() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    private void updateManager() {
        Log.d(TAG, "updateManager:" + MyUtils.getVerCode(this));
        OkHttpUtils.get().url(new ConnectUrl().getUpdateVerCodeUrl())
                .addParams("versionNumber", MyUtils.getVerCode(this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络故障，请检查网路！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "返回的数据：" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            Log.d(TAG, "用户数据：" + status);
                            if ("200".equals(status)) {
                                showNoticeDialog();
                            } else if ("300".equals(status)) {
//                                ToastUtils.showShortToast("暂时没有更新");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showNoticeDialog() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("软件版本更新");
        builder.setMessage(msg);
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent service = new Intent(MainActivity.this, UpdataService.class);
                startService(service);
//                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
//                intent.putExtra("settings_type","8");
//                intent.putExtra("settings", "系统信息");
//                startActivity(intent);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
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


    public void signOut() {
        new AlertDialog.Builder(this)
                .setTitle("退出程序")
                .setMessage("确定退出程序吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //initPushOut();
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
    public void signOut2() {
        new AlertDialog.Builder(this)
                .setTitle("退出程序")
                .setMessage("确定退出程序吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initPushOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(intent);
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



    /**
     * 注销推送绑定
     */
    private void initPushOut() {
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e("onSuccess", "" + s);
            }

            @Override
            public void onFailed(String s, String s1) {
                Log.e("onFailed", "" + s + s1);
            }
        });
    }
}
