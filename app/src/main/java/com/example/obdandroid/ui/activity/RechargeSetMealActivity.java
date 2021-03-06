package com.example.obdandroid.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.RechargeSetMealAdapter;
import com.example.obdandroid.ui.entity.ChargeMealEntity;
import com.example.obdandroid.ui.entity.PlaceAnOrderEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.PayChannelDialog;
import com.example.obdandroid.ui.view.progressButton.CircularProgressButton;
import com.example.obdandroid.ui.wechatPay.WeiXinConstants;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.CHARGE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.addRechargeRecordCheck_URL;
import static com.example.obdandroid.config.APIConfig.placeAnOrder_URL;
import static com.example.obdandroid.config.APIConfig.updateRechargeRecord_URL;
import static com.example.obdandroid.ui.wechatPay.WeiXinConstants.MEAL_ID;
import static com.example.obdandroid.ui.wechatPay.WeiXinConstants.ORDER_NO;
import static com.example.obdandroid.ui.wechatPay.WeiXinConstants.PACKAGE_VALUE;
import static com.example.obdandroid.ui.wechatPay.WeiXinConstants.PAY_MONEY;


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
    private final List<ChargeMealEntity.DataEntity.ListEntity> datas = new ArrayList<>();
    private CircularProgressButton btnBuy;
    private String rechargeSetMealSettingsId = "";
    private String rechargetAmount = "0";
    private IWXAPI wxApi;
    private LocalBroadcastManager mLocalBroadcastManager; //创建本地广播管理器类变量
    private SPUtil spUtil;

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
        wxApi = WXAPIFactory.createWXAPI(context, WeiXinConstants.APP_ID);
        spUtil = new SPUtil(context);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);//广播变量管理器获
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
            if (TextUtils.isEmpty(rechargeSetMealSettingsId)) {
                showTipsDialog("请选择套餐类型", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(rechargetAmount)) {
                showTipsDialog("请选择套餐类型", TipDialog.TYPE_ERROR);
                return;
            }
            new PayChannelDialog(context, new PayChannelDialog.DialogClick() {
                @Override
                public void aliPay(AlertDialog exitDialog, String channel, boolean confirm) {
                    if (confirm) {
                        exitDialog.dismiss();
                    }
                }

                @Override
                public void weChat(AlertDialog exitDialog, String channel, boolean confirm) {
                    if (confirm) {
                        addRechargeRecordCheck(getToken(), getUserId(), rechargetAmount, rechargeSetMealSettingsId, channel);
                        exitDialog.dismiss();
                    }
                }

                @Override
                public void Cancel(AlertDialog exitDialog, boolean confirm) {
                    if (confirm) {
                        exitDialog.dismiss();
                    }
                }
            }).setMoney(rechargetAmount).showDialog();

        });
        initReceiver();
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
     * 注册本地广播
     */
    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter("com.obd.pay");
        PayResultReceiver receiver = new PayResultReceiver();
        //绑定
        mLocalBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    private class PayResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String payResult = intent.getStringExtra("payResult");
           /* String channel = intent.getStringExtra("channel");
            String amount = intent.getStringExtra("amount");
            String mealId = intent.getStringExtra("mealId");*/
            String orderNo = intent.getStringExtra("orderNo");
            if (payResult.equals("1")) {
                btnBuy.setProgress(100);
            } else {
                btnBuy.setProgress(-1);
            }
            updateRechargeRecord(orderNo, payResult, getToken());
        }
    }

    /**
     * @param paymentChannels           支付渠道
     * @param token                     用户Token
     * @param appUserId                 用户id
     * @param rechargeSetMealSettingsId 套餐ID
     * @param amount                    支付金额
     *                                  APP用户购买套餐下单接口
     */
    private void placeAnOrder(String paymentChannels, String token, String appUserId, String rechargeSetMealSettingsId, String amount) {
        btnBuy.setProgress(0);
        new Handler().postDelayed(() -> btnBuy.setProgress(50), 3000);
        OkHttpUtils.post().url(SERVER_URL + placeAnOrder_URL).
                addParam("paymentChannels", paymentChannels).
                addParam("token", token).
                addParam("appUserId", appUserId).
                addParam("rechargeSetMealSettingsId", rechargeSetMealSettingsId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                PlaceAnOrderEntity entity = JSON.parseObject(response, PlaceAnOrderEntity.class);
                if (entity.isSuccess()) {
                    PayReq req = new PayReq();
                    req.appId = WeiXinConstants.APP_ID;
                    req.partnerId = entity.getData().getMch_id();
                    req.prepayId = entity.getData().getPrepay_id();
                    req.packageValue = PACKAGE_VALUE;
                    req.nonceStr = entity.getData().getNonce_str();
                    req.timeStamp = String.valueOf(entity.getData().getTimestamp());
                    req.sign = entity.getData().getSign();
                    boolean result = wxApi.sendReq(req);
                    if (result) {
                        spUtil.put(PAY_MONEY, amount);
                        spUtil.put(MEAL_ID, rechargeSetMealSettingsId);
                        spUtil.put(ORDER_NO, entity.getData().getOrder_no());
                    }
                } else {
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                }
            }
        });
    }

    /**
     * @param token     用户Token
     * @param appUserId APP用户ID
     *                  添加购买套餐校验
     */
    private void addRechargeRecordCheck(String token, String appUserId, String amount, String mealId, String paymentChannel) {
        OkHttpUtils.post().url(SERVER_URL + addRechargeRecordCheck_URL).
                addParam("token", token).
                addParam("appUserId", appUserId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    if (btnBuy.getProgress() == -1) {
                        btnBuy.setProgress(0);
                    }
                    placeAnOrder(paymentChannel, getToken(), getUserId(), mealId, amount);
                } else {
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                }
            }
        });

    }


    /**
     * @param orderNum 订单号
     * @param payState 支付状态(0 待支付1 成功 2 失败 3 已取消)
     * @param token    用户Token
     *                 添加购买套餐记录
     */
    private void updateRechargeRecord(String orderNum, String payState, String token) {
        OkHttpUtils.post().url(SERVER_URL + updateRechargeRecord_URL).
                addParam("orderNum", orderNum).
                addParam("payState", payState).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    setResult(102, new Intent());
                    finish();
                } else {
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
        OkHttpUtils.get().url(SERVER_URL + CHARGE_URL).
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
                    isLoadMore = entity.getData().getPages() >= Integer.parseInt(pageNum);
                    if (isRefresh) {
                        adapter.setList(entity.getData().getList());
                        recycleMeal.setAdapter(adapter);
                        // 刷新完成后调用，必须在UI线程中
                        recycleMeal.setPullLoadMoreCompleted();
                    } else {
                        new Handler().postDelayed(() -> getActivity().runOnUiThread(() -> {
                            datas.clear();
                            datas.addAll(entity.getData().getList());
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