package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.adapter.CheckRecorderAdapter;
import com.example.obdandroid.ui.adapter.TroubleCodeQueryAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.FAULT_CODE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/8 0008
 * 描述：
 */
public class TroubleCodeQueryActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_query_trouble_code;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        Context context = this;
        String troubleCodes = getIntent().getStringExtra(Constant.ACT_FLAG);
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        RecyclerView recycleContent = findViewById(R.id.recycleContent);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleContent.setLayoutManager(manager);
        TroubleCodeQueryAdapter adapter = new TroubleCodeQueryAdapter(context);
        adapter.setList(Arrays.asList(troubleCodes.replaceAll("\r|\n", ",").split(",")));
        adapter.setToken(getToken());
        recycleContent.setAdapter(adapter);
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
    }
}