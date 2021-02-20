package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.SMSVerificationCodeEntity;
import com.example.obdandroid.ui.entity.UserLoginEntity;
import com.example.obdandroid.ui.view.progressButton.CircularProgressButton;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.CountDownTimerUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.kongzue.dialog.v2.TipDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.obdandroid.config.APIConfig.LOGIN_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.sendSMSVerificationCode_URL;
import static com.example.obdandroid.config.APIConfig.verifySMSVerificationCode_URL;
import static com.example.obdandroid.config.Constant.EXPIRE_TIME;
import static com.example.obdandroid.config.Constant.IS_CHECK;
import static com.example.obdandroid.config.Constant.PASSWORD;
import static com.example.obdandroid.config.Constant.TOKEN;
import static com.example.obdandroid.config.Constant.USER_ID;
import static com.example.obdandroid.config.Constant.USER_NAME;
import static com.example.obdandroid.config.TAG.TAG_Activity;

/**
 * 作者：Jealous
 * 日期：2020/12/22 0022
 * 描述：
 */
public class LoginActivity extends BaseActivity {
    private Context context;
    private EditText etUser;
    private EditText etPwd;
    private TextView btnSignUp;
    private CircularProgressButton btnSignIn;
    private TextView tvForget;
    private AppCompatCheckBox cbMima;
    private SPUtil spUtil;
    private TextInputLayout textLayout;
    private TextView tvOtherLogin;
    private EditText etCode;
    private Button btnCode;
    private LinearLayout layoutCode;
    private int loginType = 1;
    private String taskID="";
    private CountDownTimerUtils mCountDownTimerUtils;
    private TextView tvTitle;
    private LinearLayout layoutLogin;
    private TextView tvVersion;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        etUser = findViewById(R.id.etUser);
        etPwd = findViewById(R.id.etPwd);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForget = findViewById(R.id.tvForget);
        cbMima = findViewById(R.id.cb_mima);
        textLayout = findViewById(R.id.textLayout);
        tvOtherLogin = findViewById(R.id.tvOtherLogin);
        etCode = findViewById(R.id.etCode);
        btnCode = findViewById(R.id.btn_code);
        layoutCode = findViewById(R.id.layoutCode);
        tvTitle = findViewById(R.id.tv_title);
        tvVersion = findViewById(R.id.tvVersion);

