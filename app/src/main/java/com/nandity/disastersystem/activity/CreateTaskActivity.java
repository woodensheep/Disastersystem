package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.CreateTaskFragmentPagerAdapter;
import com.nandity.disastersystem.adapter.MyFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTaskActivity extends AppCompatActivity {

    @BindView(R.id.create_task_toolbar)
    Toolbar createTaskToolbar;
    @BindView(R.id.create_task_tablayout)
    TabLayout cTTablayout;
    @BindView(R.id.create_task_viewpager)
    ViewPager cTViewpager;

    private CreateTaskFragmentPagerAdapter cTFragmentPagerAdapter;
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
        //createTaskTablayout.addTab(createTaskTablayout.newTab().setText("发起新任务"));
        //createTaskTablayout.addTab(createTaskTablayout.newTab().setText("未提交任务"));

        //使用适配器将ViewPager与Fragment绑定在一起
        cTFragmentPagerAdapter = new CreateTaskFragmentPagerAdapter(getSupportFragmentManager());
        cTViewpager.setAdapter(cTFragmentPagerAdapter);
        //将TabLayout与ViewPager绑定在一起
        cTTablayout.setupWithViewPager(cTViewpager);


//        //指定Tab的位置
//        one = mTabLayout.getTabAt(0);
//        two = mTabLayout.getTabAt(1);
//        three = mTabLayout.getTabAt(2);
//        //设置Tab的图标，假如不需要则把下面的代码删去
//        one.setIcon(R.drawable.selected_tab_image_workbench);
//        two.setIcon(R.drawable.selected_tab_image_directory);
//        three.setIcon(R.drawable.selected_tab_image_setup);
    }



}
