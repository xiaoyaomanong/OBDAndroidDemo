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
import com.example.obdandroid.ui.adapter.CarModelAdapter;
import com.example.obdandroid.ui.entity.CarModelEntity;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getCarModelList_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/11 0011
 * 描述：
 */
public class CarModelActivity extends BaseActivity {
    private Context context;
    private RecyclerView recycleCarModel;
    private CarModelAdapter adapter;
    private String automobileBrandId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_car_model;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        automobileBrandId = getIntent().getStringExtra("automobileBrandId");
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        recycleCarModel = findViewById(R.id.recycle_carModel);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleCarModel.setLayoutManager(manager);
        adapter = new CarModelAdapter(context);
        getCarModelList(getToken(), automobileBrandId);//"1347111731436064772"
        adapter.setClickCallBack(entity -> {
            Intent intent = new Intent();
            intent.putExtra("model", entity);
            setResult(100, intent);
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

    /**
     * @param token             用户token
     * @param automobileBrandId 车辆品牌ID
     *                          获取品牌型号
     */
    private void getCarModelList(String token, String automobileBrandId) {
        OkHttpUtils.get().url(SERVER_URL + getCarModelList_URL).
                addParam("token", token).
                addParam("automobileBrandId", automobileBrandId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                CarModelEntity entity = JSON.parseObject(response, CarModelEntity.class);
                if (entity.isSuccess()) {
                    adapter.setList(entity.getData());
                    recycleCarModel.setAdapter(adapter);
                } else {
                    dialogError(context, entity.getMessage());
                }
            }
        });
    }

}