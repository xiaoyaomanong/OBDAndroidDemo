package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.listener.ObdProgressListener;
import com.example.obdandroid.listener.SocketCallBack;
import com.example.obdandroid.service.BtCommService;
import com.example.obdandroid.service.CommService;
import com.example.obdandroid.service.ObdCommandJob;
import com.example.obdandroid.ui.adapter.HomeAdapter;
import com.example.obdandroid.utils.DividerGridItemDecoration;
import com.example.obdandroid.utils.ODBUtils;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.ToastUtil;
import com.github.pires.obd.enums.AvailableCommandNames;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.obdandroid.config.Constant.BT_ADDRESS_KEY;
import static com.example.obdandroid.config.Constant.BT_NAME_KEY;
import static com.example.obdandroid.config.Constant.CONNECT_BT_KEY;
import static com.example.obdandroid.config.Constant.DEVICE_ADDRESS;
import static com.example.obdandroid.config.Constant.DEVICE_NAME;
import static com.example.obdandroid.config.Constant.DISPLAY_UPDATE_TIME;
import static com.example.obdandroid.config.Constant.GPS_DISTANCE_PERIOD_KEY;
import static com.example.obdandroid.config.Constant.GPS_UPDATE_PERIOD_KEY;
import static com.example.obdandroid.config.Constant.MESSAGE_DEVICE_NAME;
import static com.example.obdandroid.config.Constant.MESSAGE_STATE_CHANGE;
import static com.example.obdandroid.config.Constant.MESSAGE_TOAST;
import static com.example.obdandroid.config.Constant.MESSAGE_UPDATE_VIEW;
import static com.example.obdandroid.config.Constant.REQUEST_ENABLE_BT;
import static com.example.obdandroid.config.Constant.TOAST;


