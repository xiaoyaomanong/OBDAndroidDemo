package com.example.obdandroid.wxapi;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.wechatPay.WeiXinConstants;
import com.example.obdandroid.utils.SPUtil;
import com.hacknife.immersive.Immersive;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private Context context;
    private IWXAPI api;
    private TextView tvOrderId;
    private TextView tvPayment;
    private LinearLayout layoutPayDetails;
    private TextView tvPay;
    private TextView btnSelectPositive;
    private String receiptNo;
    private String orderId;
    private String total;
    private String isInvoice;
    private boolean isSuccess;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0) {
                btnSelectPositive.setOnClickListener(v -> {
                    sendBroadCast(isSuccess, isInvoice, receiptNo);
                    finish();
                });
            } else {
                tvOrderId.setText(orderId);
                tvPayment.setText("￥" + total);
                tvPay.setText("支付失败");
                layoutPayDetails.setVisibility(View.GONE);
               /* tvPay.setTextColor(getResources().getColor(R.color.btn_red));
                ivPay.setImageResource(R.drawable.icon_pay_fail);*/
                btnSelectPositive.setOnClickListener(v -> {
                    finish();
                });
            }
        }
    };

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
        context = this;
        Immersive.setStatusBarColor(this, Color.rgb(68, 158, 239));
        api = WXAPIFactory.createWXAPI(this, WeiXinConstants.APP_ID);
        api.handleIntent(getIntent(), this);
        TextView txtDialogTitle = findViewById(R.id.txt_dialog_title);
        tvOrderId = findViewById(R.id.tv_orderId);
        tvPayment = findViewById(R.id.tv_payment);
        layoutPayDetails = findViewById(R.id.layout_pay_details);
        TextView tvReceiptNo = findViewById(R.id.tv_receiptNo);
        TextView tvPayTypeMoney = findViewById(R.id.tv_payTypemoney);
        LinearLayout layoutHint = findViewById(R.id.layout_hint);
        RecyclerView recycleDetails = findViewById(R.id.recycle_details);
        LinearLayout layoutSuccess = findViewById(R.id.layout_success);
        ImageView ivPay = findViewById(R.id.iv_pay);
        tvPay = findViewById(R.id.tv_pay);
        btnSelectPositive = findViewById(R.id.btn_selectPositive);
        txtDialogTitle.setText("在线支付");
        SPUtil spUtil = new SPUtil(context);
        orderId = spUtil.getString("orderNo", "");
        String type = spUtil.getString("Type", "");
        total = spUtil.getString("total", "");
        String payType = spUtil.getString("payType", "");
        isInvoice = spUtil.getString("isInvoice", "");
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
        Message message = new Message();
        message.arg1 = resp.errCode;
        handler.sendMessage(message);
    }

    /**
     * @param isSuccess 缴费是否成功
     * @param receiptNo 票据号
     *                  发送广播
     */
    private void sendBroadCast(boolean isSuccess, String isInvoice, String receiptNo) {
        Intent intent = new Intent("com.jqsoft.pay.Success");
        intent.putExtra("isSuccess", isSuccess);
        intent.putExtra("isInvoice", isInvoice);
        intent.putExtra("receiptNo", receiptNo);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}