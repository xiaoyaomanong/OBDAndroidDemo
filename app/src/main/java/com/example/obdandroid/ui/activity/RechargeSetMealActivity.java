package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.RechargeSetMealAdapter;
import com.example.obdandroid.ui.entity.ChargeMealEntity;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.CHARGE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;


/**
 * 作者：Jealous
 * 日期：2021/1/7 0007
 * 描述：
 */
public class RechargeSetMealActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBarSet;
    private PullLoadMoreRecyclerView recycleMeal;
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isLoadMore;
    private RechargeSetMealAdapter adapter;
    private List<ChargeMealEntity.DataEntity> datas = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge_meal;
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
        recycleMeal = findViewById(R.id.recycle_meal);
        recycleMeal.setLinearLayout();
        //设置是否可以下拉刷新
        recycleMeal.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        recycleMeal.setPushRefreshEnable(true);
        //显示下拉刷新
        recycleMeal.setRefreshing(true);
        //设置上拉刷新文字
        recycleMeal.setFooterViewText(getString(R.string.loading));
        //设置上拉刷新文字颜色
        recycleMeal.setFooterViewTextColor(R.color.teal_200);
        adapter = new RechargeSetMealAdapter(context);
        getChargeMeal(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), true);
        adapter.setClickCallBack(new RechargeSetMealAdapter.OnClickCallBack() {
            @Override
            public void Click(ChargeMealEntity.DataEntity entity) {
                showToast("请充值");
            }
        });
        recycleMeal.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getChargeMeal(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), true);
            }

            @Override
            public void onLoadMore() {
                if (isLoadMore) {
                    pageNum++;
                    getChargeMeal(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), false);
                } else {
                    //设置是否可以上拉刷新
                    recycleMeal.setPullLoadMoreCompleted();
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
     * @param pageNum  页号
     * @param pageSize 条数
     *                 充值套餐
     */
    private void getChargeMeal(String pageNum, String pageSize, String token, final boolean isRefresh) {
        OkHttpUtils.get().
                url(SERVER_URL + CHARGE_URL).
                addParam("pageNum", pageNum).
                addParam("pageSize", pageSize).
                addParam("token", token).
                build().
                execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogE("充值套餐:" + response);
                        ChargeMealEntity entity = JSON.parseObject(response, ChargeMealEntity.class);
                        if (entity.isSuccess()) {
                            isLoadMore = entity.getData().size() >= 10;
                            if (isRefresh) {
                                adapter.setList(entity.getData());
                                recycleMeal.setAdapter(adapter);
                                // 刷新完成后调用，必须在UI线程中
                                recycleMeal.setPullLoadMoreCompleted();
                            } else {
                                new Handler().postDelayed(() -> getActivity().runOnUiThread(() -> {
                                    datas.clear();
                                    datas.addAll(entity.getData());
                                    adapter.addFootItem(datas);
                                    // 加载更多完成后调用，必须在UI线程中
                                    recycleMeal.setPullLoadMoreCompleted();
                                }), 1000);
                            }
                        } else {
                            dialogError(context, entity.getMessage());
                        }
                    }
                });
    }
}