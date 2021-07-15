package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.ui.activity.BindBluetoothDeviceActivity;
import com.example.obdandroid.ui.activity.CheckRecordActivity;
import com.example.obdandroid.ui.activity.CheckRecordDetailsActivity;
import com.example.obdandroid.ui.activity.MyVehicleActivity;
import com.example.obdandroid.ui.activity.MyVehicleDashActivity;
import com.example.obdandroid.ui.activity.SelectAutomobileBrandActivity;
import com.example.obdandroid.ui.activity.VehicleActivity;
import com.example.obdandroid.ui.activity.VehicleCheckActivity;
import com.example.obdandroid.ui.activity.VehicleInfoActivity;
import com.example.obdandroid.ui.adapter.HomeAdapter;
import com.example.obdandroid.ui.adapter.TestRecordAdapter;
import com.example.obdandroid.ui.entity.AutomobileBrandEntity;
import com.example.obdandroid.ui.entity.BluetoothDeviceEntity;
import com.example.obdandroid.ui.entity.TestRecordEntity;
import com.example.obdandroid.ui.entity.UserCurrentRechargeEntity;
import com.example.obdandroid.ui.entity.UserInfoEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.utils.BltManagerUtils;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.USER_INFO_URL;
import static com.example.obdandroid.config.APIConfig.getCommonBrandList_URL;
import static com.example.obdandroid.config.APIConfig.getTestRecordPageList_URL;
import static com.example.obdandroid.config.APIConfig.getTheUserCurrentRecharge_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;
import static com.example.obdandroid.config.Constant.REQUEST_ENABLE_BT;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023  y
 * 描述：
 */
