package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.FillInfoPagerAdapter;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.database.BaseInfoBean;
import com.nandity.disastersystem.database.BaseInfoBeanDao;
import com.nandity.disastersystem.database.CollectInfoBean;
import com.nandity.disastersystem.database.CollectInfoBeanDao;
import com.nandity.disastersystem.fragment.BaseInfoFragment;
import com.nandity.disastersystem.fragment.CollectInfoFragment;
import com.nandity.disastersystem.fragment.MediaInfoFragment;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FillInfoActivity extends AppCompatActivity {

    @BindView(R.id.fillinfo_tablayout)
    TabLayout fillinfoTablayout;
    @BindView(R.id.fillinfo_viewpager)
    ViewPager fillinfoViewpager;
    public ProgressDialog progressDialog, uploadProgress;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    private static final String TAG = "FillInfoActivity";
    private Intent dataIntent;
    public TaskInfoBean taskInfoBean;
    private BaseInfoBeanDao baseInfoBeanDao;
    private CollectInfoBeanDao collectInfoBeanDao;
    private SharedPreferences sp;
    private String sessionId;
    private BaseInfoBean baseInfoBean;
    private CollectInfoBean collectInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        baseInfoBeanDao = MyApplication.getDaoSession().getBaseInfoBeanDao();
        collectInfoBeanDao = MyApplication.getDaoSession().getCollectInfoBeanDao();
        dataIntent = getIntent();
        taskInfoBean = (TaskInfoBean) dataIntent.getSerializableExtra("taskBean");
        Log.d("FillInfoActivity", "上级页面传递过来的taskBean" + taskInfoBean.toString());
        initView();
        initListener();
    }

    private void initView() {
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new BaseInfoFragment());
        fragments.add(new CollectInfoFragment());
        fragments.add(new MediaInfoFragment());
        fillinfoViewpager.setAdapter(new FillInfoPagerAdapter(getSupportFragmentManager(),fragments));
        fillinfoTablayout.setupWithViewPager(fillinfoViewpager);
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        progressDialog.show();
    }

    private void initListener() {

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseInfoBean = baseInfoBeanDao.queryBuilder().where(BaseInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
                collectInfoBean = collectInfoBeanDao.queryBuilder().where(CollectInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
                if (baseInfoBean == null || collectInfoBean == null) {
                    ToastUtils.showShortToast("请先填写信息并保存后再上传！");
                } else {
                    OkHttpUtils.get().url(new ConnectUrl().getTaskStatusUrl())
                            .addParams("taskId",taskInfoBean.getmTaskId())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    ToastUtils.showShortToast("连接服务器失败！");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    String status;
                                    try {
                                        JSONObject object=new JSONObject(response);
                                        status=object.getString("status");
                                        if("300".equals(status)){
                                            uploadData();
                                        }else if ("200".equals(status)){
                                            ToastUtils.showShortToast("该任务已完成无需上传信息！");
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void uploadData() {
        uploadProgress = new ProgressDialog(FillInfoActivity.this, ProgressDialog.STYLE_SPINNER);
        uploadProgress.setCanceledOnTouchOutside(false);
        uploadProgress.setCancelable(false);
        uploadProgress.setMessage("正在上传...");
        uploadProgress.show();
        String name = baseInfoBean.getBaseInfoName();
        String lng = baseInfoBean.getBaseInfoLng();
        String lat = baseInfoBean.getBaseInfoLat();
        String address = baseInfoBean.getBaseInfoAddress();
        String contact = baseInfoBean.getBaseInfoContact();
        String mobile = baseInfoBean.getBaseInfoMobile();
        int level = Integer.valueOf(baseInfoBean.getBaseInfoLevel()) + 1;
        int isDis = Integer.valueOf(baseInfoBean.getBaseInfoIsDisaster()) + 1;
        String id = taskInfoBean.getmRowNumber();
        Log.d(TAG, "灾害点ID:" + id);
        OkHttpUtils.post().url(new ConnectUrl().getBaseInfoUrl())
                .addParams("sessionId", sessionId)
                .addParams("dis_name", name)
                .addParams("dis_location", address)
                .addParams("dis_lon", lng)
                .addParams("dis_lat", lat)
                .addParams("dis_person", contact)
                .addParams("dis_person_phone", mobile)
                .addParams("dis_sf", isDis + "")
                .addParams("dis_level", level + "")
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        uploadProgress.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                upLoadConnectInfo();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
                                sp.edit().putBoolean("isLogin",false).apply();
                                startActivity(new Intent(FillInfoActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void upLoadConnectInfo() {
        String reason = getReason(collectInfoBean.getCollectInfoDisasterReason());
        String level=getLevel(collectInfoBean.getCollectInfoDisasterLevel());
        int type=Integer.valueOf(collectInfoBean.getCollectInfoType())+1;
        int disOrDan=Integer.valueOf(collectInfoBean.getCollectInfoDisOrDan())+1;
        int isResearch=Integer.valueOf(collectInfoBean.getCollectInfoIsResearch())+1;
        collectInfoBean.setId(Long.valueOf(taskInfoBean.getTaskExtendsId()));
        Log.d("TaskActivity",Long.valueOf(taskInfoBean.getTaskExtendsId())+"");
        collectInfoBean.setCollectInfoDisasterReason(reason);
        collectInfoBean.setCollectInfoDisasterLevel(level);
        collectInfoBean.setCollectInfoType(type+"");
        collectInfoBean.setCollectInfoDisOrDan(disOrDan+"");
        collectInfoBean.setCollectInfoIsResearch(isResearch+"");
        if (collectInfoBean.getCollectInfoMeasure().equals("点击选择")){
            collectInfoBean.setCollectInfoMeasure("");
        }
        if (collectInfoBean.getCollectInfoDisposition().equals("点击选择")){
            collectInfoBean.setCollectInfoDisposition("");
        }
        Gson gson=new Gson();
        String info=gson.toJson(collectInfoBean);
        Log.d(TAG,"采集信息json字符串："+info);
        OkHttpUtils.post().url(new ConnectUrl().getConnectInfoUrl())
                .addParams("info", info)
                .addParams("sessionId", sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        uploadProgress.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
//                                deleteDao();
                                finish();
                            }else if("400".equals(status)){
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin",false).apply();
                                startActivity(new Intent(FillInfoActivity.this, LoginActivity.class));
                                finish();
                            }else {
                                ToastUtils.showShortToast(msg);
                                uploadProgress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void deleteDao() {
        baseInfoBeanDao.delete(baseInfoBean);
        collectInfoBeanDao.delete(collectInfoBean);
    }

    private String getLevel(String collectInfoDisasterLevel) {
        String result="";
        if("0".equals(collectInfoDisasterLevel)){
            result="5";
        }else if("1".equals(collectInfoDisasterLevel)){
            result="27";
        }else if ("2".equals(collectInfoDisasterLevel)){
            result="33";
        }else {
            result="35";
        }
        return result;
    }

    private String getReason(String collectInfoDisasterReason) {
        String result = "";
        if ("0".equals(collectInfoDisasterReason)) {
            result = "46";
        } else if ("1".equals(collectInfoDisasterReason)) {
            result = "45";
        } else if ("2".equals(collectInfoDisasterReason)) {
            result = "44";
        } else if ("3".equals(collectInfoDisasterReason)) {
            result = "43";
        } else if ("4".equals(collectInfoDisasterReason)) {
            result = "39";
        } else {
            result = "26";
        }
        return result;
    }


}

