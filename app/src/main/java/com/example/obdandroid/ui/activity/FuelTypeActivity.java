package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.FuelTypeAdapter;
import com.example.obdandroid.ui.entity.VocationalDictDataListEntity;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getVocationalDictDataListByType_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/11 0011
 * 描述：
 */
public class FuelTypeActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBarSet;
    private RecyclerView recycleFuelType;
    private FuelTypeAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fuel_type;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        titleBarSet = findViewById(R.id.titleBarSet);
        recycleFuelType = findViewById(R.id.recycle_fuelType);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleFuelType.setLayoutManager(manager);
        adapter = new FuelTypeAdapter(context);
        getVocationalDictDataListByType(getToken(), "燃油类型", "fuel_type");
        adapter.setClickCallBack(entity -> {
            Intent intent = new Intent();
            intent.putExtra("fuelType", entity);
            setResult(99, intent);
            finish();
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

    private void getVocationalDictDataListByType(String token, String name, String code) {
        OkHttpUtils.get().url(SERVER_URL + getVocationalDictDataListByType_URL).
                addParam("token", token).
                addParam("name", name).
                addParam("code", code).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                VocationalDictDataListEntity entity = JSON.parseObject(response, VocationalDictDataListEntity.class);
                if (entity.isSuccess()) {
                    adapter.setList(entity.getData());
                    recycleFuelType.setAdapter(adapter);
                }else {
                    dialogError(context,entity.getMessage());
                }
            }
        });
    }
}