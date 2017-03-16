package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.FillInfoPagerAdapter;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.fragment.CompleteBaseInfoFragment;
import com.nandity.disastersystem.fragment.CompleteCollectInfoFragment;
import com.nandity.disastersystem.fragment.CompleteMediaInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompleteInfoActivity extends AppCompatActivity {

    @BindView(R.id.tl_back_info)
    TabLayout tlBackInfo;
    @BindView(R.id.vp_back_info)
    ViewPager vpBackInfo;
    public TaskInfoBean taskInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        ButterKnife.bind(this);
        taskInfoBean = (TaskInfoBean) getIntent().getSerializableExtra("TaskBean");
        initView();
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CompleteBaseInfoFragment());
        fragments.add(new CompleteCollectInfoFragment());
        fragments.add(new CompleteMediaInfoFragment());
        vpBackInfo.setAdapter(new FillInfoPagerAdapter(getSupportFragmentManager(), fragments));
        tlBackInfo.setupWithViewPager(vpBackInfo);
    }

}
