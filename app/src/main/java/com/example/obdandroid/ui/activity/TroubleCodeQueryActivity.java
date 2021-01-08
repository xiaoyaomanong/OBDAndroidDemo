package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.view.View;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.FAULT_CODE_URL;
import static com.example.obdandroid.config.APIConfig.SERVER_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/8 0008
 * 描述：
 */
public class TroubleCodeQueryActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBarSet;
    private PullLoadMoreRecyclerView recycleCar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_vehicle;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        titleBarSet = findViewById(R.id.titleBarSet);
        recycleCar = findViewById(R.id.recycle_Car);
        recycleCar.setLinearLayout();
        //设置是否可以下拉刷新
        recycleCar.setPullRefreshEnable(true);
        //设置是否可以上拉刷新
        recycleCar.setPushRefreshEnable(true);
        //显示下拉刷新
        recycleCar.setRefreshing(true);
        //设置上拉刷新文字
        recycleCar.setFooterViewText(getString(R.string.loading));
        //设置上拉刷新文字颜色
        recycleCar.setFooterViewTextColor(R.color.teal_200);
        getVehiclePageList("P0000", getToken());
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
     * @param faultCode OBD汽车故障码
     * @param token     接口令牌
     *                  查询故障码
     */
    private void getVehiclePageList(String faultCode, String token) {
        OkHttpUtils.get().
                url(SERVER_URL + FAULT_CODE_URL).
                addParam("token", token).
                addParam("faultCode", faultCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogE("查询故障码:" + response);
            }
        });
    }

}