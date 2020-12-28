package com.example.obdandroid.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
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
 * 日期：2019/8/19 0019 16:48
 */
public class IosDialog {
    private BlurView blur;
    private ViewGroup bkg;
    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private ImageView splitHorizontal;
    private TextView btnSelectNegative;
    private ImageView splitVertical;
    private TextView btnSelectPositive;
    private int blur_front_color;

    private Context context;
    private String title;
    private CharSequence message;
    private String SelectPositive;
    private String SelectNegative;
    private DialogClick dialogClick;

    public IosDialog(Context context, DialogClick dialogClick) {
        this.context = context;
        this.dialogClick = dialogClick;
    }

    public IosDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public IosDialog setMessage(CharSequence message) {
        this.message = message;
        return this;
    }

    public IosDialog setSelectPositive(String SelectPositive) {
        this.SelectPositive = SelectPositive;
        return this;
    }

    public IosDialog setSelectNegative(String SelectNegative) {
        this.SelectNegative = SelectNegative;
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
    public void showDialog() {
        final AlertDialog exitDialog = new AlertDialog.Builder(context, R.style.darkMode).create();
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_select_ios, null);
        Window window = exitDialog.getWindow();
        exitDialog.setView(rootView);
        window.setWindowAnimations(R.style.iOSAnimStyle);
        bkg = (RelativeLayout) rootView.findViewById(R.id.bkg);
        txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
        txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
        splitHorizontal = rootView.findViewById(R.id.split_horizontal);
        btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
        splitVertical = rootView.findViewById(R.id.split_vertical);
        btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);

        splitVertical.setVisibility(View.VISIBLE);

        if (isNull(title)) {
            txtDialogTitle.setVisibility(View.GONE);
        } else {
            txtDialogTitle.setVisibility(View.VISIBLE);
            txtDialogTitle.setText(title);
        }
        if (isNull(message.toString())) {
            txtDialogTip.setVisibility(View.GONE);
        } else {
            txtDialogTip.setVisibility(View.VISIBLE);
            txtDialogTip.setText(message);
        }

        btnSelectPositive.setText(SelectPositive);
        btnSelectPositive.setOnClickListener(v -> dialogClick.Confirm(exitDialog,true));
        btnSelectNegative.setVisibility(View.VISIBLE);
        btnSelectNegative.setText(SelectNegative);
        btnSelectNegative.setOnClickListener(v -> dialogClick.Cancel(exitDialog,true));

        int bkgResId;
        if (dialog_theme == THEME_DARK) {
            splitHorizontal.setBackgroundResource(R.color.ios_dialog_split_dark);
            splitVertical.setBackgroundResource(R.color.ios_dialog_split_dark);
            btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left_dark);
            btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right_dark);
            bkgResId = R.drawable.rect_dlg_dark;
            blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
        } else {
            btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left);
            btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
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
        void Confirm(AlertDialog exitDialog, boolean confirm);

        void Cancel(AlertDialog exitDialog, boolean confirm);
    }
}
