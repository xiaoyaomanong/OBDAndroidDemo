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

import com.example.obdandroid.R;
import com.example.obdandroid.listener.OnSwipeTouchListener;

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
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        linearLayout = findViewById(R.id.linearLayout);
        etUser = findViewById(R.id.etUser);
        etPwd = findViewById(R.id.etPwd);
        btnSignUp = findViewById(R.id.btnSignUp);
    }
}
