package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.listener.CallEndListener;
import com.example.obdandroid.ui.view.CircleWelComeView;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.Notification;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.TripRecord;

import static com.example.obdandroid.config.Constant.CONNECT_BT_KEY;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_OBD_CONNECTION_STATUS;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_READ_OBD_REAL_TIME_DATA;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：车辆检测
 */
public class VehicleCheckFragment extends BaseFragment {
    private Context context;
    private TitleBar titleBar;
    private CircleWelComeView circleView;
    private TextView btStart;
    private static String mConnectedDeviceName = null;
    private static String mConnectedDeviceAddress = null;

    public static VehicleCheckFragment getInstance() {
        return new VehicleCheckFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vehicle_check;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        context = getHoldingActivity();
        titleBar = getView(R.id.titleBar);
        circleView = getView(R.id.circleView);
        btStart = getView(R.id.btStart);
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
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleView.start();
                LogE("");
                if (!TextUtils.isEmpty(mConnectedDeviceAddress)) {
                    connectBtDevice(mConnectedDeviceAddress);
                } else {
                    showToast(getString(R.string.text_bluetooth_error_connecting));
                }
            }
        });

        circleView.setCallEndListener(() -> LogE("333333"));

        //Notification.show(context, 2, R.mipmap.ico_wechat, getString(R.string.app_name), "这是一条消息", Notification.SHOW_TIME_LONG, Notification.TYPE_NORMAL);
    }

    /**
     * @param address 蓝牙设备MAC地址
     *                启动与所选蓝牙设备的连接
     */
    private void connectBtDevice(String address) {
        // 获取BluetoothDevice对象
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        startServiceOBD(device);
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
     * 处理建立的蓝牙连接...
     */
    private void onConnect() {

    }

    /**
     * 处理蓝牙连接断开
     */
    private void onDisconnect() {

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
                if (circleView.isDiffuse()) {
                    circleView.stop();
                }

              /*  tvContent.setText(tripRecord.toString());
                tvHighSpeed.setText(tripRecord.getSpeedMax() + " km/h");
                dashSpeed.setRealTimeValue(tripRecord.getSpeed());*/
                // 在这里，您可以使用getter方法从TripRecord获取实时数据，如
                //tripRecord.getSpeed();
                //tripRecord.getEngineRpm();
            }
        }
    };

    private void showTipDialog(String msg, int type) {
        TipDialog.show(context, msg, TipDialog.TYPE_ERROR, type);
    }

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
}