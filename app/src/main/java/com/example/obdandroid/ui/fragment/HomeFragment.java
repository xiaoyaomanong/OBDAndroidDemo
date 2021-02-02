package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.listener.Data;
import com.example.obdandroid.service.GpsServices;
import com.example.obdandroid.ui.activity.AutomobileBrandActivity;
import com.example.obdandroid.ui.activity.BindBluetoothDeviceActivity;
import com.example.obdandroid.ui.activity.CheckRecordActivity;
import com.example.obdandroid.ui.activity.CheckRecordDetailsActivity;
import com.example.obdandroid.ui.activity.MyVehicleActivity;
import com.example.obdandroid.ui.activity.MyVehicleDash;
import com.example.obdandroid.ui.activity.SelectAutomobileBrandActivity;
import com.example.obdandroid.ui.activity.VehicleActivity;
import com.example.obdandroid.ui.activity.VehicleInfoActivity;
import com.example.obdandroid.ui.adapter.HomeAdapter;
import com.example.obdandroid.ui.adapter.TestRecordAdapter;
import com.example.obdandroid.ui.entity.AutomobileBrandEntity;
import com.example.obdandroid.ui.entity.BluetoothDeviceEntity;
import com.example.obdandroid.ui.entity.TestRecordEntity;
import com.example.obdandroid.ui.entity.UserInfoEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CustomeDialog;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.gc.materialdesign.widgets.Dialog;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;
import com.sohrab.obd.reader.trip.TripRecord;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.USER_INFO_URL;
import static com.example.obdandroid.config.APIConfig.getCommonBrandList_URL;
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
public class HomeFragment extends BaseFragment implements LocationListener, GpsStatus.Listener {
    private Context context;
    private TitleBar titleBar;
    private List<BluetoothDeviceEntity> blueList;
    private int yourChoice;
    private SPUtil spUtil;
    private RecyclerView recycleCar;
    private PhilText tvCurrentSpeed;
    private PhilText tvMaxSpeed;
    private PhilText tvAverageSpeed;
    private CustomerDashboardViewLight dashSpeed;
    private RecyclerView recycleContent;
    private LinearLayout layoutCar;
    private ImageView ivCarLogo;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private LinearLayout layoutOBD;
    private LinearLayout layoutAddCar;
    private LinearLayout layoutMoreTest;
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
    private TripRecord tripRecord;
    private SharedPreferences sharedPreferences;
    private LocationManager mLocationManager;
    private static Data data;
    private Data.OnGpsServiceUpdate onGpsServiceUpdate;
    private HomeAdapter homeAdapter;
    private Chronometer time;

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
        dashSpeed = getView(R.id.dashSpeed);
        tvCurrentSpeed = getView(R.id.tvCurrentSpeed);
        tvMaxSpeed = getView(R.id.tvMaxSpeed);
        tvAverageSpeed = getView(R.id.tvAverageSpeed);
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
        time = getView(R.id.time);
        titleBar.setTitle("汽车扫描");
        spUtil = new SPUtil(context);
        dialogUtils = new DialogUtils(context);
        data = new Data(onGpsServiceUpdate);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getHoldingActivity());
        mConnectedDeviceName = ObdPreferences.get(context).getBlueToothDeviceName();
        mConnectedDeviceAddress = ObdPreferences.get(context).getBlueToothDeviceAddress();
        blueList = getBlueTooth();//初始化蓝牙
        registerOBDReceiver();//注册OBD数据接收广播
        initReceiver();//注册选择默认车辆广播
        setGPS();
        getUserInfo(getUserId(), getToken(), spUtil.getString("vehicleId", ""));
        resetData();
        setSpeed();//设置速度仪表盘
        layoutMoreDash.setOnClickListener(v -> {
            if (tripRecord != null) {
                Intent intent = new Intent(context, MyVehicleDash.class);
                intent.putExtra("data", tripRecord);
                startActivity(intent);
            } else {
                showTipDialog("正在读取OBD数据,请稍后查看");
            }

        });
        setCheckRecord();
        time.setText("00:00:00");
        time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            boolean isPair = true;

            @Override
            public void onChronometerTick(Chronometer chrono) {
                long time;
                if (data.isRunning()) {
                    time = SystemClock.elapsedRealtime() - chrono.getBase();
                    data.setTime(time);
                } else {
                    time = data.getTime();
                }

                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                chrono.setText(hh + ":" + mm + ":" + ss);

                if (data.isRunning()) {
                    chrono.setText(hh + ":" + mm + ":" + ss);
                } else {
                    if (isPair) {
                        isPair = false;
                        chrono.setText(hh + ":" + mm + ":" + ss);
                    } else {
                        isPair = true;
                        chrono.setText("");
                    }
                }
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
     * 启动或停止GSP监听服务
     */
    public void onstartGPS() {
        if (!data.isRunning()) {
            data.setRunning(true);
            time.setBase(SystemClock.elapsedRealtime() - data.getTime());
            time.start();
            data.setFirstTime(true);
            context.startService(new Intent(context, GpsServices.class));
        } else {
            data.setRunning(false);
            context.stopService(new Intent(context, GpsServices.class));
        }
    }

    public void resetData() {
        tvMaxSpeed.setText("0");
        tvCurrentSpeed.setText("0");
        tvAverageSpeed.setText("0");
        time.stop();
        time.setText("00:00:00");
    }

    /**
     * 设置GPS
     */
    @SuppressLint("DefaultLocale")
    private void setGPS() {
        onGpsServiceUpdate = () -> {
            double maxSpeedTemp = data.getMaxSpeed();
            double distanceTemp = data.getDistance();
            double averageTemp;
            if (sharedPreferences.getBoolean("auto_average", false)) {
                averageTemp = data.getAverageSpeedMotion();
            } else {
                averageTemp = data.getAverageSpeed();
            }
            String speedUnits;
            String distanceUnits;
            if (sharedPreferences.getBoolean("miles_per_hour", false)) {
                maxSpeedTemp *= 0.62137119;
                distanceTemp = distanceTemp / 1000.0 * 0.62137119;
                averageTemp *= 0.62137119;
                speedUnits = "mi/h";
                distanceUnits = "mi";
            } else {
                speedUnits = "km/h";
                if (distanceTemp <= 1000.0) {
                    distanceUnits = "m";
                } else {
                    distanceTemp /= 1000.0;
                    distanceUnits = "km";
                }
            }

            SpannableString s = new SpannableString(String.format("%.0f %s", maxSpeedTemp, speedUnits));
            s.setSpan(new RelativeSizeSpan(0.5f), s.length() - speedUnits.length() - 1, s.length(), 0);
            tvMaxSpeed.setText(s.toString().replace("km/h", ""));

            s = new SpannableString(String.format("%.0f %s", averageTemp, speedUnits));
            s.setSpan(new RelativeSizeSpan(0.5f), s.length() - speedUnits.length() - 1, s.length(), 0);
            tvAverageSpeed.setText(s.toString().replace("km/h", ""));

            s = new SpannableString(String.format("%.3f %s", distanceTemp, distanceUnits));
            s.setSpan(new RelativeSizeSpan(0.5f), s.length() - distanceUnits.length() - 1, s.length(), 0);
        };
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 设置车检记录
     */
    private void setCheckRecord() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycleContent.setLayoutManager(layoutManager);
        recordAdapter = new TestRecordAdapter(context);
        recordAdapter.setToken(getToken());
        getTestRecordPageList(getToken(), String.valueOf(1), String.valueOf(2), getUserId());
        recordAdapter.setClickCallBack(entity -> {
            OBDJsonTripEntity tripEntity = JSON.parseObject(entity.getTestData(), OBDJsonTripEntity.class);
            Intent intent = new Intent(context, CheckRecordDetailsActivity.class);
            intent.putExtra("data", tripEntity);
            startActivity(intent);
        });
        layoutMoreTest.setOnClickListener(v -> JumpUtil.startAct(context, CheckRecordActivity.class));
    }

    /**
     * 设置车速仪表
     */
    private void setSpeed() {
        dashSpeed.setmSection(8);
        dashSpeed.setmHeaderText("km/h");
        dashSpeed.setmMin(240);
        dashSpeed.setmMin(0);
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
                        if (TextUtils.isEmpty(vehicleId)) {
                            //选择已绑定的车辆
                            new CustomeDialog(context, "您已经绑定车辆,请选择！", confirm -> {
                                if (confirm) {
                                    JumpUtil.startAct(context, MyVehicleActivity.class);
                                }
                            }).setPositiveButton("确定").setTitle("提示").show();
                        } else {
                            layoutAddCar.setVisibility(View.GONE);
                            layoutCar.setVisibility(View.VISIBLE);
                            layoutOBD.setVisibility(View.VISIBLE);
                            getVehicleInfoById(getToken(), vehicleId);
                            layoutCar.setOnClickListener(v -> JumpUtil.startActToData(context, VehicleInfoActivity.class, vehicleId, 0));
                        }
                    } else {
                        layoutAddCar.setVisibility(View.VISIBLE);
                        layoutCar.setVisibility(View.GONE);
                        layoutOBD.setVisibility(View.GONE);
                        showDefaultVehicle();
                        //添加车辆绑定
                        layoutAddCar.setOnClickListener(v -> JumpUtil.startAct(context, SelectAutomobileBrandActivity.class));
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
                    deviceAddress = entity.getData().getBluetoothDeviceNumber();
                    if (!TextUtils.isEmpty(deviceAddress)) {
                        dialogUtils.showProgressDialog("正在连接OBD");
                        connectBtDevice(deviceAddress);
                    }
                    if (entity.getData().getVehicleStatus() == 1) {//车辆状态 1 未绑定 2 已绑定 ,
                        tvHomeObdTip.setText("将OBD插入车辆并连接");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_no);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tvHomeObdTip.setCompoundDrawables(drawable, null, null, null);
                        tvObd.setText("OBD 未绑定");
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
                        isConnected = blueList.get(yourChoice).getBlue_address().equals(deviceAddress);
                        if (!TextUtils.isEmpty(blueList.get(yourChoice).getBlue_address())) {
                            if (isConnected) {
                                onstartGPS();
                                connectBtDevice(blueList.get(yourChoice).getBlue_address());
                            } else {
                                showTipDialog("当前车辆绑定OBD设备,与连接的OBD设备不一致");
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
        // 获取BluetoothDevice对象
        dialogUtils.showProgressDialog("正在连接OBD");
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
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
        new Handler().postDelayed(() -> dialogUtils.showProgressDialog("读取OBD中..."), 1000);
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

    private void showTipDialog(String msg) {
        TipDialog.show(context, msg, TipDialog.TYPE_ERROR, TipDialog.TYPE_WARNING);
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
                } else if (connectionStatusMsg.equals("socket closed")) {
                    spUtil.put(CONNECT_BT_KEY, "ON");
                    onstartGPS();
                }
            } else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA)) {
                tripRecord = TripRecord.getTripRecode(context);
                if (tripRecord != null) {
                    dialogUtils.dismiss();
                    showTipDialog("OBD数据读取成功");
                }
            }
        }
    };

    /**
     * 注册本地广播
     */
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
    public void onLocationChanged(Location location) {
        if (location.hasAccuracy()) {
            double acc = location.getAccuracy();
            String units;
            if (sharedPreferences.getBoolean("miles_per_hour", false)) {
                units = "ft";
                acc *= 3.28084;
            } else {
                units = "m";
            }
            SpannableString s = new SpannableString(String.format("%.0f %s", acc, units));
            s.setSpan(new RelativeSizeSpan(0.75f), s.length() - units.length() - 1, s.length(), 0);
            LogE("精确度:" + s);
        }

        if (location.hasSpeed()) {
            double speed = location.getSpeed() * 3.6;
            String units;
            if (sharedPreferences.getBoolean("miles_per_hour", false)) { // Convert to MPH
                speed *= 0.62137119;
                units = "mi/h";
            } else {
                units = "km/h";
            }
            SpannableString s = new SpannableString(String.format(Locale.ENGLISH, "%.0f %s", speed, units));
            s.setSpan(new RelativeSizeSpan(0.25f), s.length() - units.length() - 1, s.length(), 0);
            LogE("当前速度:" + s);
            tvCurrentSpeed.setText(s.toString().replace("km/h", ""));
            dashSpeed.setVelocity(Float.parseFloat(s.toString().replace("km/h", "")));
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                int satsUsed = 0;
                Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
                for (GpsSatellite sat : sats) {
                    if (sat.usedInFix()) {
                        satsUsed++;
                    }
                }
                if (satsUsed == 0) {
                    data.setRunning(false);
                    context.stopService(new Intent(context, GpsServices.class));
                }
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showGpsDisabledDialog();
                }
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
        }
    }

    public void showGpsDisabledDialog() {
        Dialog dialog = new Dialog(context, getResources().getString(R.string.gps_disabled), getResources().getString(R.string.please_enable_gps));
        dialog.setOnAcceptButtonClickListener(view -> startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS")));
        dialog.show();
    }

    public static Data getData() {
        return data;
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if (data == null) {
            data = new Data(onGpsServiceUpdate);
        } else {
            data.setOnGpsServiceUpdate(onGpsServiceUpdate);
        }
        if (!mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            Log.w("MainActivity", "找不到GPS位置提供程序。GPS数据显示将不可用。");
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGpsDisabledDialog();
        }
        mLocationManager.addGpsStatusListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销接收器
        context.unregisterReceiver(mObdReaderReceiver);
        //解绑
        lm.unregisterReceiver(testReceiver);
        //停止服务
        context.stopService(new Intent(context, ObdReaderService.class));
        context.stopService(new Intent(context, GpsServices.class));
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