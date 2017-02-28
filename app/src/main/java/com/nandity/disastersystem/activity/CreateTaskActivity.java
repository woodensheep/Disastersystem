package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nandity.disastersystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTaskActivity extends AppCompatActivity {

    @BindView(R.id.create_task_toolbar)
    Toolbar createTaskToolbar;
    @BindView(R.id.create_task_tablayout)
    TabLayout createTaskTablayout;
    @BindView(R.id.create_task_viewpager)
    ViewPager createTaskViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);
        createTaskToolbar.setTitle("创建任务");
        setSupportActionBar(createTaskToolbar);
        initView();
    }

    private void initView() {
        createTaskTablayout.addTab(createTaskTablayout.newTab().setText("发起新任务"));
        createTaskTablayout.addTab(createTaskTablayout.newTab().setText("未提交任务"));
    }


}
