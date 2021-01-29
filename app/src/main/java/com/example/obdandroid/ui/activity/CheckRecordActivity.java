package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.CheckRecordAdapter;
import com.example.obdandroid.ui.entity.TestRecordEntity;
import com.example.obdandroid.ui.entity.VehicleEntity;
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
import static com.example.obdandroid.config.APIConfig.getTestRecordPageList_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/29 0029
 * 描述：
 */
public class CheckRecordActivity extends BaseActivity {
    private Context context;
    private PullLoadMoreRecyclerView recycleContent;
    private int pageNum = 1;
    private final int pageSize = 10;
    private boolean isLoadMore;
    private final List<TestRecordEntity.DataEntity.ListEntity> datas = new ArrayList<>();
    private CheckRecordAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_check_record;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        recycleContent = findViewById(R.id.recycleContent);
        recycleContent.setLinearLayout();
        //设置是否可以下拉刷新
        recycleContent.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        recycleContent.setPushRefreshEnable(true);
        //显示下拉刷新
        recycleContent.setRefreshing(true);
        //设置上拉刷新文字
        recycleContent.setFooterViewText(getString(R.string.loading));
        //设置上拉刷新文字颜色
        recycleContent.setFooterViewTextColor(R.color.teal_200);
        recycleContent.setFooterViewBackgroundColor(R.color.color_080707);
        adapter = new CheckRecordAdapter(context);
        adapter.setToken(getToken());
        getTestRecordPageList(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), getUserId(), true);
        recycleContent.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getTestRecordPageList(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), getUserId(), true);
            }

            @Override
            public void onLoadMore() {
                if (isLoadMore) {
                    pageNum++;
                    getTestRecordPageList(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), getUserId(), false);
                } else {
                    //设置是否可以上拉刷新
                    recycleContent.setPullLoadMoreCompleted();
                }
            }
        });

        adapter.setClickCallBack(new CheckRecordAdapter.OnClickCallBack() {
            @Override
            public void click(TestRecordEntity.DataEntity.ListEntity entity) {

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
     * @param token     用户token
     * @param pageNum   页号
     * @param pageSize  条数
     * @param appUserId 用户id
     *                  获取用户检测记录列表
     */
    private void getTestRecordPageList( String pageNum, String pageSize,String token, String appUserId,boolean isRefresh) {
        OkHttpUtils.get().url(SERVER_URL + getTestRecordPageList_URL).
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
                TestRecordEntity entity = JSON.parseObject(response, TestRecordEntity.class);
                if (entity.isSuccess()) {
                    isLoadMore = Integer.parseInt(pageSize) <= entity.getData().getPages();
                    if (isRefresh) {
                        adapter.setList(entity.getData().getList());
                        recycleContent.setAdapter(adapter);
                        // 刷新完成后调用，必须在UI线程中
                        recycleContent.setPullLoadMoreCompleted();
                    } else {
                        new Handler().postDelayed(() -> getActivity().runOnUiThread(() -> {
                            datas.clear();
                            datas.addAll(entity.getData().getList());
                            adapter.addFootItem(datas);
                            // 加载更多完成后调用，必须在UI线程中
                            recycleContent.setPullLoadMoreCompleted();
                        }), 1000);
                    }
                } else {
                    dialogError(context, entity.getMessage());
                }
            }
        });
    }
}