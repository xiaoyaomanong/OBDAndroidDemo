package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.tu.loadingdialog.LoadingDialog;
import com.example.obdandroid.R;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.listener.OnSwipeTouchListener;
import com.example.obdandroid.ui.entity.LoginParam;
import com.example.obdandroid.ui.entity.UserLoginEntity;
import com.example.obdandroid.ui.view.progressButton.CircularProgressButton;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.AppDateUtils;
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
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.example.obdandroid.config.APIConfig.LOGIN_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
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
public class LoginActivity extends AppCompatActivity {
    private Context context;
    private ImageView imageView;
    private TextView textView;
    private int count = 0;
    private EditText etUser;
    private EditText etPwd;
    private TextView btnSignUp;
    private CircularProgressButton btnSignIn;
    private TextView tvForget;
    private AppCompatCheckBox cbMima;
    private SPUtil spUtil;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initView();
        spUtil = new SPUtil(context);
        imageView.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
            }

            @SuppressLint("SetTextI18n")
            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            @SuppressLint("SetTextI18n")
            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeBottom() {

            }
        });
        getPermission();
        //判断记住密码多选框的状态
        if (spUtil.getBoolean(IS_CHECK, false)) {
            //设置默认是记录密码状态
            cbMima.setChecked(true);
            String UserName = spUtil.getString(USER_NAME, "");
            String Pwd = spUtil.getString(PASSWORD, "");
            etUser.setText(UserName);
            etPwd.setText(Pwd);
        }


        //登录
        btnSignIn.setIndeterminateProgressMode(true);
        btnSignIn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etUser.getText().toString().trim())) {
                showTipsDialog("请输入手机号", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                showTipsDialog("请输入密码", TipDialog.TYPE_ERROR);
                return;
            }
            //登录成功和记住密码框为选中状态才保存用户信息
            String userNameValue = etUser.getText().toString();
            String passwordValue = etPwd.getText().toString();
            if (btnSignIn.getProgress() == -1) {
                btnSignIn.setProgress(0);
            }
            userLogin(userNameValue, passwordValue);
        });
        //注册
        btnSignUp.setOnClickListener(v -> JumpUtil.startAct(context, RegisterActivity.class));

        //记住密码
        //监听记住密码多选框按钮事件
        cbMima.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spUtil.put(IS_CHECK, cbMima.isChecked());
            }
        });

    }


    /**
     * @param mobile   手机号
     * @param password 密码
     *                 用户登录
     */
    private void userLogin(String mobile, String password) {
        btnSignIn.setProgress(0);
        new Handler().postDelayed(() -> btnSignIn.setProgress(50), 1000);
        OkHttpUtils.post().url(SERVER_URL + LOGIN_URL).
                addParam("mobile", mobile).
                addParam("password", password).build().
                execute(new StringCallback() {
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

    /**
     * 初始化控件
     */
    private void initView() {
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        etUser = findViewById(R.id.etUser);
        etPwd = findViewById(R.id.etPwd);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForget = findViewById(R.id.tvForget);
        cbMima = findViewById(R.id.cb_mima);
    }
}
