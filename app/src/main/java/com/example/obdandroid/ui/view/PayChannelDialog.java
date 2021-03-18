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

import com.example.obdandroid.R;
import com.kongzue.dialog.util.BlurView;

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

    private Context context;
    private String money;
    private DialogClick dialogClick;
    private TextView tvPayMoney;
    private TextView btnCancel;
    private LinearLayout layoutAliPay;
    private LinearLayout layoutWeChat;

    public PayChannelDialog(Context context, DialogClick dialogClick) {
        this.context = context;
        this.dialogClick = dialogClick;
    }


    public PayChannelDialog setMoney(String money) {
        this.money = money;
        return this;
    }

    /**
     * @param s 判断内容
     * @return 判空操作
     */
    private boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null")) {
            return true;
        }
        return false;
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
        layoutAliPay.setOnClickListener(v -> dialogClick.aliPay(exitDialog, "2", true));
        layoutWeChat.setOnClickListener(v -> dialogClick.aliPay(exitDialog, "1", true));
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

    private void initView(View rootView) {
        tvPayMoney = rootView.findViewById(R.id.tvPayMoney);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        layoutAliPay = rootView.findViewById(R.id.layoutAliPay);
        layoutWeChat = rootView.findViewById(R.id.layoutWeChat);
    }

    public interface DialogClick {
        void aliPay(AlertDialog exitDialog, String channel, boolean confirm);

        void weChat(AlertDialog exitDialog, String channel, boolean confirm);

        void Cancel(AlertDialog exitDialog, boolean confirm);
    }
}