        spUtil = new SPUtil(context);
        String OtherLogin = spUtil.getString("OtherLogin", getString(R.string.text_pwd_msg));
        tvOtherLogin.setText(OtherLogin);
        getPermission();
        if (OtherLogin.contains("账号")) {//验证码登录
            cbMima.setVisibility(View.INVISIBLE);
            textLayout.setVisibility(View.GONE);
            layoutCode.setVisibility(View.VISIBLE);
            tvTitle.setText("短信快捷登录");
            loginType = 2;
        } else {//密码登录
            cbMima.setVisibility(View.VISIBLE);
            layoutCode.setVisibility(View.GONE);
            textLayout.setVisibility(View.VISIBLE);
            loginType = 1;
            tvTitle.setText("账号密码登录");
        }
        tvOtherLogin.setOnClickListener(v -> {
            if (tvOtherLogin.getText().toString().contains("账号")) {//密码登录
                cbMima.setVisibility(View.VISIBLE);
                textLayout.setVisibility(View.VISIBLE);
                layoutCode.setVisibility(View.GONE);
                tvOtherLogin.setText(getString(R.string.text_smd_msg));
                spUtil.put("OtherLogin", getString(R.string.text_smd_msg));
                tvTitle.setText("账号密码登录");
                loginType = 1;
            } else {//验证码登录
                cbMima.setVisibility(View.INVISIBLE);
                textLayout.setVisibility(View.GONE);
                layoutCode.setVisibility(View.VISIBLE);
                tvOtherLogin.setText(getString(R.string.text_pwd_msg));
                spUtil.put("OtherLogin", getString(R.string.text_pwd_msg));
                tvTitle.setText("短信快捷登录");
                loginType = 2;
            }
        });
        //判断记住密码多选框的状态
        if (spUtil.getBoolean(IS_CHECK, false)) {
            //设置默认是记录密码状态
            cbMima.setChecked(true);
            String UserName = spUtil.getString(USER_NAME, "");
            String Pwd = spUtil.getString(PASSWORD, "");
            etUser.setText(UserName);
            etPwd.setText(Pwd);
        }
        btnSignIn.setIndeterminateProgressMode(true);
        btnSignIn.setOnClickListener(v -> {
            if (loginType == 1) {
                loginPwd();
            } else {
                loginCode();
            }
        });
        mCountDownTimerUtils = new CountDownTimerUtils(btnCode, context, 60000, 1000);
        btnCode.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etUser.getText().toString())) {
                showTipsDialog("请输入手机号", TipDialog.TYPE_ERROR);
                return;
            }
            sendSMSVerificationCode(etUser.getText().toString());
        });

        //注册
        btnSignUp.setOnClickListener(v -> JumpUtil.startAct(context, RegisterActivity.class));
        //记住密码 监听记住密码多选框按钮事件
        cbMima.setOnCheckedChangeListener((buttonView, isChecked) -> spUtil.put(IS_CHECK, cbMima.isChecked()));
    }

    /**
     * 密码登录
     */
    private void loginPwd() {
        //登录
        if (TextUtils.isEmpty(etUser.getText().toString())) {
            showTipsDialog("请输入手机号", TipDialog.TYPE_ERROR);
            return;
        }
        if (!isMobileNO(etUser.getText().toString())) {
            showTipsDialog("请输入有效的手机号码！", TipDialog.TYPE_ERROR);
            return;
        }
        if (TextUtils.isEmpty(etPwd.getText().toString())) {
            showTipsDialog("请输入密码", TipDialog.TYPE_ERROR);
            return;
        }
        //登录成功和记住密码框为选中状态才保存用户信息
        String userNameValue = etUser.getText().toString();
        String passwordValue = etPwd.getText().toString();
        if (btnSignIn.getProgress() == -1) {
            btnSignIn.setProgress(0);
        }
        userLogin(userNameValue, passwordValue, "1", "", "");
    }

    /**
     * 验证码登录
     */
    private void loginCode() {
        if (TextUtils.isEmpty(etUser.getText().toString())) {
            showTipsDialog("请输入手机号", TipDialog.TYPE_ERROR);
            return;
        }
        if (!isMobileNO(etUser.getText().toString())) {
            showTipsDialog("请输入有效的手机号码！", TipDialog.TYPE_ERROR);
            return;
        }
        if (TextUtils.isEmpty(etCode.getText().toString())) {
            showTipsDialog("请输入短信验证码", TipDialog.TYPE_ERROR);
            return;
        }
        if (TextUtils.isEmpty(taskID)) {
            showTipsDialog("请获取短信验证码", TipDialog.TYPE_ERROR);
            return;
        }
        //登录成功和记住密码框为选中状态才保存用户信息
        String userNameValue = etUser.getText().toString();
        String code = etCode.getText().toString();
        if (btnSignIn.getProgress() == -1) {
            btnSignIn.setProgress(0);
        }
        verifySMSVerificationCode(taskID, userNameValue, code);
    }

    /**
     * @param mobiles 手机号
     * @return 校验手机号
     */
    public boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，
        //"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }


    /**
     * @param mobile           手机号
     * @param password         密码
     * @param loginType        登录类型
     * @param verificationCode 验证码
     *                         用户登录
     */
    private void userLogin(String mobile, String password, String loginType, String verificationCode, String taskID) {
        btnSignIn.setProgress(0);
        new Handler().postDelayed(() -> btnSignIn.setProgress(50), 3000);
        OkHttpUtils.post().url(SERVER_URL + LOGIN_URL).
                addParam("mobile", mobile).
                addParam("password", password).
                addParam("loginType", loginType).
                addParam("verificationCode", verificationCode).
                addParam("taskID", taskID).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                btnSignIn.setProgress(-1);
                showTipsDialog(validateError(e, response), TipDialog.TYPE_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                UserLoginEntity entity = JSON.parseObject(response, UserLoginEntity.class);
                if (entity.isSuccess()) {
                    btnSignIn.setProgress(100);
                    if (cbMima.isChecked()) {
                        //记住用户名、密码、
                        spUtil.put(USER_NAME, mobile);
                        spUtil.put(PASSWORD, password);
                    }
                    spUtil.put(Constant.IS_LOGIN, true);
                    spUtil.put(TOKEN, entity.getData().getToken());
                    spUtil.put(USER_ID, String.valueOf(entity.getData().getUserId()));
                    spUtil.put(EXPIRE_TIME, AppDateUtils.dealDateFormat(entity.getData().getExpireTime()));
                    JumpUtil.startAct(context, MainActivity.class);
                    ActivityManager.getInstance().finishActivitys();
                } else {
                    btnSignIn.setProgress(-1);
                    showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                }
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
        OkHttpUtils.post().url(SERVER_URL + verifySMSVerificationCode_URL).
                addParam("taskID", taskID).
                addParam("mobile", mobile).
                addParam("verificationCode", verificationCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG_Activity, "校验短信验证码：" + response);
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    userLogin(mobile, "", "2", verificationCode, taskID);
                }
            }
        });
    }

    /**
     * 获取权限
     */
    public void getPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,
                READ_PHONE_STATE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        Toast.makeText(context, "未授权", Toast.LENGTH_SHORT).show();
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
