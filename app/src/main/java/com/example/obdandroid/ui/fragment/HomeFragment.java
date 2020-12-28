package com.example.obdandroid.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class HomeFragment extends BaseFragment {


    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }
}