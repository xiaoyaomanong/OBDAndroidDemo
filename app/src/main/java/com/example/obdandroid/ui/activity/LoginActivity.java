package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import static com.example.obdandroid.config.APIConfig.LOGIN_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.Constant.EXPIRETIME;
import static com.example.obdandroid.config.Constant.TOKEN;
import static com.example.obdandroid.config.Constant.USERID;
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
    private Button btnSignUp;
    private Button btnSignIn;
    private TextView tvForget;
    private SPUtil spUtil;
    private LoadingDialog dialog;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        LoadingDialog.Builder builder1 = new LoadingDialog.Builder(context)
                .setMessage("登录中...")
                .setCancelable(false)
                .setCancelOutside(false)
                .setShowMessage(true);
        dialog = builder1.create();
        //登录
        btnSignIn.setOnClickListener(v -> {
          /*  if (TextUtils.isEmpty(etUser.getText().toString().trim())) {
                showTipsDialog("请输入手机号", TipDialog.TYPE_ERROR);
                return;
            }
            if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                showTipsDialog("请输入密码", TipDialog.TYPE_ERROR);
                return;
            }
            userLogin(etUser.getText().toString(), etPwd.getText().toString());*/
            JumpUtil.startAct(context, MainActivity.class);
            ActivityManager.getInstance().finishActivitys();
        });
        //注册
        btnSignUp.setOnClickListener(v -> JumpUtil.startAct(context, RegisterActivity.class));
    }

    /**
     * @return 生成参数
     */
    private String generateParam() {
        LoginParam param = new LoginParam();
        param.setMobile(etUser.getText().toString());
        param.setPassword(etPwd.getText().toString());
        return JSON.toJSONString(param);
    }

    /**
     * @param mobile   手机号
     * @param password 密码
     *                 用户登录
     */
    private void userLogin(String mobile, String password) {
        dialog.show();
        OkHttpUtils.post().
                url(SERVER_URL + LOGIN_URL).
                addParam("mobile", mobile).
                addParam("password", password).build().
                execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {
                        handler.postDelayed(() -> dialog.dismiss(), 2000);
                        showTipsDialog(validateError(e, response), TipDialog.TYPE_ERROR);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG_Activity, "用户登录:" + response);
                        UserLoginEntity entity = JSON.parseObject(response, UserLoginEntity.class);
                        if (entity.isSuccess()) {
                            handler.postDelayed(() -> dialog.dismiss(), 2000);
                            Log.e(TAG_Activity, "过期时间:" + AppDateUtils.dealDateFormat(entity.getData().getExpireTime()));
                            spUtil.put(Constant.ISLOGIN, true);
                            handler.postDelayed(() -> {
                                spUtil.put(TOKEN, entity.getData().getToken());
                                spUtil.put(USERID, String.valueOf(entity.getData().getUserId()));
                                spUtil.put(EXPIRETIME, AppDateUtils.dealDateFormat(entity.getData().getExpireTime()));
                                JumpUtil.startAct(context, MainActivity.class);
                                ActivityManager.getInstance().finishActivitys();
                            }, 2000);
                        } else {
                            handler.postDelayed(() -> {
                                dialog.dismiss();
                                showTipsDialog(entity.getMessage(), TipDialog.TYPE_ERROR);
                            }, 1000);
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
    }
}
