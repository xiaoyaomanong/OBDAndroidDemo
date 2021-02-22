package com.example.obdandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.ui.activity.CheckRecordDetailsActivity;
import com.example.obdandroid.ui.adapter.RemindAdapter;
import com.example.obdandroid.ui.entity.RemindPageEntity;
import com.example.obdandroid.ui.entity.TestRecordEntity;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getRemindPageList_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：检测消息
 */
public class MsgFragment extends BaseFragment {
    private Context context;
    private TitleBar titleBarSet;
    private PullLoadMoreRecyclerView recycleRemind;
    private int pageNum = 1;
    private final int pageSize = 10;
    private RemindAdapter adapter;
    private boolean isLoadMore;
    private List<RemindPageEntity.DataEntity.ListEntity> datas = new ArrayList<>();

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
        recycleRemind.setLinearLayout();
        //设置是否可以下拉刷新
        recycleRemind.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        recycleRemind.setPushRefreshEnable(true);
        //显示下拉刷新
        recycleRemind.setRefreshing(true);
        //设置上拉刷新文字
        recycleRemind.setFooterViewText(getString(R.string.loading));
        //设置上拉刷新文字颜色
        recycleRemind.setFooterViewTextColor(R.color.teal_200);
        recycleRemind.setFooterViewBackgroundColor(R.color.color_080707);
        getRemindPageList(String.valueOf(pageNum), getToken(), getUserId(), true);
        adapter = new RemindAdapter(context);
        getRemindPageList(String.valueOf(pageNum), getToken(), getUserId(), true);
        recycleRemind.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getRemindPageList(String.valueOf(pageNum), getToken(), getUserId(), true);
            }

            @Override
            public void onLoadMore() {
                if (isLoadMore) {
                    pageNum++;
                    getRemindPageList(String.valueOf(pageNum), getToken(), getUserId(), false);
                } else {
                    //设置是否可以上拉刷新
                    recycleRemind.setPullLoadMoreCompleted();
                }
            }
        });

        adapter.setClickCallBack(entity -> {
          /*  OBDJsonTripEntity tripEntity = JSON.parseObject(entity.getTestData(), OBDJsonTripEntity.class);
            Intent intent = new Intent(context, CheckRecordDetailsActivity.class);
            intent.putExtra("data", tripEntity);
            startActivity(intent);*/
        });
    }

    /**
     * @param token     用户token
     * @param pageNum   页号
     * @param appUserId 用户id
     *                  获取用户消息列表
     */
    private void getRemindPageList(String pageNum, String token, String appUserId, boolean isRefresh) {
        OkHttpUtils.get().url(SERVER_URL + getRemindPageList_URL).
                addParam("token", token).
                addParam("pageNum", pageNum).
                addParam("pageSize", "10").
                addParam("appUserId", appUserId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                RemindPageEntity entity = JSON.parseObject(response, RemindPageEntity.class);
                if (entity.isSuccess()) {
                    isLoadMore = Integer.parseInt(pageNum) <= entity.getData().getPages();
                    if (isRefresh) {
                        adapter.setList(entity.getData().getList());
                        recycleRemind.setAdapter(adapter);
                        // 刷新完成后调用，必须在UI线程中
                        recycleRemind.setPullLoadMoreCompleted();
                    } else {
                        new Handler().postDelayed(() -> getActivity().runOnUiThread(() -> {
                            datas.clear();
                            datas.addAll(entity.getData().getList());
                            adapter.addFootItem(datas);
                            // 加载更多完成后调用，必须在UI线程中
                            recycleRemind.setPullLoadMoreCompleted();
                        }), 1000);
                    }
                }
            }
        });
    }
}