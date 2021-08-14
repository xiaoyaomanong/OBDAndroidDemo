package com.example.obdandroid.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.view.IosDialog;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：个人中心设置
 */
public class AppSettingActivity extends BaseActivity {
    private Context context;
    private SPUtil spUtil;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
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
        LinearLayout layoutExit = findViewById(R.id.layout_exit);
        LinearLayout layoutUpdatePwd = findViewById(R.id.layoutUpdatePwd);
        spUtil = new SPUtil(context);
        layoutUpdatePwd.setOnClickListener(v -> JumpUtil.startAct(context, UpdatePwdActivity.class));
        //退出账户
        layoutExit.setOnClickListener(v ->
                new IosDialog(context, new IosDialog.DialogClick() {
                    @Override
                    public void Confirm(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            spUtil.put(Constant.IS_LOGIN, false);
                            JumpUtil.startAct(context, LoginActivity.class);
                            try {
                                ActivityManager.getInstance().finishActivitys();
                            } catch (Exception e) {
                                LogE("该服务未注册");
                            }
                            exitDialog.dismiss();
                        }
                    }

                    @Override
                    public void Cancel(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            exitDialog.dismiss();
                        }
                    }
                }).setMessage("是否退出客户端").setTitle("退出提示").setSelectNegative("取消").setSelectPositive("确定").showDialog());
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

}