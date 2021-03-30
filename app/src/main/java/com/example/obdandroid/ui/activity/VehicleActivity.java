package com.example.obdandroid.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.MyVehicleAdapter;
import com.example.obdandroid.ui.entity.AutomobileBrandEntity;
import com.example.obdandroid.ui.entity.BrandPinYinEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.VehicleEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.IosDialog;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zx.uploadlibrary.utils.OKHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.Vehicle_URL;
import static com.example.obdandroid.config.APIConfig.deleteVehicle_URL;

/**
 * 作者：Jealous
 * 日期：2021/1/31
 * 描述：
 */
public class VehicleActivity extends BaseActivity {
    private Context context;
    private PullLoadMoreRecyclerView recycleCar;
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isLoadMore;
    private final List<VehicleEntity.DataEntity.ListEntity> datas = new ArrayList<>();
    private MyVehicleAdapter adapter;
    private SPUtil spUtil;
    private LocalBroadcastManager mLocalBroadcastManager; //创建本地广播管理器类变量
    private AutomobileBrandEntity.DataEntity dataEntity;
    private BrandPinYinEntity yinEntity;
    private DialogUtils dialogUtils;
    private String CarId;

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
        context = this;
        dataEntity = (AutomobileBrandEntity.DataEntity) getIntent().getSerializableExtra("data");
        yinEntity = (BrandPinYinEntity) getIntent().getSerializableExtra("dataA");
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        recycleCar = findViewById(R.id.recycle_Car);
        spUtil = new SPUtil(context);
        dialogUtils = new DialogUtils(context);
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
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);                   //广播变量管理器获
        adapter = new MyVehicleAdapter(context);
        getVehiclePageList(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), getUserId(), true);
        recycleCar.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getVehiclePageList(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), getUserId(), true);
            }

            @Override
            public void onLoadMore() {
                if (isLoadMore) {
                    pageNum++;
                    getVehiclePageList(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), getUserId(), false);
                } else {
                    //设置是否可以上拉刷新
                    recycleCar.setPullLoadMoreCompleted();
                }
            }
        });
        adapter.setClickCallBack(new MyVehicleAdapter.OnClickCallBack() {
            @Override
            public void click(VehicleEntity.DataEntity.ListEntity entity) {
                JumpUtil.startActToData(context, VehicleInfoActivity.class, String.valueOf(entity.getVehicleId()), 0);
            }

            @Override
            public void select(VehicleEntity.DataEntity.ListEntity entity, int position) {
                new CustomeDialog(context, "是否选择" + entity.getAutomobileBrandName() + "该车辆", confirm -> {
                    if (confirm) {
                        adapter.getMap().put(position, !adapter.getMap().get(position));
                        //刷新适配器
                        adapter.notifyDataSetChanged();
                        //单选
                        adapter.singlesel(position);
                        spUtil.remove("vehicleId");
                        spUtil.put("vehicleId", String.valueOf(entity.getVehicleId()));
                        CarId = String.valueOf(entity.getVehicleId());
                        Intent intent = new Intent("com.android.ObdCar");//创建发送广播的Action
                        intent.putExtra("vehicleId", String.valueOf(entity.getVehicleId()));//发送携带的数据
                        mLocalBroadcastManager.sendBroadcast(intent);                               //发送本地广播
                        finish();
                    }
                }).setPositiveButton("确定").setTitle("选择默认车辆").show();
            }

            @Override
            public void delete(VehicleEntity.DataEntity.ListEntity entity) {
                new IosDialog(context, new IosDialog.DialogClick() {
                    @Override
                    public void Confirm(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            if (String.valueOf(entity.getVehicleId()).equals(spUtil.getString("vehicleId", ""))) {
                                showToast("该车辆已被选中为默认车辆,暂时无法删除");
                            } else {
                                deleteVehicle(getToken(), getUserId(), String.valueOf(entity.getVehicleId()));
                            }
                            exitDialog.dismiss();
                        }
                    }

                    @Override
                    public void Cancel(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            exitDialog.dismiss();
                        }
                    }
                }).
                        setMessage("是否删除+" + entity.getAutomobileBrandName() + "该车辆").
                        setSelectNegative("取消").setSelectPositive("确定").
                        setTitle("删除车辆提示").showDialog();
            }
        });
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                if (TextUtils.isEmpty(spUtil.getString("vehicleId", ""))) {
                    showTipDialog("您还未选择检测车辆,请选择一辆车");
                }else {
                    finish();
                }
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                Intent intent = new Intent(context, AddVehicleActivity.class);
                intent.putExtra("data", dataEntity);
                intent.putExtra("dataA", yinEntity);
                startActivityForResult(intent, 11);
            }
        });
    }


    /**
     * @param pageNum   页号
     * @param pageSize  条数
     * @param token     接口令牌
     * @param isRefresh 是否刷新
     *                  获取用户车辆列表
     */
    private void getVehiclePageList(String pageNum, String pageSize, String token, String appUserId, final boolean isRefresh) {
        OkHttpUtils.get().
                url(SERVER_URL + Vehicle_URL).
                addParam("pageNum", pageNum).
                addParam("pageSize", pageSize).
                addParam("token", token).
                addParam("appUserId", appUserId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                VehicleEntity entity = JSON.parseObject(response, VehicleEntity.class);
                if (entity.isSuccess()) {
                    isLoadMore = Integer.parseInt(pageSize) <= entity.getData().getPages();
                    if (isRefresh) {
                        adapter.setList(entity.getData().getList());
                        recycleCar.setAdapter(adapter);
                        // 刷新完成后调用，必须在UI线程中
                        recycleCar.setPullLoadMoreCompleted();
                    } else {
                        new Handler().postDelayed(() -> getActivity().runOnUiThread(() -> {
                            datas.clear();
                            datas.addAll(entity.getData().getList());
                            adapter.addFootItem(datas);
                            // 加载更多完成后调用，必须在UI线程中
                            recycleCar.setPullLoadMoreCompleted();
                        }), 1000);
                    }
                } else {
                    dialogError(context, entity.getMessage());
                }
            }
        });
    }


    private void deleteVehicle(String token, String userId, String vehicleId) {
        dialogUtils.showProgressDialog("正在删除车辆...");
        OkHttpUtils.get().url(SERVER_URL + deleteVehicle_URL).
                addParam("token", token).
                addParam("userId", userId).
                addParam("vehicleId", vehicleId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    dialogUtils.dismiss();
                    new CustomeDialog(context, "删除成功", confirm -> {
                        if (confirm) {
                            pageNum = 1;
                            pageSize = 10;
                            getVehiclePageList(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), getUserId(), true);
                        }
                    }).setTitle("删除车辆").setPositiveButton("确定").show();
                } else {
                    dialogUtils.dismiss();
                    showToast(entity.getMessage());
                }

            }
        });

    }

    @Override
    //安卓重写返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TextUtils.isEmpty(spUtil.getString("vehicleId", ""))) {
                showTipDialog("您还未选择检测车辆,请选择一辆车");
            }else {
                finish();
            }
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (resultCode == 10) {
                pageNum = 1;
                pageSize = 10;
                getVehiclePageList(String.valueOf(pageNum), String.valueOf(pageSize), getToken(), getUserId(), true);
            }
        }
    }
}