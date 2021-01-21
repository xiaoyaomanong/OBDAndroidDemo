package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.RechargeSetMealAdapter;
import com.example.obdandroid.ui.entity.ChargeMealEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.utils.AppDateUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.CHARGE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.addRechargeRecord_URL;


/**
 * 作者：Jealous
 * 日期：2021/1/7 0007
 * 描述：
 */
public class RechargeSetMealActivity extends BaseActivity {
    private Context context;
    private PullLoadMoreRecyclerView recycleMeal;
    private int pageNum = 1;
    private final int pageSize = 10;
    private boolean isLoadMore;
    private RechargeSetMealAdapter adapter;
    private final List<ChargeMealEntity.DataEntity> datas = new ArrayList<>();
    private com.example.obdandroid.ui.view.progressButton.CircularProgressButton btnBuy;
    private String rechargeSetMealSettingsId="";
    private String rechargetAmount="";
    private final String paymentChannels="1";
    private final String rechargeStatus="1";

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
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        recycleMeal = findViewById(R.id.recycle_meal);
        btnBuy = findViewById(R.id.btnBuy);
        recycleMeal.setLinearLayout();
        //设置是否可以下拉刷新
        recycleMeal.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        recycleMeal.setPushRefreshEnable(true);
        //显示下拉刷新
        recycleMeal.setRefreshing(true);
        recycleMeal.setFooterViewBackgroundColor(R.color.color_2C2B30);
        //设置上拉刷新文字
        recycleMeal.setFooterViewText(getString(R.string.loading));
        //设置上拉刷新文字颜色
        recycleMeal.setFooterViewTextColor(R.color.color_E0AA79);
        adapter = new RechargeSetMealAdapter(context);
        getChargeMeal(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), true);
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
        adapter.setClickCallBack(entity -> {
            rechargeSetMealSettingsId = String.valueOf(entity.getRechargeSetMealSettingsId());
            rechargetAmount = String.valueOf(entity.getRechargeSetMeaAmount());
        });
        btnBuy.setIndeterminateProgressMode(true);
        btnBuy.setOnClickListener(v -> {
            if (TextUtils.isEmpty(rechargeSetMealSettingsId)){
                showTipsDialog("请选择套餐类型", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(rechargetAmount)){
                showTipsDialog("请选择套餐类型", TipDialog.TYPE_ERROR);
                return;
            }
            if (btnBuy.getProgress() == -1) {
                btnBuy.setProgress(0);
            }
            addRechargeRecord(getUserId(),rechargeSetMealSettingsId, AppDateUtils.getTodayDateTimeHms(),rechargetAmount,paymentChannels,rechargeStatus,getToken());
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
     * @param appUserId                 APP用户ID
     * @param rechargeSetMealSettingsId 套餐ID
     * @param rechargeTime              支付时间(格式为：yyyy-MM-dd HH:mm:ss)
     * @param rechargetAmount           支付金额
     * @param paymentChannels           支付渠道(1 微信 2 支付宝)
     * @param rechargeStatus            支付状态(1 成功 2 失败 3 已取消)
     * @param token                     用户Token
     *                                  添加购买套餐记录
     */
    private void addRechargeRecord(String appUserId, String rechargeSetMealSettingsId, String rechargeTime,
                                   String rechargetAmount, String paymentChannels, String rechargeStatus, String token) {
        btnBuy.setProgress(0);
        new Handler().postDelayed(() -> btnBuy.setProgress(50), 3000);
        OkHttpUtils.post().url(SERVER_URL + addRechargeRecord_URL).
                addParam("appUserId", appUserId).
                addParam("rechargeSetMealSettingsId", rechargeSetMealSettingsId).
                addParam("rechargeTime", rechargeTime).
                addParam("rechargetAmount", rechargetAmount).
                addParam("paymentChannels", paymentChannels).
                addParam("rechargeStatus", rechargeStatus).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    btnBuy.setProgress(100);
                    new CustomeDialog(context, "购买套餐成功！", confirm -> {

                    }).setPositiveButton("确定").setTitle("支付").show();
                } else {
                    btnBuy.setProgress(-1);
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                }
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
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
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

    /**
     * @param msg  提示信息
     * @param type 提示类型
     *             提示
     */
    private void showTipsDialog(String msg, int type) {
        TipDialog.show(context, msg, TipDialog.SHOW_TIME_SHORT, type);
    }

}