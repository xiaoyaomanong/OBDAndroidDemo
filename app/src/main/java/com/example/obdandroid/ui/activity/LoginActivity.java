package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.listener.OnSwipeTouchListener;
import com.example.obdandroid.utils.ActivityManager;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.kongzue.dialog.v2.TipDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
        //登录
        btnSignIn.setOnClickListener(v -> {
            if (!etUser.getText().toString().trim().contains("1")) {
                showTipsDialog("账号错误", TipDialog.TYPE_ERROR);
                return;
            }
            if (!etPwd.getText().toString().trim().contains("1")) {
                showTipsDialog("密码错误", TipDialog.TYPE_ERROR);
                return;
            }
            spUtil.put(Constant.ISLOGIN, true);
            JumpUtil.startAct(context, MainActivity.class);
            ActivityManager.getInstance().finishActivitys();
        });
        //注册
        btnSignUp.setOnClickListener(v -> JumpUtil.startAct(context, RegisterActivity.class));
    }

    /**
     * 获取权限
     */
    public void getPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(CAMERA, READ_EXTERNAL_STORAGE,  WRITE_EXTERNAL_STORAGE,
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
