package com.example.obdandroid.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.http.HttpService;
import com.example.obdandroid.http.ResponseCallBack;
import com.example.obdandroid.ui.adapter.ReceivingAddressAdapter;
import com.example.obdandroid.ui.entity.AppUserAddressEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.IosDialog;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Callback;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.deleteAppUserAddress_URL;
import static com.example.obdandroid.config.APIConfig.getAppUserAddressList_URL;

/**
 * 作者：Jealous
 * 日期：2021/9/2 0002
 * 描述： 收货地址管理
 */
public class ReceivingAddressActivity extends BaseActivity {
    private RecyclerView recycleAddress;
    private Context context;
    private ReceivingAddressAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_receiving_address;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        TextView tvAddAddress = findViewById(R.id.tvAddAddress);
        recycleAddress = findViewById(R.id.recycleAddress);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleAddress.setLayoutManager(manager);
        adapter = new ReceivingAddressAdapter(context);
        getAppUserAddressList(getToken(), getUserId());
        adapter.setClickCallBack(new ReceivingAddressAdapter.OnClickCallBack() {
            @Override
            public void update(AppUserAddressEntity.DataEntity.ListEntity entity) {
                Intent intent = new Intent(context, UpdateUserAddressActivity.class);
                intent.putExtra(Constant.ACT_FLAG, entity.getId());
                startActivityForResult(intent, 100);
            }

            @Override
            public void delete(AppUserAddressEntity.DataEntity.ListEntity entity) {
                new IosDialog(context, new IosDialog.DialogClick() {
                    @Override
                    public void Confirm(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            deleteAppUserAddress(entity.getId(), getToken());
                            exitDialog.dismiss();
                        }
                    }

                    @Override
                    public void Cancel(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            exitDialog.dismiss();
                        }
                    }
                }).setTitle("删除提示").setMessage("是否删除该收货地址？").setSelectNegative("取消").setSelectPositive("确定").showDialog();
            }

            @Override
            public void setDefault(AppUserAddressEntity.DataEntity.ListEntity entity, int position) {
                new IosDialog(context, new IosDialog.DialogClick() {
                    @Override
                    public void Confirm(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            adapter.getMap().put(position, !adapter.getMap().get(position));
                            //刷新适配器
                            adapter.notifyDataSetChanged();
                            //单选
                            adapter.singlesel(position);
                            setDefaultAddress(entity.getId(), getToken(), getUserId());
                            exitDialog.dismiss();
                        }
                    }

                    @Override
                    public void Cancel(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            exitDialog.dismiss();
                        }
                    }
                }).setTitle("设置提示").setMessage("是否设置改地址为默认地址？").setSelectNegative("取消").setSelectPositive("确定").showDialog();
            }
        });
        tvAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddAddressActivity.class);
            startActivityForResult(intent, 100);
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
     * @param id    地址id
     * @param token 接口令牌
     *              设置默认地址
     */
    private void setDefaultAddress(String id, String token, String appUserId) {
        HttpService.getInstance().
                setDefaultAddress(token, appUserId, id, true).
                enqueue(new ResponseCallBack(new ResponseCallBack.CallBack() {
                    @Override
                    public void onSuccess(String response) {
                        ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                        if (entity.isSuccess()) {
                            new CustomeDialog(context, entity.getMessage(), confirm -> {
                                if (confirm) {
                                    getAppUserAddressList(getToken(), getUserId());
                                }
                            }).setTitle("提示").setPositiveButton("知道了").show();
                        }
                    }

                    @Override
                    public void onFail(Throwable t) {

                    }
                }));
    }

    /**
     * @param id    地址id
     * @param token 接口令牌
     *              删除收货地址
     */
    private void deleteAppUserAddress(String id, String token) {
        OkHttpUtils.get().url(SERVER_URL + deleteAppUserAddress_URL).
                addParam("id", id).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    new CustomeDialog(context, entity.getMessage(), confirm -> {
                        if (confirm) {
                            getAppUserAddressList(getToken(), getUserId());
                        }
                    }).setPositiveButton("知道了").setTitle("删除提示").show();

                }
            }
        });
    }

    /**
     * @param token     接口令牌
     * @param appUserId 用户id
     *                  获取收货地址
     */
    private void getAppUserAddressList(String token, String appUserId) {
        OkHttpUtils.get().url(SERVER_URL + getAppUserAddressList_URL).
                addParam("token", token).
                addParam("appUserId", appUserId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                AppUserAddressEntity entity = JSON.parseObject(response, AppUserAddressEntity.class);
                if (entity.isSuccess()) {
                    adapter.setList(entity.getData().getList());
                    recycleAddress.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 101) {
                getAppUserAddressList(getToken(), getUserId());
            }
        }
    }
}