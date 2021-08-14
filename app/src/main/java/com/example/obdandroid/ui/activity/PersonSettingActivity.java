package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.entity.UserInfoEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.BitMapUtils;
import com.example.obdandroid.utils.DialogUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.USER_INFO_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/28 0028
 * 描述：个人设置
 */
public class PersonSettingActivity extends BaseActivity {
    private CircleImageView imageHeader;
    private TextView tvNick;
    private TextView tvPhone;
    private TextView tvSex;
    private TextView tvValidityDate;
    private TextView tvVIP;
    private TextView tvIsTheDeviceBound;
    private DialogUtils dialogUtils;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_person_setting;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        imageHeader = findViewById(R.id.imageHeader);
        tvNick = findViewById(R.id.tvNick);
        tvPhone = findViewById(R.id.tvPhone);
        tvSex = findViewById(R.id.tvSex);
        tvValidityDate = findViewById(R.id.tvValidityDate);
        tvVIP = findViewById(R.id.tvVIP);
        tvIsTheDeviceBound = findViewById(R.id.tvIsTheDeviceBound);
        dialogUtils = new DialogUtils(this);
        getUserInfo(getUserId(), getToken());
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

    @SuppressLint("SetTextI18n")
    private void setView(UserInfoEntity.DataEntity entity) {
        if (entity.getHeadPortrait().length() > 0) {
            imageHeader.setImageBitmap(BitMapUtils.stringToBitmap(entity.getHeadPortrait()));
        }
        tvNick.setText(entity.getNickname());
        tvPhone.setText(entity.getPhoneNum());
        tvSex.setText("未知");
        if (!TextUtils.isEmpty(entity.getEndValidity())) {
            tvValidityDate.setText(entity.getEndValidity().split(" ")[0]);
        }
        tvVIP.setText(entity.getIsVip() == 1 ? "是" : "否");
        tvIsTheDeviceBound.setText(entity.isTheDeviceBound() ? "是" : "否");
    }

    /**
     * @param userId 用户id
     * @param token  接口令牌
     *               用户信息
     */
    private void getUserInfo(String userId, String token) {
        dialogUtils.showProgressDialog();
        OkHttpUtils.get().url(SERVER_URL + USER_INFO_URL).
                addParam("userId", userId).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                UserInfoEntity entity = JSON.parseObject(response, UserInfoEntity.class);
                if (entity.isSuccess()) {
                    dialogUtils.dismiss();
                    setView(entity.getData());
                } else {
                    dialogUtils.dismiss();
                }
            }
        });
    }

}