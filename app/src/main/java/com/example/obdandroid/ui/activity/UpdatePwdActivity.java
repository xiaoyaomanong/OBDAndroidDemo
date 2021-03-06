package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.utils.DialogUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.CustomDialog;
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
    private DialogUtils dialogUtils;

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
        Button btnUpdatePwd = findViewById(R.id.btnUpdatePwd);
        dialogUtils = new DialogUtils(context);
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
            updatePwd(getToken(), getUserId(), etNewPwdOK.getText().toString());
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
     * @param token  接口令牌
     * @param userId 用户id
     * @param newPwd 新密码
     *               修改密码
     */
    private void updatePwd(String token, String userId, String newPwd) {
        dialogUtils.showProgressDialog("正在修改...");
        OkHttpUtils.post().
                url(SERVER_URL + UPDATE_PASSWORD_URL).
                addParam("token", token).
                addParam("userId", userId).
                addParam("newPwd", newPwd).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    dialogUtils.dismiss();
                    new CustomeDialog(context, "密码修改成功,需要重新登录！", new CustomeDialog.DialogClick() {
                        @Override
                        public void Confirm(boolean confirm) {
                            if (confirm){

                            }
                        }
                    }).setPositiveButton("确定").setTitle("提示").show();
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