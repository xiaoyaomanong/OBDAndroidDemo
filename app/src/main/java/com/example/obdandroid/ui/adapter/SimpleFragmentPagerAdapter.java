package com.example.obdandroid.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragment;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragment) {
        super(fm);
        this.mFragment = mFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

}