public class HomeFragment extends BaseFragment {
    private Context context;
    private TitleBar titleBar;
    private List<BluetoothDeviceEntity> blueList;
    private int yourChoice;
    private RecyclerView recycleCar;
    private RecyclerView recycleContent;
    private LinearLayout layoutCar;
    private ImageView ivCarLogo;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private LinearLayout layoutOBD;
    private LinearLayout layoutAddCar;
    private LinearLayout layoutMoreTest;
    private TextView tvCheckTime;
    private TextView tvObd;
    private TextView tvHomeObdTip;
    private static String mConnectedDeviceName = null;
    private static String mConnectedDeviceAddress = null;
    private TestRecordAdapter recordAdapter;
    private TestReceiver testReceiver;
    private DialogUtils dialogUtils;
    private boolean isConnected = false;
    private boolean isConn = false;
    private String deviceAddress;
    private HomeAdapter homeAdapter;
    private boolean isVip;
    private LocalBroadcastManager mLocalBroadcastManager; //创建本地广播管理器类变量
    private static final int COMPLETES = 1;
    private static final int COMPLETET = 2;
    private SPUtil spUtil;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == COMPLETES) {
                onConnect();
            }
            if (msg.what == COMPLETET) {
                onDisconnect();
            }
        }
    };


    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view, Bundle savedInstanceState) {
        context = getHoldingActivity();
        titleBar = getView(R.id.titleBar);
        recycleCar = getView(R.id.recycleCar);
        recycleContent = getView(R.id.recycleContent);
        layoutCar = getView(R.id.layoutCar);
        ivCarLogo = getView(R.id.ivCarLogo);
        tvAutomobileBrandName = getView(R.id.tvAutomobileBrandName);
        tvModelName = getView(R.id.tvModelName);
        layoutOBD = getView(R.id.layoutOBD);
        tvObd = getView(R.id.tv_obd);
        layoutAddCar = getView(R.id.layoutAddCar);
        tvHomeObdTip = getView(R.id.tv_home_obd_tip);
        LinearLayout layoutMoreDash = getView(R.id.layoutMoreDash);
        layoutMoreTest = getView(R.id.layoutMoreTest);
        LinearLayout layoutCheck = getView(R.id.layoutCheck);
        tvCheckTime = getView(R.id.tvCheckTime);
        titleBar.setTitle("汽车扫描");
        spUtil = new SPUtil(context);
        dialogUtils = new DialogUtils(context);
        mConnectedDeviceName = ObdPreferences.get(context).getBlueToothDeviceName();
        mConnectedDeviceAddress = ObdPreferences.get(context).getBlueToothDeviceAddress();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);                   //广播变量管理器获
        blueList = getBlueTooth();//初始化蓝牙
        initReceiver();//注册选择默认车辆广播
        initRecordReceiver();//注册车辆检测记录跟新广播
        getUserInfo(getUserId(), getToken(), spUtil.getString("vehicleId", ""));
        getTheUserCurrentRecharge(getUserId(), getToken());
        layoutMoreDash.setOnClickListener(v -> {
            Intent intent = new Intent(context, MyVehicleDashActivity.class);
            startActivityForResult(intent, 102);
        });
        setCheckRecord();
        layoutCheck.setOnClickListener(v -> {
            if (isConnected) {
                if (isVip) {
                    Intent intent = new Intent(context, VehicleCheckActivity.class);
                    startActivityForResult(intent, 102);
                } else {
                    showTipDialog("请购买服务");
                }
            } else {
                showTipDialog("设备未连接,请连接设备");
            }
        });
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {

            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                showSingleChoiceDialog();
            }
        });
    }


    /**
     * 展示默认车辆
     */
    private void showDefaultVehicle() {
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleCar.setLayoutManager(manager);
        homeAdapter = new HomeAdapter(context);
        getCommonBrandList(getToken());
        homeAdapter.setClickCallBack(entity -> {
            Intent intent = new Intent(context, VehicleActivity.class);
            intent.putExtra("data", entity);
            startActivity(intent);
        });
    }

    /**
     * 设置车检记录
     */
    private void setCheckRecord() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycleContent.setLayoutManager(layoutManager);
        recordAdapter = new TestRecordAdapter(context);
        recordAdapter.setToken(getToken());
        getTestRecordPageList(getToken(), String.valueOf(1), String.valueOf(5), getUserId());
        recordAdapter.setClickCallBack(entity -> {
            OBDJsonTripEntity tripEntity = JSON.parseObject(entity.getTestData(), OBDJsonTripEntity.class);
            Intent intent = new Intent(context, CheckRecordDetailsActivity.class);
            intent.putExtra("data", tripEntity);
            startActivity(intent);
        });
        layoutMoreTest.setOnClickListener(v -> JumpUtil.startAct(context, CheckRecordActivity.class));
    }

    private void sendMessage(int what) {
        Message msg = new Message();
        msg.what = what;
        handler.sendMessage(msg);
    }

    /**
     * @param token 用户token
     *              获取常用车辆
     */
    private void getCommonBrandList(String token) {
        OkHttpUtils.get().url(SERVER_URL + getCommonBrandList_URL).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                AutomobileBrandEntity entity = JSON.parseObject(response, AutomobileBrandEntity.class);
                if (entity.isSuccess()) {
                    homeAdapter.setList(entity.getData());
                    recycleCar.setAdapter(homeAdapter);
                }
            }
        });
    }

    /**
     * @param appUserId 用户id
     * @param token     token
     *                  获取当前账号充值状态
     */
    private void getTheUserCurrentRecharge(String appUserId, String token) {
        OkHttpUtils.get().url(SERVER_URL + getTheUserCurrentRecharge_URL).
                addParam("appUserId", appUserId).
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                UserCurrentRechargeEntity entity = JSON.parseObject(response, UserCurrentRechargeEntity.class);
                if (entity.isSuccess()) {
                    isVip = entity.getData().size() == 1;
                }
            }
        });
    }

    /**
     * @param userId 用户id
     * @param token  接口令牌
     *               用户信息
     */
    private void getUserInfo(String userId, String token, String vehicleId) {
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
                    if (entity.getData().isTheDeviceBound()) {
                        if (TextUtils.isEmpty(vehicleId)) {
                            //选择已绑定的车辆
                            new CustomeDialog(context, "您已经绑定车辆,请选择检测车辆！", confirm -> {
                                if (confirm) {
                                    JumpUtil.startAct(context, MyVehicleActivity.class);
                                }
                            }).setPositiveButton("确定").setTitle("提示").show();
                        } else {
                            getVehicleInfoById(token, vehicleId);
                        }
                    } else {
                        layoutAddCar.setVisibility(View.VISIBLE);
                        layoutCar.setVisibility(View.GONE);
                        layoutOBD.setVisibility(View.GONE);
                        showDefaultVehicle();
                        //添加车辆绑定
                        layoutAddCar.setOnClickListener(v -> JumpUtil.startAct(context, SelectAutomobileBrandActivity.class));
                    }
                } else {
                    dialogUtils.dismiss();
                    dialogError(context, entity.getMessage());
                }
            }
        });
    }

    /**
     * @param token     用户Token
     * @param vehicleId 车辆ID
     *                  获取用户车辆详情
     */
    private void getVehicleInfoById(String token, String vehicleId) {
        OkHttpUtils.get().url(SERVER_URL + getVehicleInfoById_URL).
                addParam("token", token).
                addParam("vehicleId", vehicleId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response, int id) {
                VehicleInfoEntity entity = JSON.parseObject(response, VehicleInfoEntity.class);
                if (entity.isSuccess()) {
                    layoutAddCar.setVisibility(View.GONE);
                    layoutCar.setVisibility(View.VISIBLE);
                    layoutOBD.setVisibility(View.VISIBLE);
                    layoutCar.setOnClickListener(v -> JumpUtil.startActToData(context, VehicleInfoActivity.class, vehicleId, 0));
                    tvAutomobileBrandName.setText(entity.getData().getAutomobileBrandName());
                    tvModelName.setText(entity.getData().getModelName());
                    if (!TextUtils.isEmpty(entity.getData().getLogo())) {
                        Glide.with(context).load(SERVER_URL + entity.getData().getLogo()).into(ivCarLogo);
                    }
                    deviceAddress = entity.getData().getBluetoothDeviceNumber();
                    if (!TextUtils.isEmpty(deviceAddress) && !isConnected) {
                        connectBtDevice(deviceAddress);
                    }
                    if (entity.getData().getVehicleStatus() == 1) {//车辆状态 1 未绑定 2 已绑定 ,
                        tvHomeObdTip.setText("将设备插入车辆并连接");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_no);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvHomeObdTip.setCompoundDrawables(drawable, null, null, null);
                        tvObd.setText("设备未绑定");
                        layoutOBD.setOnClickListener(v -> {
                            Intent intent = new Intent(context, BindBluetoothDeviceActivity.class);
                            intent.putExtra("vehicleId", vehicleId);
                            startActivityForResult(intent, 100);
                        });
                    } else {
                        tvHomeObdTip.setText(entity.getData().getBluetoothDeviceNumber());
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_ok);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvHomeObdTip.setCompoundDrawables(drawable, null, null, null);
                        tvObd.setText("设备已绑定");
                    }
                }
            }
        });
    }

    /**
     * @param token     用户token
     * @param pageNum   页号
     * @param pageSize  条数
     * @param appUserId 用户id
     *                  获取用户检测记录列表
     */
    private void getTestRecordPageList(String token, String pageNum, String pageSize, String appUserId) {
        OkHttpUtils.get().url(SERVER_URL + getTestRecordPageList_URL).
                addParam("token", token).
                addParam("pageNum", pageNum).
                addParam("pageSize", pageSize).
                addParam("appUserId", appUserId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response, int id) {
                TestRecordEntity entity = JSON.parseObject(response, TestRecordEntity.class);
                if (entity.isSuccess()) {
                    if (entity.getData().getList().size() != 0) {
                        tvCheckTime.setText("上次检测 " + entity.getData().getList().get(0).getDetectionTime());
                    }
                    recordAdapter.setList(entity.getData().getList());
                } else {
                    recordAdapter.setList(null);
                }
                recycleContent.setAdapter(recordAdapter);
            }
        });
    }

    /**
     * 选择已配对蓝牙
     */
    private void showSingleChoiceDialog() {
        yourChoice = 0;
        final String[] items = new String[blueList.size()];
        for (int i = 0; i < blueList.size(); i++) {
            items[i] = blueList.get(i).getBlue_name();
        }
        for (int i = 0; i < items.length; i++) {
            if (mConnectedDeviceName.equals(items[i])) {
                yourChoice = i;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("已配对蓝牙设备");
        builder.setIcon(R.drawable.icon_bluetooth);
        // 第二个参数是默认选项，此处设置为0
        builder.setSingleChoiceItems(items, yourChoice,
                (dialog, which) -> yourChoice = which);
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定",
                (dialog, which) -> {
                    if (yourChoice != -1) {
                        isConn = blueList.get(yourChoice).getBlue_address().equals(deviceAddress);
                        if (!TextUtils.isEmpty(blueList.get(yourChoice).getBlue_address())) {
                            if (isConn) {
                                if (!MainApplication.getBluetoothSocket().isConnected()) {
                                    connectBtDevice(blueList.get(yourChoice).getBlue_address());
                                } else {
                                    showTipDialog("当前设备已连接");
                                }
                            } else {
                                showTipDialog("当前车辆绑定设备,与连接的设备不一致");
                            }
                        } else {
                            showToast(getString(R.string.text_bluetooth_error_connecting));
                        }
                    }
                });
        builder.show();
    }

    /**
     * @param address 蓝牙设备MAC地址
     *                启动与所选蓝牙设备的连接
     */
    private void connectBtDevice(String address) {
        dialogUtils.showProgressDialog("正在连接");
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        new Thread(() ->
                BltManagerUtils.getInstance().createBond(device, (code, msg) -> {
                    if (code == 0) {
                        sendMessage(COMPLETES);
                    } else {
                        sendMessage(COMPLETET);
                    }
                })).start();
    }

    /**
     * 处理建立的蓝牙连接...
     */
    @SuppressLint("StringFormatInvalid")
    private void onConnect() {
        titleBar.setLeftTitle("已连接");
        titleBar.setRightIcon(R.drawable.action_connect);
        isConnected = true;
        TipDialog.show(context, getString(R.string.title_connected_to) + mConnectedDeviceName, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
        dialogUtils.dismiss();
        // getOBDData(MainApplication.getBluetoothSocket());
    }

    /**
     * 处理蓝牙连接断开
     */
    private void onDisconnect() {
        titleBar.setLeftTitle("未连接");
        titleBar.setRightIcon(R.drawable.action_disconnect);
        isConnected = false;
        TipDialog.show(context, mConnectedDeviceName + getString(R.string.title_connected_fail), TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
        dialogUtils.dismiss();
    }


    /**
     * 设置默认模式
     */
    private void setDefaultMode() {
        titleBar.setLeftTitle("未连接");
        titleBar.setRightIcon(R.drawable.action_disconnect);
    }

    private void showTipDialog(String msg) {
        TipDialog.show(context, msg, TipDialog.TYPE_ERROR, TipDialog.TYPE_WARNING);
    }

    /**
     * 注册本地广播
     */
    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter("com.android.ObdCar");
        testReceiver = new TestReceiver();
        mLocalBroadcastManager.registerReceiver(testReceiver, intentFilter);
    }

    private class TestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String vehicleId = intent.getStringExtra("vehicleId");
            String type = intent.getStringExtra("type");
            switch (type) {
                case "1":
                    getTheUserCurrentRecharge(getUserId(), getToken());
                    break;
                case "2":
                    getUserInfo(getUserId(), getToken(), vehicleId);
                    break;
            }
        }
    }

    /**
     * 注册本地广播
     */
    private void initRecordReceiver() {
        //获取实例
        IntentFilter intentFilter = new IntentFilter("com.android.Record");
        RecordReceiver receiver = new RecordReceiver();
        //绑定
        mLocalBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    private class RecordReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getTestRecordPageList(getToken(), String.valueOf(1), String.valueOf(5), getUserId());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解绑
        mLocalBroadcastManager.unregisterReceiver(testReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                if (!TextUtils.isEmpty(mConnectedDeviceAddress)) {
                    connectBtDevice(mConnectedDeviceAddress);
                } else {
                    setDefaultMode();
                    showToast(getString(R.string.text_bluetooth_error_connecting));
                }
            }
        }
        if (requestCode == 100) {
            if (resultCode == 101) {
                getUserInfo(getUserId(), getToken(), spUtil.getString("vehicleId", ""));
            }
        }
    }
}