/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class HomeFragment extends BaseFragment implements ObdProgressListener, LocationListener {
    private Context context;
    private TitleBar titleBar;
    private BluetoothAdapter bluetoothadapter;
    private List<HashMap<String, Object>> blueList;
    private int yourChoice;
    private SPUtil spUtil;
    private boolean preRequisites = true;
    private boolean isServiceBound;
    private BluetoothSocket bluetoothSocket;
    /**
     * 显示更新计时器
     */
    private static final Timer updateTimer = new Timer();
    /**
     * 连接的BT设备的名称
     */
    private static String mConnectedDeviceName = null;
    private static String mConnectedDeviceAddress = null;
    /**
     * 当前操作模式
     */
    private MODE mode = MODE.OFFLINE;


    /**
     * 操作模式
     */
    public enum MODE {
        OFFLINE,//< OFFLINE mode
        ONLINE,    //< ONLINE mode
    }

    /**
     * 处理消息请求
     */
    @SuppressLint("HandlerLeak")
    private transient final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                //接收到的处理程序通知事件的日志跟踪消息
                switch (msg.what) {
                    case MESSAGE_STATE_CHANGE:
                        // 接收到的处理程序通知事件的日志跟踪消息
                        switch ((CommService.STATE) msg.obj) {
                            case CONNECTED:
                                onConnect();
                                break;
                            case CONNECTING:
                                // 正在连接
                                titleBar.setLeftTitle(R.string.title_connecting);
                                break;
                            default:
                                onDisconnect();
                                break;
                        }
                        break;
                    case MESSAGE_DEVICE_NAME:
                        // 保存连接设备的名称
                        mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                        mConnectedDeviceAddress = msg.getData().getString(DEVICE_ADDRESS);
                        spUtil.put(BT_NAME_KEY, mConnectedDeviceName);
                        spUtil.put(BT_ADDRESS_KEY, mConnectedDeviceAddress);
                        Toast.makeText(context, getString(R.string.connected_to) + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                        break;

                    case MESSAGE_TOAST:
                        Toast.makeText(context, msg.getData().getString(TOAST),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (Exception ex) {
                LogE("mHandler中的错误" + ex.getMessage());
            }
        }
    };
    public Map<String, String> commandResult = new HashMap<>();
    boolean mGpsIsStarted = false;
    private Location mLastLocation;
    private final Runnable mQueueCommands = new Runnable() {
        public void run() {
            double lat = 0;
            double lon = 0;
            double alt = 0;
            final int posLen = 7;
            if (mGpsIsStarted && mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();
                alt = mLastLocation.getAltitude();

                StringBuilder sb = new StringBuilder();
                sb.append("Lat: ");
                sb.append(String.valueOf(mLastLocation.getLatitude()), 0, posLen);
                sb.append(" Lon: ");
                sb.append(String.valueOf(mLastLocation.getLongitude()), 0, posLen);
                sb.append(" Alt: ");
                sb.append(mLastLocation.getAltitude());
                LogE(sb.toString());
            }
            // final String vin = spUtil.getString(VEHICLE_ID_KEY, "UNDEFINED_VIN");//汽车id
            Map<String, String> temp = new HashMap<>(commandResult);
            LogE("命令结果:" + JSON.toJSONString(temp));
            commandResult.clear();
            new Handler().postDelayed(mQueueCommands, 1);
        }
    };
    private LocationManager mLocService;
    private LocationProvider mLocProvider;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void initView(View view, Bundle savedInstanceState) {
        context = getHoldingActivity();
        titleBar = getView(R.id.titleBar);
        RecyclerView recycleFun = getView(R.id.recycle_Fun);
        titleBar.setTitle("汽车扫描");
        spUtil = new SPUtil(context);
        mConnectedDeviceName = spUtil.getString(BT_NAME_KEY, "");
        mConnectedDeviceAddress = spUtil.getString(BT_ADDRESS_KEY, "");
        initBlueTooth();
        if (CommService.medium == CommService.MEDIUM.BLUETOOTH) {// 获取本地蓝牙适配器
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
        }
        if (!TextUtils.isEmpty(mConnectedDeviceAddress)) {
            connectBtDevice(mConnectedDeviceAddress);
        } else {
            setDefaultMode(mode);
            TipDialog.show(context, getString(R.string.text_bluetooth_error_connecting), TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_WARNING);
        }

        // 设置数据更新计时器
        updateTimer.schedule(updateTask, 0, DISPLAY_UPDATE_TIME);
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleFun.setLayoutManager(manager);
        recycleFun.addItemDecoration(new DividerGridItemDecoration(context));
        HomeAdapter homeAdapter = new HomeAdapter(context);
        recycleFun.setAdapter(homeAdapter);

        homeAdapter.setClickCallBack(name -> {

        });
        gpsInit();
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
     * 选择已配对蓝牙
     */
    private void showSingleChoiceDialog() {
        yourChoice = 0;
        final String[] items = new String[blueList.size()];
        final String[] itemsAddress = new String[blueList.size()];
        for (int i = 0; i < blueList.size(); i++) {
            items[i] = (String) blueList.get(i).get("blue_name");
            itemsAddress[i] = (String) blueList.get(i).get("blue_address");
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
                        connectBtDevice(itemsAddress[yourChoice]);
                    }
                });
        builder.show();
    }

    /**
     * 计时器任务循环更新数据屏幕
     */
    private transient final TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            /* 转发消息以更新视图*/
            Message msg = mHandler.obtainMessage(MESSAGE_UPDATE_VIEW);
            mHandler.sendMessage(msg);
        }
    };

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
            LogE("获取已经配对" + devices.size());
            for (BluetoothDevice bluetoothDevice : devices) {
                LogE("已经配对的蓝牙设备：");
                LogE(bluetoothDevice.getName());
                LogE(bluetoothDevice.getAddress());
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
     * @param address 蓝牙设备MAC地址
     *                启动与所选蓝牙设备的连接
     */
    private void connectBtDevice(String address) {
        // 获取BluetoothDevice对象
        BluetoothDevice device = bluetoothadapter.getRemoteDevice(address);
        //尝试连接到设备  BT通讯服务的成员对象
        CommService mCommService = new BtCommService(context, mHandler);
        mCommService.connect(device, true, new SocketCallBack() {
            @Override
            public void connectMsg(String msg, BluetoothSocket socket) {
                if (TextUtils.equals(msg, "已连接")) {
                    bluetoothSocket = socket;
                }
            }
        });
    }

    /**
     * 处理建立的蓝牙连接...
     */
    @SuppressLint("StringFormatInvalid")
    private void onConnect() {
        mode = MODE.ONLINE;
        // 显示连接状态
        TipDialog.show(context, R.string.title_connected_to + mConnectedDeviceName, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
        titleBar.setLeftTitle("ONLINE");
        titleBar.setRightIcon(R.drawable.action_connect);
        spUtil.put(CONNECT_BT_KEY, "ONLINE");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ODBUtils.getInstance(context).startObdConnection(bluetoothSocket, job -> stateUpdate(job));
            }
        }).start();


    }

    /**
     * 处理蓝牙连接断开
     */
    private void onDisconnect() {
        mode = MODE.OFFLINE;
        titleBar.setLeftTitle("OFFLINE");
        titleBar.setRightIcon(R.drawable.action_disconnect);
        spUtil.put(CONNECT_BT_KEY, "OFFLINE");
        // preRequisites = false;
    }

    /**
     * @param mode 模式
     *             设置默认模式
     */
    private void setDefaultMode(MODE mode) {
        if (mode.equals(MODE.OFFLINE)) {
            titleBar.setLeftTitle("OFFLINE");
            titleBar.setRightIcon(R.drawable.action_disconnect);
            spUtil.put(CONNECT_BT_KEY, "OFFLINE");
        }
    }

    private void showTipDialog(String msg, int type) {
        TipDialog.show(context, msg, TipDialog.TYPE_ERROR, type);
    }

    /**
     * gps定位初始化
     */
    private void gpsInit() {
        mLocService = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mLocService != null) {
            mLocProvider = mLocService.getProvider(LocationManager.GPS_PROVIDER);
            if (mLocProvider != null) {
                if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocService.addGpsStatusListener(event -> {
                    switch (event) {
                        case GpsStatus.GPS_EVENT_STARTED:
                            showToast(getString(R.string.status_gps_started));
                            break;
                        case GpsStatus.GPS_EVENT_STOPPED:
                            showToast(getString(R.string.status_gps_stopped));
                            break;
                        case GpsStatus.GPS_EVENT_FIRST_FIX:
                            showToast(getString(R.string.status_gps_fix));
                            break;
                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                            break;
                    }
                });
                //mLocService.addGpsStatusListener(this);
                if (mLocService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showToast(getString(R.string.status_gps_ready));
                    return;
                }
            }
        }
        showTipDialog(getString(R.string.status_gps_no_support), TipDialog.TYPE_WARNING);
        LogE("无法获得GPS提供商");
    }

    private synchronized void gpsStart() {
        if (!mGpsIsStarted && mLocProvider != null && mLocService != null && mLocService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocService.requestLocationUpdates(mLocProvider.getName(), spUtil.getInt(GPS_UPDATE_PERIOD_KEY, 1), spUtil.getFloat(GPS_DISTANCE_PERIOD_KEY, 5), this);
            mGpsIsStarted = true;
        } else {
            showToast(getString(R.string.status_gps_no_support));
        }
    }

    private synchronized void gpsStop() {
        if (mGpsIsStarted) {
            mLocService.removeUpdates(this);
            mGpsIsStarted = false;
            showToast(getString(R.string.status_gps_stopped));
        }
    }

    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    public void onStatusChanged(String provider, int status, android.os.Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    @Override
    public void stateUpdate(ObdCommandJob job) {
        final String cmdName = job.getCommand().getName();
        String cmdResult = "";
        final String cmdID = LookUpCommand(cmdName);

        if (job.getState().equals(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR)) {
            cmdResult = job.getCommand().getResult();
            if (cmdResult != null && isServiceBound) {
                LogE("OBD状态:" + cmdResult.toLowerCase());
            }
        } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.BROKEN_PIPE)) {
            if (isServiceBound)
                stopLiveData();
        } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED)) {
            cmdResult = getString(R.string.status_obd_no_support);
        } else {
            cmdResult = job.getCommand().getFormattedResult();
            if (isServiceBound)
                LogE("OBD状态:" + getString(R.string.status_obd_data));
        }
        LogE("cmdID: " + cmdID + "   cmdName: " + cmdName + "  cmdResult: " + cmdResult);
        commandResult.put(cmdID, cmdResult);
        Map<String, String> temp = new HashMap<>(commandResult);
        LogE("命令结果:" + JSON.toJSONString(temp));
        // commandResult.clear();
        //updateTripStatistic(job, cmdID);
    }

    public static String LookUpCommand(String txt) {
        for (AvailableCommandNames item : AvailableCommandNames.values()) {
            if (item.getValue().equals(txt)) return item.name();
        }
        return txt;
    }

    private void stopLiveData() {
        LogE("停止获取OBD实时数据");
        gpsStop();
    }
}