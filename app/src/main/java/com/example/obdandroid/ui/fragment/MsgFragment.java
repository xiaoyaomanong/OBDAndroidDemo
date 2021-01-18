package com.example.obdandroid.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：检测消息
 */
public class MsgFragment extends BaseFragment {

    public static MsgFragment getInstance() {
        return new MsgFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_msg;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }
}