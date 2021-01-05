package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.http.OkHttpClientUtils;
import com.example.obdandroid.listener.OnSwipeTouchListener;
import com.example.obdandroid.ui.entity.RegisterParam;
import com.example.obdandroid.ui.view.CircleImageView;

import static com.example.obdandroid.config.APIConfig.REGISTER_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.TAG.TAG_Activity;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class RegisterActivity extends AppCompatActivity {
    private Context context;
    private ImageView imageView;
    private TextView textView;
    int count = 0;
    private LinearLayout linearLayout;
    private EditText etUser;
    private EditText etPwd;
    private Button btnSignUp;
    private CircleImageView myHeaderImage;
    private EditText etNick;
    private EditText etCode;
    private Button btnCode;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        initView();
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
        //注册
        btnSignUp.setOnClickListener(v -> registerUser(generateParam()));
    }

    /**
     * 初始化控件
     */
    private void initView() {
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        linearLayout = findViewById(R.id.linearLayout);
        etUser = findViewById(R.id.etUser);
        etPwd = findViewById(R.id.etPwd);
        btnSignUp = findViewById(R.id.btnSignUp);
        myHeaderImage = findViewById(R.id.my_header_image);
        etNick = findViewById(R.id.etNick);
        etCode = findViewById(R.id.etCode);
        btnCode = findViewById(R.id.btn_code);
    }

    private String generateParam() {
        RegisterParam param = new RegisterParam();
        param.setHeadPortrait("");
        param.setMobile("15101307774");
        param.setNickname("蛋疼");
        param.setPassword("123");
        param.setVerificationCode("666666");
        param.setRegistrationPlatform("Android");
        return JSON.toJSONString(param);
    }

    /**
     * @param param 注册内容
     *              用户注册
     */
    private void registerUser(String param) {
        OkHttpClientUtils.submitFormBody(SERVER_URL + REGISTER_URL,
                param, new OkHttpClientUtils.UPLoadIMGCallBack() {
                    @Override
                    public void OnSuccess(String response) {
                        Log.e(TAG_Activity, "用户注册:" + response);

                    }

                    @Override
                    public void OnFail(String error) {

                    }
                });
    }

}
