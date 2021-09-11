package com.example.obdandroid.ui.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.ui.entity.DefaultAddressEntity;
import com.kongzue.dialog.util.BlurView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getDefault_URL;
import static com.kongzue.dialog.v2.DialogSettings.THEME_DARK;
import static com.kongzue.dialog.v2.DialogSettings.blur_alpha;
import static com.kongzue.dialog.v2.DialogSettings.dialog_background_color;
import static com.kongzue.dialog.v2.DialogSettings.dialog_theme;
import static com.kongzue.dialog.v2.DialogSettings.use_blur;

/**
 * 作者：Jealous
 * 日期：2020/5/11 0011 16:04
 */
public class PayChannelDialog {
    private BlurView blur;
    private ViewGroup bkg;
    private int blur_front_color;

    private final Context context;
    private String money;
    private final DialogClick dialogClick;
    private TextView tvPayMoney;
    private TextView btnCancel;
    private LinearLayout layoutAliPay;
    private LinearLayout layoutWeChat;
    private LinearLayout layoutAddress;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private int commodityType;//商品类型1 实物 2 虚拟 ,
    private String token;
    private String appUserId;

    public PayChannelDialog(Context context, DialogClick dialogClick) {
        this.context = context;
        this.dialogClick = dialogClick;
    }


    public PayChannelDialog setMoney(String money) {
        this.money = money;
        return this;
    }

    public PayChannelDialog setCommodityType(int commodityType) {
        this.commodityType = commodityType;
        return this;
    }

    public PayChannelDialog setToken(String token) {
        this.token = token;
        return this;
    }

    public PayChannelDialog setAppUserId(String appUserId) {
        this.appUserId = appUserId;
        return this;
    }

    /**
     * @param s 判断内容
     * @return 判空操作
     */
    private boolean isNull(String s) {
        return s == null || s.trim().isEmpty() || s.equals("null");
    }


    /**
     * 显示提示
     */
    @SuppressLint("SetTextI18n")
    public void showDialog() {
        final AlertDialog exitDialog = new AlertDialog.Builder(context, R.style.darkMode).create();
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_pay, null);
        Window window = exitDialog.getWindow();
        exitDialog.setView(rootView);
        window.setWindowAnimations(R.style.iOSAnimStyle);
        bkg = (RelativeLayout) rootView.findViewById(R.id.bkg);
        initView(rootView);
        if (!isNull(money)) {
            tvPayMoney.setText("￥" + money);
        }
        if (commodityType == 1) {
            layoutAddress.setVisibility(View.VISIBLE);
            getDefault(token, appUserId);
        } else {
            layoutAddress.setVisibility(View.GONE);
        }
        layoutAliPay.setOnClickListener(v -> dialogClick.aliPay(exitDialog, "2", tvName.getText().toString(), tvPhone.getText().toString(), tvAddress.getText().toString(), true));
        layoutWeChat.setOnClickListener(v -> dialogClick.weChat(exitDialog, "1", tvName.getText().toString(), tvPhone.getText().toString(), tvAddress.getText().toString(), true));
        btnCancel.setOnClickListener(v -> dialogClick.Cancel(exitDialog, true));

        int bkgResId;
        if (dialog_theme == THEME_DARK) {
            bkgResId = R.drawable.rect_dlg_dark;
            blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
        } else {
            bkgResId = R.drawable.rect_light;
            blur_front_color = Color.argb(blur_alpha, 255, 255, 255);      //白
        }

        if (use_blur) {
            bkg.post(() -> {
                blur = new BlurView(context, null);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bkg.getHeight());
                blur.setOverlayColor(blur_front_color);
                bkg.addView(blur, 0, params);
            });
        } else {
            bkg.setBackgroundResource(bkgResId);
        }

        if (dialog_background_color != -1) {
            bkg.setBackgroundResource(dialog_background_color);
        }
        exitDialog.show();
    }

    /**
     * @param token     接口令牌
     * @param appUserId 用户id
     *                  获取默认地址
     */
    private void getDefault(String token, String appUserId) {
        OkHttpUtils.get().url(SERVER_URL + getDefault_URL).
                addParam("token", token).
                addParam("appUserId", appUserId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                DefaultAddressEntity entity = JSON.parseObject(response, DefaultAddressEntity.class);
                if (entity.isSuccess()) {
                    tvName.setText(entity.getData().getContacts());
                    tvAddress.setText(entity.getData().getAddress());
                    tvPhone.setText(entity.getData().getTelephone());
                }
            }
        });
    }

    private void initView(View rootView) {
        tvPayMoney = rootView.findViewById(R.id.tvPayMoney);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        layoutAliPay = rootView.findViewById(R.id.layoutAliPay);
        layoutWeChat = rootView.findViewById(R.id.layoutWeChat);
        layoutAddress = rootView.findViewById(R.id.layoutAddress);
        tvAddress = rootView.findViewById(R.id.tvAddress);
        tvPhone = rootView.findViewById(R.id.tvPhone);
        tvName = rootView.findViewById(R.id.tvName);
    }

    public interface DialogClick {
        void aliPay(AlertDialog exitDialog, String channel, String contacts, String telephone, String address, boolean confirm);

        void weChat(AlertDialog exitDialog, String channel, String contacts, String telephone, String address, boolean confirm);

        void Cancel(AlertDialog exitDialog, boolean confirm);
    }
}