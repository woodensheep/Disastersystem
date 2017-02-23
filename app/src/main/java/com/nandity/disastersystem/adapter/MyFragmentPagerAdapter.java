package com.nandity.disastersystem.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nandity.disastersystem.fragment.DirectoryFragment;
import com.nandity.disastersystem.fragment.SetupFragment;
import com.nandity.disastersystem.fragment.WorkbenchFragment;

/**
 * Created by lemon on 2017/2/22.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"工作平台", "通讯录", "系统设置"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new DirectoryFragment();
        } else if (position == 2) {
            return new SetupFragment();
        }
        return new WorkbenchFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}