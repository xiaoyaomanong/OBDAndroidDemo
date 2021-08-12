package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.base.BaseLoginActivity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.SMSVerificationCodeEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.progressButton.CircularProgressButton;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.CountDownTimerUtils;
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
import static com.example.obdandroid.config.APIConfig.sendSMSVerificationCode_URL;
import static com.example.obdandroid.config.APIConfig.verifySMSVerificationCode_URL;

/**
 * 作者：Jealous
 * 日期：2021/5/7 0007
 * 描述：
 */
public class ForgetPwdActivity extends BaseLoginActivity {
    private EditText etNewPwd;
    private EditText etNewPwdOK;
    private Context context;
    private CircularProgressButton btnUpdatePwd;
    private CircularProgressButton btnNext;
    private CountDownTimerUtils mCountDownTimerUtils;
    private EditText etUser;
    private EditText etCode;
    private String taskID = "";
    private LinearLayout layoutOne;
    private LinearLayout layoutTwo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_forget_pwd;
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
        etUser = findViewById(R.id.etUser);
        etCode = findViewById(R.id.etCode);
        Button btnCode = findViewById(R.id.btn_code);
        layoutOne = findViewById(R.id.layoutOne);
         btnNext = findViewById(R.id.btnNext);
        layoutTwo = findViewById(R.id.layoutTwo);
        mCountDownTimerUtils = new CountDownTimerUtils(btnCode, context, 60000, 1000);
        btnCode.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etUser.getText().toString())) {
                showTipsDialog("请输入手机号", TipDialog.TYPE_ERROR);
                return;
            }
            sendSMSVerificationCode(etUser.getText().toString());
        });

        btnNext.setIndeterminateProgressMode(true);
        btnNext.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etUser.getText().toString())) {
                showTipsDialog("请输入手机号", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(etCode.getText().toString())) {
                showTipsDialog("请输入验证码", TipDialog.TYPE_ERROR);
                return;
            }
            if (btnNext.getProgress() == -1) {
                btnNext.setProgress(0);
            }
            verifySMSVerificationCode(taskID, etUser.getText().toString(), etCode.getText().toString());
        });

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
            updatePwd(etUser.getText().toString(), etNewPwdOK.getText().toString());
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
     * @param mobile 手机号
     *               发送短信验证码
     */
    private void sendSMSVerificationCode(String mobile) {
        mCountDownTimerUtils.start();
        OkHttpUtils.post().url(SERVER_URL + sendSMSVerificationCode_URL).
                addParam("mobile", mobile).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                SMSVerificationCodeEntity entity = JSON.parseObject(response, SMSVerificationCodeEntity.class);
                if (entity.isSuccess()) {
                    showTipsDialog("验证码发送成功", TipDialog.TYPE_FINISH);
                    taskID = entity.getData().getTaskID();
                    mCountDownTimerUtils.onFinish();
                } else {
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_FINISH);
                }
            }
        });
    }

    /**
     * @param taskID           短信验证码id
     * @param mobile           手机号
     * @param verificationCode 验证码
     *                         校验短信验证码
     */
    private void verifySMSVerificationCode(String taskID, String mobile, String verificationCode) {
        btnNext.setProgress(0);
        new Handler().postDelayed(() -> btnNext.setProgress(50), 3000);
        OkHttpUtils.post().url(SERVER_URL + verifySMSVerificationCode_URL).
                addParam("taskID", taskID).
                addParam("mobile", mobile).
                addParam("verificationCode", verificationCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                btnNext.setProgress(-1);
                showTipsDialog(validateError(e, response), TipDialog.TYPE_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    btnNext.setProgress(100);
                    showTipsDialog("验证通过", TipDialog.TYPE_FINISH);
                    layoutOne.setVisibility(View.GONE);
                    layoutTwo.setVisibility(View.VISIBLE);
                } else {
                    btnNext.setProgress(-1);
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                }
            }
        });
    }

    /**
     * @param phoneNum 手机号码
     * @param newPwd 新密码
     *               修改密码
     */
    private void updatePwd(String phoneNum, String newPwd) {
        btnUpdatePwd.setProgress(0);
        new Handler().postDelayed(() -> btnUpdatePwd.setProgress(50), 3000);
        OkHttpUtils.post().url(SERVER_URL + UPDATE_PASSWORD_URL).
                addParam("userId", "").
                addParam("phoneNum", phoneNum).
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
                            try {
                                ActivityManager.getInstance().finishActivitys();
                            } catch (Exception e) {
                                LogE("该服务未注册");
                            }
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