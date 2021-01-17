package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.ui.adapter.HomeAdapter;
import com.example.obdandroid.ui.view.dashView.DashboardView;
import com.example.obdandroid.utils.DividerGridItemDecoration;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.ToastUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.TripRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    private TextView tvContent;
    private TextView tvHighSpeed;
    private  DashboardView dashSpeed;
    private static String mConnectedDeviceName = null;
    private static String mConnectedDeviceAddress = null;

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
        RecyclerView recycleCar = getView(R.id.recycleCar);
        RecyclerView recycleContent = getView(R.id.recycleContent);
         dashSpeed = getView(R.id.dashSpeed);
        tvContent = getView(R.id.tv_content);
        tvHighSpeed = getView(R.id.tvHighSpeed);
        titleBar.setTitle("汽车扫描");
        spUtil = new SPUtil(context);
        mConnectedDeviceName = ObdPreferences.get(context).getBlueToothDeviceName();
        mConnectedDeviceAddress = ObdPreferences.get(context).getBlueToothDeviceAddress();
        /*
         *  配置obd：在arrayList中添加所需命令并设置为ObdConfiguration。
         *  如果您没有设置任何命令或传递null，那么将请求所有命令OBD command。   *
         */
        ObdConfiguration.setmObdCommands(context, null);//传递null意味着我们现在正在执行所有OBD命令，但是您应该添加必需的命令以便像上面注释的行一样快速检索。


        // 设定每升汽油价格，以便计算汽油成本。默认值为7$/l
        float gasPrice = 7; // 每升，你应该根据你的要求初始化。
        ObdPreferences.get(context).setGasPrice(gasPrice);
        initBlueTooth();
        checkBlueTooth(mConnectedDeviceAddress);
        // 设置数据更新计时器
        GridLayoutManager manager = new GridLayoutManager(context, 5);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleCar.setLayoutManager(manager);
        HomeAdapter homeAdapter = new HomeAdapter(context);
        recycleCar.setAdapter(homeAdapter);
        homeAdapter.setClickCallBack(name -> {

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
    private void checkBlueTooth(String blueToothDeviceAddress) {
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
        if (!TextUtils.isEmpty(blueToothDeviceAddress)) {
            connectBtDevice(blueToothDeviceAddress);
        } else {
            setDefaultMode();
            TipDialog.show(context, getString(R.string.text_bluetooth_error_connecting), TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_WARNING);
        }
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
        TipDialog.show(context, R.string.title_connected_to + mConnectedDeviceName, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
        titleBar.setLeftTitle("已连接");
        titleBar.setRightIcon(R.drawable.action_connect);
        spUtil.put(CONNECT_BT_KEY, "ON");
    }

    /**
     * @param bluetoothDevice 蓝夜设备
     *                        启动蓝牙连接服务
     */
    private void startServiceOBD(BluetoothDevice bluetoothDevice) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_READ_OBD_REAL_TIME_DATA);
        intentFilter.addAction(ACTION_OBD_CONNECTION_STATUS);
        context.registerReceiver(mObdReaderReceiver, intentFilter);
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
                LogE("connectionStatusMsg:" + connectionStatusMsg);
                Toast.makeText(context, connectionStatusMsg, Toast.LENGTH_SHORT).show();
                if (connectionStatusMsg.equals(getString(R.string.obd_connected))) {
                    //OBD连接在OBD连接之后做什么
                    onConnect();
                } else if (connectionStatusMsg.equals(getString(R.string.connect_lost))) {
                    showTipDialog("连接OBD失败", TipDialog.TYPE_ERROR);
                    //OBD断开连接断开后做什么
                    onDisconnect();
                } else {
                    showTipDialog(connectionStatusMsg, TipDialog.TYPE_WARNING);
                    // 在这里您可以检查OBD连接和配对状态
                }
            } else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA)) {
                TripRecord tripRecord = TripRecord.getTripRecode(context);
                LogE("实时数据:" + tripRecord.toString());
                tvContent.setText(tripRecord.toString());
                tvHighSpeed.setText(tripRecord.getSpeedMax()+" km/h");
                dashSpeed.setRealTimeValue(tripRecord.getSpeed());
                // 在这里，您可以使用getter方法从TripRecord获取实时数据，如
                //tripRecord.getSpeed();
                //tripRecord.getEngineRpm();
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销接收器
        context.unregisterReceiver(mObdReaderReceiver);
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
                    TipDialog.show(context, getString(R.string.text_bluetooth_error_connecting), TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_WARNING);
                }
            }
        }
    }
}