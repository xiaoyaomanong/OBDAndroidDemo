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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.ui.activity.VehicleInfoActivity;
import com.example.obdandroid.ui.adapter.HomeAdapter;
import com.example.obdandroid.ui.adapter.TestRecordAdapter;
import com.example.obdandroid.ui.entity.TestRecordEntity;
import com.example.obdandroid.ui.entity.UserInfoEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.DashboardView;
import com.example.obdandroid.utils.BitMapUtils;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.ToastUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.TripRecord;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.USER_INFO_URL;
import static com.example.obdandroid.config.APIConfig.getTestRecordPageList_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;
import static com.example.obdandroid.config.Constant.CONNECT_BT_KEY;
import static com.example.obdandroid.config.Constant.REQUEST_ENABLE_BT;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_OBD_CONNECTION_STATUS;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_READ_OBD_REAL_TIME_DATA;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class HomeFragment extends BaseFragment {
    private Context context;
    private TitleBar titleBar;
    private BluetoothAdapter bluetoothadapter;
    private List<HashMap<String, Object>> blueList;
    private int yourChoice;
    private SPUtil spUtil;
    private PhilText tvHighSpeed;
    private DashboardView dashSpeed;
    private PhilText tvCurrentSpeed;
    private RecyclerView recycleContent;
    private LinearLayout layoutCar;
    private ImageView ivCarLogo;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private LinearLayout layoutOBD;
    private LinearLayout layoutAddCar;
    private TextView tvObd;
    private TextView tvHomeObdTip;
    private static String mConnectedDeviceName = null;
    private static String mConnectedDeviceAddress = null;
    private TestRecordAdapter recordAdapter;
    private LocalBroadcastManager lm;
    private TestReceiver testReceiver;
    private DialogUtils dialogUtils;
    private boolean isConnected = false;
    private String deviceAddress;

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
        RecyclerView recycleCar = getView(R.id.recycleCar);
        recycleContent = getView(R.id.recycleContent);
        dashSpeed = getView(R.id.dashSpeed);
        tvHighSpeed = getView(R.id.tvHighSpeed);
        tvCurrentSpeed = getView(R.id.tvCurrentSpeed);
        layoutCar = getView(R.id.layoutCar);
        ivCarLogo = getView(R.id.ivCarLogo);
        tvAutomobileBrandName = getView(R.id.tvAutomobileBrandName);
        tvModelName = getView(R.id.tvModelName);
        layoutOBD = getView(R.id.layoutOBD);
        tvObd = getView(R.id.tv_obd);
        layoutAddCar = getView(R.id.layoutAddCar);
        tvHomeObdTip = getView(R.id.tv_home_obd_tip);
        titleBar.setTitle("汽车扫描");
        spUtil = new SPUtil(context);
        dialogUtils = new DialogUtils(context);
        mConnectedDeviceName = ObdPreferences.get(context).getBlueToothDeviceName();
        mConnectedDeviceAddress = ObdPreferences.get(context).getBlueToothDeviceAddress();
        initBlueTooth();
        checkBlueTooth();
        registerOBDReceiver();
        initReceiver();
        getUserInfo(getUserId(), getToken(), spUtil.getString("vehicleId", ""));
        // 设置数据更新计时器
        GridLayoutManager manager = new GridLayoutManager(context, 5);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleCar.setLayoutManager(manager);
        HomeAdapter homeAdapter = new HomeAdapter(context);
        recycleCar.setAdapter(homeAdapter);
        homeAdapter.setClickCallBack(name -> {

        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycleContent.setLayoutManager(layoutManager);
        recordAdapter = new TestRecordAdapter(context);
        getTestRecordPageList(getToken(), String.valueOf(1), String.valueOf(2), getUserId());
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
     * @param userId 用户id
     * @param token  接口令牌
     *               用户信息
     */
    private void getUserInfo(String userId, String token, String vehicleId) {
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
                    if (entity.getData().isTheDeviceBound()) {
                        layoutAddCar.setVisibility(View.GONE);
                        layoutCar.setVisibility(View.VISIBLE);
                        layoutOBD.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(vehicleId)) {
                            //选择已绑定的车辆
                        } else {
                            getVehicleInfoById(getToken(), vehicleId);
                            layoutCar.setOnClickListener(v -> JumpUtil.startActToData(context, VehicleInfoActivity.class, vehicleId, 0));
                        }
                    } else {
                        layoutAddCar.setVisibility(View.VISIBLE);
                        layoutCar.setVisibility(View.GONE);
                        layoutOBD.setVisibility(View.GONE);
                        //添加车辆绑定
                    }
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
                    tvAutomobileBrandName.setText(entity.getData().getAutomobileBrandName());
                    tvModelName.setText(entity.getData().getModelName());
                    if (!TextUtils.isEmpty(entity.getData().getLogo())) {
                        Glide.with(context).load(SERVER_URL + entity.getData().getLogo()).into(ivCarLogo);
                    }
                    deviceAddress=entity.getData().getBluetoothDeviceNumber();
                    if (entity.getData().getVehicleStatus() == 1) {//车辆状态 1 未绑定 2 已绑定 ,
                        tvHomeObdTip.setText("将OBD插入车辆并连接");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_no);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvHomeObdTip.setCompoundDrawables(drawable, null, null, null);
                        tvObd.setText("OBD 未绑定");
                    } else {
                        tvHomeObdTip.setText(entity.getData().getBluetoothDeviceNumber());
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_ok);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvHomeObdTip.setCompoundDrawables(drawable, null, null, null);
                        tvObd.setText("OBD 已绑定");
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

            @Override
            public void onResponse(String response, int id) {
                TestRecordEntity entity = JSON.parseObject(response, TestRecordEntity.class);
                if (entity.isSuccess()) {
                    recordAdapter.setList(entity.getData().getList());
                    recycleContent.setAdapter(recordAdapter);
                }
            }
        });
    }

    /**
     * 选择已配对蓝牙
     */
    private void showSingleChoiceDialog() {
        yourChoice = 0;
        final String[] items = new String[blueList.size()];
        final List<BluetoothDevice> devicesList = new ArrayList<>();
        for (int i = 0; i < blueList.size(); i++) {
            items[i] = (String) blueList.get(i).get("blue_name");
            devicesList.add((BluetoothDevice) blueList.get(i).get("blue_device"));
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
                        dialogUtils.showProgressDialog("正在连接OBD");
                        mConnectedDeviceAddress=devicesList.get(yourChoice).getAddress();
                        isConnected= mConnectedDeviceAddress.equals(mConnectedDeviceAddress);
                        if (!TextUtils.isEmpty(devicesList.get(yourChoice).getAddress())) {
                            if (isConnected){
                                connectBtDevice(devicesList.get(yourChoice).getAddress());
                            }else {
                                dialogUtils.dismiss();
                                showToast("当前车辆绑定OBD设备,与连接的OBD设备不一致");
                            }

                        } else {
                            dialogUtils.dismiss();
                            showToast(getString(R.string.text_bluetooth_error_connecting));
                        }
                        startServiceOBD(devicesList.get(yourChoice));
                    }
                });
        builder.show();
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
            blueList = new ArrayList<>();
            for (BluetoothDevice bluetoothDevice : devices) {
                HashMap<String, Object> blueHashMap = new HashMap<>();
                blueHashMap.put("blue_device", bluetoothDevice);
                blueHashMap.put("blue_name", bluetoothDevice.getName());
                blueHashMap.put("blue_address", bluetoothDevice.getAddress());
                blueList.add(blueHashMap);
            }
        } else {
            ToastUtil.shortShow("本机没有蓝牙设备");
        }
    }

    /**
     * 检查蓝牙
     */
    private void checkBlueTooth() {
        bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
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
        setDefaultMode();
    }

    /**
     * @param address 蓝牙设备MAC地址
     *                启动与所选蓝牙设备的连接
     */
    private void connectBtDevice(String address) {
        // 获取BluetoothDevice对象
        BluetoothDevice device = bluetoothadapter.getRemoteDevice(address);
        startServiceOBD(device);
    }

    /**
     * 处理建立的蓝牙连接...
     */
    @SuppressLint("StringFormatInvalid")
    private void onConnect() {
        TipDialog.show(context, getString(R.string.title_connected_to) + mConnectedDeviceName, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
        dialogUtils.dismiss();
        titleBar.setLeftTitle("已连接");
        titleBar.setRightIcon(R.drawable.action_connect);
        spUtil.put(CONNECT_BT_KEY, "ON");
    }

    /**
     * 注册OBD读取服务
     */
    private void registerOBDReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_READ_OBD_REAL_TIME_DATA);
        intentFilter.addAction(ACTION_OBD_CONNECTION_STATUS);
        context.registerReceiver(mObdReaderReceiver, intentFilter);
    }

    /**
     * @param bluetoothDevice 蓝夜设备
     *                        启动蓝牙连接服务
     */
    private void startServiceOBD(BluetoothDevice bluetoothDevice) {
        //启动服务，该服务将在后台执行连接，并执行命令，直到您停止
        Intent intent = new Intent(context, ObdReaderService.class);
        intent.putExtra("device", bluetoothDevice);
        context.startService(intent);
    }

    /**
     * 处理蓝牙连接断开
     */
    private void onDisconnect() {
        titleBar.setLeftTitle("未连接");
        titleBar.setRightIcon(R.drawable.action_disconnect);
        spUtil.put(CONNECT_BT_KEY, "OFF");
        dialogUtils.dismiss();
    }


    /**
     * 设置默认模式
     */
    private void setDefaultMode() {
        titleBar.setLeftTitle("未连接");
        titleBar.setRightIcon(R.drawable.action_disconnect);
        spUtil.put(CONNECT_BT_KEY, "OFF");
    }

    private void showTipDialog(String msg, int type) {
        TipDialog.show(context, msg, TipDialog.TYPE_ERROR, type);
    }

    /**
     * 接收OBD连接状态和实时数据的广播接收器
     */
    private final BroadcastReceiver mObdReaderReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_OBD_CONNECTION_STATUS)) {
                String connectionStatusMsg = intent.getStringExtra(ObdReaderService.INTENT_OBD_EXTRA_DATA);
                if (connectionStatusMsg.equals(getString(R.string.obd_connected))) {
                    //OBD连接在OBD连接之后做什么
                    onConnect();
                } else if (connectionStatusMsg.equals(getString(R.string.connect_lost))) {
                    //OBD断开连接断开后做什么
                    onDisconnect();
                } else if (connectionStatusMsg.contains("未检测到OBD设备")) {
                    onDisconnect();
                }
            } else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA)) {
                TripRecord tripRecord = TripRecord.getTripRecode(context);
                tvHighSpeed.setText(String.valueOf(tripRecord.getSpeedMax()));
                tvCurrentSpeed.setText(String.valueOf(tripRecord.getSpeed()));
                dashSpeed.setRealTimeValue(tripRecord.getSpeed());
            }
        }
    };

    private void initReceiver() {
        //获取实例
        lm = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("com.android.ObdCar");
        testReceiver = new TestReceiver();
        //绑定
        lm.registerReceiver(testReceiver, intentFilter);

    }

    private class TestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String vehicleId = intent.getStringExtra(Intent.EXTRA_TEXT);
            getUserInfo(getUserId(), getToken(), vehicleId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销接收器
        try {
            context.unregisterReceiver(mObdReaderReceiver);
        } catch (Exception e) {
            LogE("33333");
        }
        //解绑
        lm.unregisterReceiver(testReceiver);
        //停止服务
        context.stopService(new Intent(context, ObdReaderService.class));
        // 这将停止后台线程，如果任何运行立即。
        ObdPreferences.get(context).setServiceRunningStatus(false);
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
    }
}