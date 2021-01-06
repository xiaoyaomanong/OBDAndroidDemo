package com.example.obdandroid.ui.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.R;


/**
 * 作者：Jealous
 * 日期：2017/11/8 0008 09:36
 */

public class PhotoDialog extends Dialog implements View.OnClickListener {
    private TextView titleTxt;
    private TextView album;
    private TextView camera;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String title;

    public PhotoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public PhotoDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public PhotoDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected PhotoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public PhotoDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    public PhotoDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        titleTxt = findViewById(R.id.title);
        album = findViewById(R.id.album);
        camera = findViewById(R.id.camera);
        album.setOnClickListener(this);
        camera.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.album:
                if (listener != null) {
                    listener.onClickAlbum(this, true);
                }
                break;
            case R.id.camera:
                if (listener != null) {
                    listener.onClickCamera(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener {
        void onClickAlbum(Dialog dialog, boolean confirm);

        void onClickCamera(Dialog dialog, boolean confirm);
    }
}
