package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nandity.disastersystem.R;

public class MyTaskActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        toolbar = (Toolbar) findViewById(R.id.my_task_toolbar);
        toolbar.setTitle("我的任务");
        setSupportActionBar(toolbar);
        tabLayout= (TabLayout) findViewById(R.id.my_task_tablayout);
        initView();
        setListener();
    }

    private void setListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView() {
        tabLayout.addTab(tabLayout.newTab().setText("未处理(0)"));
        tabLayout.addTab(tabLayout.newTab().setText("已上传(0)"));
        tabLayout.addTab(tabLayout.newTab().setText("未上传(0)"));
    }

}
