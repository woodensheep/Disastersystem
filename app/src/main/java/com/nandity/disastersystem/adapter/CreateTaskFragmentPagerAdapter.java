package com.nandity.disastersystem.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nandity.disastersystem.fragment.DirectoryFragment;
import com.nandity.disastersystem.fragment.NewTaskFragment;
import com.nandity.disastersystem.fragment.SetupFragment;
import com.nandity.disastersystem.fragment.UnTaskFragment;
import com.nandity.disastersystem.fragment.WorkbenchFragment;

/**
 * Created by lemon on 2017/2/22.
 */
public class CreateTaskFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"发起新任务", "未提交任务"};

    public CreateTaskFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new NewTaskFragment();
        }
        return new NewTaskFragment();
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