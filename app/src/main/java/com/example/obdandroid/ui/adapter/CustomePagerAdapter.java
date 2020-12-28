package com.example.obdandroid.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2020/6/17 0017 17:53
 */
public class CustomePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList;
    private String[] mTitles;
    private FragmentManager fragmentManager;

    public CustomePagerAdapter(FragmentManager fm, List<Fragment> list, String[] mTitles) {
        super(fm);
        this.fragmentManager = fm;
        this.mList = list;
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    //返回PagerAdapter.POSITION_NONE保证调用notifyDataSetChanged刷新Fragment。
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;//必须返回的是POSITION_NONE，否则不会刷新

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    //这个就不说了
    private String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    //必须重写此方法，添加tag一一做记录
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }
}