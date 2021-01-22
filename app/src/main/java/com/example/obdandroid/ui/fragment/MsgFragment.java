package com.example.obdandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.hjq.bar.TitleBar;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：检测消息
 */
public class MsgFragment extends BaseFragment {
    private Context context;
    private TitleBar titleBarSet;
    private PullLoadMoreRecyclerView recycleRemind;

    public static MsgFragment getInstance() {
        return new MsgFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_msg;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        context = getHoldingActivity();
        titleBarSet = getView(R.id.titleBarSet);
        recycleRemind = getView(R.id.recycle_Remind);
    }
}