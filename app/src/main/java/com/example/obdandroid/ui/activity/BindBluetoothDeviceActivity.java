package com.example.obdandroid.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.BindBluetoothDeviceAdapter;
import com.example.obdandroid.ui.entity.BluetoothDeviceEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.IosDialog;
import com.example.obdandroid.utils.ToastUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.bindingVehicle_URL;
import static com.example.obdandroid.config.Constant.REQUEST_ENABLE_BT;

/**
 * 作者：Jealous
 * 日期：2021/1/13 0013
 * 描述：
 */
public class BindBluetoothDeviceActivity extends BaseActivity {
    private RecyclerView recycleBluetoothDevice;
    private BindBluetoothDeviceAdapter simpleAdapter;
    private Context context;
    private String vehicleId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bluetooth_device;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        vehicleId = getIntent().getStringExtra("vehicleId");
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        recycleBluetoothDevice = findViewById(R.id.recycle_BluetoothDevice);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleBluetoothDevice.setLayoutManager(manager);
        simpleAdapter = new BindBluetoothDeviceAdapter(context);
        checkBluetooth();
        initBlueTooth();
        simpleAdapter.setClickCallBack(device ->
                new IosDialog(context, new IosDialog.DialogClick() {
                    @Override
                    public void Confirm(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            bindingVehicle(getToken(), getUserId(), vehicleId, device.getBlue_address());
                            exitDialog.dismiss();
                        }
                    }

                    @Override
                    public void Cancel(AlertDialog exitDialog, boolean confirm) {
                        if (confirm) {
                            exitDialog.dismiss();
                        }
                    }
                }).setSelectPositive("绑定").
                        setSelectNegative("取消").
                        setMessage("是否绑定" + device.getBlue_name() + "蓝牙设备").
                        setTitle("绑定蓝牙").
                        showDialog());
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
     * @param token                 用户token
     * @param userId                用户ID
     * @param vehicleId             车辆ID
     * @param bluetoothDeviceNumber 蓝牙设备码
     *                              蓝牙设备绑定车辆
     */
    private void bindingVehicle(String token, String userId, String vehicleId, String bluetoothDeviceNumber) {
        LogE("token："+token);
        LogE("userId："+userId);
        LogE("vehicleId："+vehicleId);
        LogE("bluetoothDeviceNumber："+bluetoothDeviceNumber);
        OkHttpUtils.get().url(SERVER_URL + bindingVehicle_URL).
                addParam("token", token).
                addParam("userId", userId).
                addParam("vehicleId", vehicleId).
                addParam("bluetoothDeviceNumber", bluetoothDeviceNumber).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    new CustomeDialog(context, "绑定成功！", confirm -> {
                        if (confirm) {
                            setResult(101, new Intent());
                            finish();
                        }
                    }).setPositiveButton("确定").setTitle("绑定提示").show();
                } else {
                    showTipsDialog("绑定失败!", TipDialog.TYPE_ERROR);
                }
            }
        });
    }

    /**
     * @param msg  提示信息
     * @param type 提示类型
     *             提示
     */
    private void showTipsDialog(String msg, int type) {
        TipDialog.show(context, msg, TipDialog.SHOW_TIME_SHORT, type);
    }

    /**
     * 初始化蓝牙
     */
    private void initBlueTooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                adapter.enable();
                //睡一秒钟，避免不发现
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            List<BluetoothDeviceEntity> blueList = new ArrayList<>();
            for (BluetoothDevice bluetoothDevice : devices) {
                LogE("Address:" + bluetoothDevice.getAddress());
                BluetoothDeviceEntity entity = new BluetoothDeviceEntity();
                entity.setBlue_address(bluetoothDevice.getAddress());
                entity.setBlue_name(bluetoothDevice.getName());
                blueList.add(entity);
            }
            simpleAdapter.setList(blueList);
            recycleBluetoothDevice.setAdapter(simpleAdapter);
        } else {
            ToastUtil.shortShow("本机没有蓝牙设备");
        }
    }

    /**
     * 检测蓝牙是否打开
     */
    private void checkBluetooth() {
        BluetoothAdapter bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
        //如果BT未开启，请请求将其启用。
        if (bluetoothadapter != null) {
            /*
             * 记住最初的蓝牙状态
             * 蓝牙适配器的初始状态
             */
            boolean initialBtStateEnabled = bluetoothadapter.isEnabled();
            if (!initialBtStateEnabled) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                showToast("蓝牙已打开");
            }
        }
    }
}