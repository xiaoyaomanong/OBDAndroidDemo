package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.progressButton.CircularProgressButton;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.JumpUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.UPDATE_PASSWORD_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/6 0006
 * 描述：修改密码
 */
public class UpdatePwdActivity extends BaseActivity {
    private EditText etNewPwd;
    private EditText etNewPwdOK;
    private Context context;
    private CircularProgressButton btnUpdatePwd;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_update_pwd;
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
        etNewPwd = findViewById(R.id.etNewPwd);
        etNewPwdOK = findViewById(R.id.etNewPwdOK);
        btnUpdatePwd = findViewById(R.id.btnUpdatePwd);
        btnUpdatePwd.setIndeterminateProgressMode(true);
        btnUpdatePwd.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etNewPwd.getText().toString().trim())) {
                showTipsDialog("请输入新密码", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(etNewPwdOK.getText().toString().trim())) {
                showTipsDialog("请确认新密码", TipDialog.TYPE_ERROR);
                return;
            }
            if (!TextUtils.equals(etNewPwd.getText().toString().trim(), etNewPwdOK.getText().toString().trim())) {
                showTipsDialog("两次输入的密码不一致", TipDialog.TYPE_ERROR);
                return;
            }
            if (btnUpdatePwd.getProgress() == -1) {
                btnUpdatePwd.setProgress(0);
            }
            updatePwd(getUserId(), etNewPwdOK.getText().toString());
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
     * @param userId 用户id
     * @param newPwd 新密码
     */
    private void updatePwd(String userId, String newPwd) {
        btnUpdatePwd.setProgress(0);
        new Handler().postDelayed(() -> btnUpdatePwd.setProgress(50), 3000);
        OkHttpUtils.post().url(SERVER_URL + UPDATE_PASSWORD_URL).
                addParam("userId", userId).
                addParam("phoneNum", "").
                addParam("newPwd", newPwd).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                btnUpdatePwd.setProgress(-1);
                showTipsDialog(validateError(e, response), TipDialog.TYPE_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    btnUpdatePwd.setProgress(100);
                    new CustomeDialog(context, "密码修改成功,需要重新登录！", confirm -> {
                        if (confirm) {
                            JumpUtil.startAct(mContext, LoginActivity.class);
                        }
                    }).setPositiveButton("确定").setTitle("提示").show();
                } else {
                    btnUpdatePwd.setProgress(-1);
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
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