package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.FillInfoPagerAdapter;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.database.BaseInfoBean;
import com.nandity.disastersystem.database.BaseInfoBeanDao;
import com.nandity.disastersystem.database.CollectInfoBeanDao;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FillInfoActivity extends AppCompatActivity {

    @BindView(R.id.fillinfo_tablayout)
    TabLayout fillinfoTablayout;
    @BindView(R.id.fillinfo_viewpager)
    ViewPager fillinfoViewpager;
    public ProgressDialog progressDialog;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    private Intent dataIntent;
    public TaskInfoBean taskInfoBean;
    private BaseInfoBeanDao baseInfoBeanDao;
    private CollectInfoBeanDao collectInfoBeanDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        ButterKnife.bind(this);
        baseInfoBeanDao= MyApplication.getDaoSession().getBaseInfoBeanDao();
        collectInfoBeanDao=MyApplication.getDaoSession().getCollectInfoBeanDao();
        dataIntent = getIntent();
        taskInfoBean = (TaskInfoBean) dataIntent.getSerializableExtra("taskBean");
        Log.d("FillInfoActivity", "上级页面传递过来的taskBean" + taskInfoBean.toString());
        initView();
        initListener();
    }
    private void initView() {
        fillinfoViewpager.setAdapter(new FillInfoPagerAdapter(getSupportFragmentManager()));
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
                BaseInfoBean unique = baseInfoBeanDao.queryBuilder().where(BaseInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
                if (unique==null){
                    ToastUtils.showShortToast("请先填写信息并保存后再上传！");
                }else {
                    String name=unique.getBaseInfoName();
                    String lng=unique.getBaseInfoLng();
                    String lat=unique.getBaseInfoLat();
                    String address=unique.getBaseInfoAddress();
                    String contact=unique.getBaseInfoContact();
                    String mobile=unique.getBaseInfoMobile();
                    String level=unique.getBaseInfoLevel();
                    String id="";
//                    OkHttpUtils.get().url(new ConnectUrl().getBaseInfoUrl())
//                            .addParams("dis_name",name)
//                            .addParams("dis_location",address)
//                            .addParams("dis_lon",lng)
//                            .addParams("dis_lat",lat)
//                            .addParams("dis_person",contact)
//                            .addParams("dis_person_phone",mobile)
//                            .addParams("dis_sf","")
//                            .addParams()
                }
            }
        });
    }

}

