package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.FillInfoPagerAdapter;
import com.nandity.disastersystem.bean.TaskInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FillInfoActivity extends AppCompatActivity {

    @BindView(R.id.fill_info_toolbar)
    Toolbar fillInfoToolbar;
    @BindView(R.id.fillinfo_tablayout)
    TabLayout fillinfoTablayout;
    @BindView(R.id.fillinfo_viewpager)
    ViewPager fillinfoViewpager;
    public ProgressDialog progressDialog;
    private Intent dataIntent;
    public TaskInfoBean taskInfoBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        ButterKnife.bind(this);
        dataIntent=getIntent();
        taskInfoBean= (TaskInfoBean) dataIntent.getSerializableExtra("taskBean");
        Log.d("FillInfoActivity","上级页面传递过来的taskBean"+taskInfoBean.toString());
        fillInfoToolbar.setTitle("信息填报");
        setSupportActionBar(fillInfoToolbar);
        initView();
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

}
