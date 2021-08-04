package com.example.obdandroid.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
 * 日期：2017/10/20 0020 14:04
 */

public class CustomeDialog {
    private BlurView blur;
    private ViewGroup bkg;
    private TextView txt_dialog_title;
    private TextView txt_dialog_msg;
    private TextView btn_selectPositive;
    private int blur_front_color;

    private Context context;
    private String title;
    private String message;
    private String SelectPositive;
    private DialogClick dialogClick;

    public CustomeDialog(Context context, String message, DialogClick dialogClick) {
        this.context = context;
        this.message=message;
        this.dialogClick = dialogClick;
    }

    public CustomeDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomeDialog setPositiveButton(String SelectPositive) {
        this.SelectPositive = SelectPositive;
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
    public void show() {
        final AlertDialog exitDialog = new AlertDialog.Builder(context, R.style.darkMode).create();
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_custome, null);
        Window window = exitDialog.getWindow();
        exitDialog.setView(rootView);
        window.setWindowAnimations(R.style.iOSAnimStyle);
        bkg = (RelativeLayout) rootView.findViewById(R.id.bkg);
        txt_dialog_title = rootView.findViewById(R.id.txt_dialog_title);
        txt_dialog_msg = rootView.findViewById(R.id.txt_dialog_msg);
        btn_selectPositive = rootView.findViewById(R.id.btn_selectPositive);
        if (isNull(title)) {
            txt_dialog_title.setVisibility(View.GONE);
        } else {
            txt_dialog_title.setVisibility(View.VISIBLE);
            txt_dialog_title.setText(title);
        }
        if (isNull(message)) {
            txt_dialog_msg.setVisibility(View.GONE);
        } else {
            txt_dialog_msg.setVisibility(View.VISIBLE);
            txt_dialog_msg.setText(message);
        }

        btn_selectPositive.setText(SelectPositive);
        btn_selectPositive.setOnClickListener(v -> {
            dialogClick.Confirm(true);
            exitDialog.dismiss();
        });

        int bkgResId;
        if (dialog_theme == THEME_DARK) {
            btn_selectPositive.setBackgroundResource(R.drawable.button_dialog_right_dark);
            bkgResId = R.drawable.rect_dlg_dark;
            blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
        } else {
            btn_selectPositive.setBackgroundResource(R.drawable.button_dialog_right);
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

    public interface DialogClick {
        void Confirm(boolean confirm);
    }
}
