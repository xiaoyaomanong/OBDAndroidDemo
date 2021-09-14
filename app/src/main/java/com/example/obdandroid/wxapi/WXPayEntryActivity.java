package com.example.obdandroid.wxapi;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.wechatPay.WeiXinConstants;
import com.example.obdandroid.utils.SPUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.example.obdandroid.config.Constant.PAY_ACTION;
import static com.example.obdandroid.ui.wechatPay.WeiXinConstants.MEAL_ID;
import static com.example.obdandroid.ui.wechatPay.WeiXinConstants.ORDER_NO;
import static com.example.obdandroid.ui.wechatPay.WeiXinConstants.PAY_MONEY;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    private TextView tvContent;
    private LocalBroadcastManager mLocalBroadcastManager; //创建本地广播管理器类变量
    private String rechargeStatus;
    private String amount;
    private String rechargeSetMealSettingsId;
    private SPUtil spUtil;

    @Override
    protected int getContentViewId() {
        return R.layout.pay_result;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        Context context = this;
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        TextView btnOK = findViewById(R.id.btnOK);
        spUtil = new SPUtil(context);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);//广播变量管理器获
        api = WXAPIFactory.createWXAPI(context, WeiXinConstants.APP_ID);
        api.handleIntent(getIntent(), this);
        String orderNo = spUtil.getString(ORDER_NO, "");
        tvTitle.setText("支付提示");
        btnOK.setOnClickListener(v -> {
            sendBroadCast(rechargeStatus, orderNo);
            finish();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        //支付状态 1 已下单 2 待支付 3 已支付 4已发货 5 已完成 6 已取消
        switch (resp.errCode) {
            case 0:
                tvContent.setText("支付成功");
                rechargeStatus = "3";
                break;
            case -1:
                tvContent.setText("支付失败");
                rechargeStatus = "-1";
                break;
            case -2:
                tvContent.setText("用户取消支付");
                rechargeStatus = "6";
                break;
        }
    }

    private void sendBroadCast(String status, String orderNo) {
        Intent intent = new Intent(PAY_ACTION);//创建发送广播的Action
        intent.putExtra("payResult", status);//支付结果
        intent.putExtra("orderNo", orderNo);//支付金额
        mLocalBroadcastManager.sendBroadcast(intent); //发送本地广播
    }

}