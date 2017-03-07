package com.nandity.disastersystem.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nandity.disastersystem.fragment.BaseInfoFragment;
import com.nandity.disastersystem.fragment.CollectInfoFragment;
import com.nandity.disastersystem.fragment.MediaInfoFragment;

/**
 * Created by ChenPeng on 2017/3/6.
 */

public class FillInfoPagerAdapter extends FragmentPagerAdapter {
    private String[] titles={"基础信息","采集信息","媒体信息"};
    public FillInfoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new BaseInfoFragment();
            case 1:
                return new CollectInfoFragment();
            case 2:
                return new MediaInfoFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
