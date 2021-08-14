package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.RechargeRecordAdapter;
import com.example.obdandroid.ui.entity.RechargeRecordEntity;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getRechargeRecordPageList_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/22 0022
 * 描述：购买记录
 */
public class RechargeRecordActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBarSet;
    private PullLoadMoreRecyclerView recycleRechargeRecord;
    private RechargeRecordAdapter adapter;
    private int pageNum = 1;
    private final int pageSize = 10;
    private boolean isLoadMore;
    private final List<RechargeRecordEntity.DataEntity.ListEntity> datas = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge_record;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        titleBarSet = findViewById(R.id.titleBarSet);
        recycleRechargeRecord = findViewById(R.id.recycle_recharge_record);
        recycleRechargeRecord.setLinearLayout();
        //设置是否可以下拉刷新
        recycleRechargeRecord.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        recycleRechargeRecord.setPushRefreshEnable(true);
        //显示下拉刷新
        recycleRechargeRecord.setRefreshing(true);
        recycleRechargeRecord.setFooterViewBackgroundColor(R.color.color_2C2B30);
        //设置上拉刷新文字
        recycleRechargeRecord.setFooterViewText(getString(R.string.loading));
        //设置上拉刷新文字颜色
        recycleRechargeRecord.setFooterViewTextColor(R.color.color_E0AA79);
        adapter = new RechargeRecordAdapter(context);
        getRechargeRecordPageList(getToken(), String.valueOf(pageNum), String.valueOf(pageSize), getUserId(), true);
        recycleRechargeRecord.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getRechargeRecordPageList(getToken(), String.valueOf(pageNum), String.valueOf(pageSize), getUserId(), true);
            }

            @Override
            public void onLoadMore() {
                if (isLoadMore) {
                    pageNum++;
                    getRechargeRecordPageList(getToken(), String.valueOf(pageNum), String.valueOf(pageSize), getUserId(), false);
                } else {
                    //设置是否可以上拉刷新
                    recycleRechargeRecord.setPullLoadMoreCompleted();
                }
            }
        });
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

    /**
     * @param token     用户Token
     * @param pageNum   页号
     * @param pageSize  条数
     * @param appUserId APP用户ID
     *                  获取用户购买套餐记录列表
     */
    private void getRechargeRecordPageList(String token, String pageNum, String pageSize, String appUserId, final boolean isRefresh) {
        OkHttpUtils.get().url(SERVER_URL + getRechargeRecordPageList_URL).
                addParam("token", token).
                addParam("pageNum", pageNum).
                addParam("pageSize", pageSize).
                addParam("appUserId", appUserId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogE("获取用户购买套餐记录列表："+response);
                RechargeRecordEntity entity = JSON.parseObject(response, RechargeRecordEntity.class);
                if (entity.isSuccess()) {
                    isLoadMore = entity.getData().getPages() >= 10;
                    if (isRefresh) {
                        adapter.setList(entity.getData().getList());
                        recycleRechargeRecord.setAdapter(adapter);
                        // 刷新完成后调用，必须在UI线程中
                        recycleRechargeRecord.setPullLoadMoreCompleted();
                    } else {
                        new Handler().postDelayed(() ->
                                getActivity().runOnUiThread(() -> {
                                    datas.clear();
                                    datas.addAll(entity.getData().getList());
                                    adapter.addFootItem(datas);
                                    // 加载更多完成后调用，必须在UI线程中
                                    recycleRechargeRecord.setPullLoadMoreCompleted();
                                }), 1000);
                    }
                } else {
                    dialogError(context, entity.getMessage());
                }
            }
        });
    }

}