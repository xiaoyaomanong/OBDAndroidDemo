package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.entity.FaultCodeDetailsEntity;
import com.example.obdandroid.utils.DialogUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.FAULT_CODE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;

/**
 * 作者：Jealous
 * 日期：2021/2/2 0002
 * 描述：
 */
public class TroubleCodeQueryDetailsActivity extends BaseActivity {
    private TextView tvFaultCode;
    private TextView tvBelongingSystem;
    private TextView tvScopeOfApplication;
    private TextView tvChineseMeaning;
    private TextView tvEnglishMeaning;
    private TextView tvCauseOfFailure;
    private DialogUtils dialogUtils;
    private String faultCode;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_query_trouble_details;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        Context context = this;
        faultCode = getIntent().getStringExtra(Constant.ACT_FLAG);
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        tvFaultCode = findViewById(R.id.tvFaultCode);
        tvBelongingSystem = findViewById(R.id.tvBelongingSystem);
        tvScopeOfApplication = findViewById(R.id.tvScopeOfApplication);
        tvChineseMeaning = findViewById(R.id.tvChineseMeaning);
        tvEnglishMeaning = findViewById(R.id.tvEnglishMeaning);
        tvCauseOfFailure = findViewById(R.id.tvCauseOfFailure);
        dialogUtils = new DialogUtils(context);
        getFaultCodeDetails(faultCode, getToken());
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

    private void setView(List<FaultCodeDetailsEntity.DataEntity> list) {
        if (list.size() != 0) {
            tvFaultCode.setText(TextUtils.isEmpty(list.get(0).getFaultCode()) ? "未知" : list.get(0).getFaultCode());
            tvBelongingSystem.setText(TextUtils.isEmpty(list.get(0).getBelongingSystem()) ? "未知" : list.get(0).getBelongingSystem());
            tvScopeOfApplication.setText(TextUtils.isEmpty(list.get(0).getScopeOfApplication()) ? "未知" : list.get(0).getScopeOfApplication());
            tvChineseMeaning.setText(TextUtils.isEmpty(list.get(0).getChineseMeaning()) ? "未知" : list.get(0).getChineseMeaning());
            tvEnglishMeaning.setText(TextUtils.isEmpty(list.get(0).getEnglishMeaning()) ? "未知" : list.get(0).getEnglishMeaning());
            tvCauseOfFailure.setText(TextUtils.isEmpty(list.get(0).getCauseOfFailure()) ? "未知" : list.get(0).getCauseOfFailure());
        }
    }

    /**
     * @param faultCode OBD汽车故障码
     * @param token     接口令牌
     *                  查询故障码
     */
    private void getFaultCodeDetails(String faultCode, String token) {
        dialogUtils.showProgressDialog();
        OkHttpUtils.get().url(SERVER_URL + FAULT_CODE_URL).
                addParam("token", token).
                addParam("faultCode", faultCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response, int id) {
                FaultCodeDetailsEntity entity = JSON.parseObject(response, FaultCodeDetailsEntity.class);
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