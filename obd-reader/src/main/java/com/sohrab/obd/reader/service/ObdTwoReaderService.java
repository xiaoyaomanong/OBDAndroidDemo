package com.sohrab.obd.reader.service;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.widget.Toast;

import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.constants.DefineObdTwoReader;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.obdCommand.control.TroubleCodesCommand;
import com.sohrab.obd.reader.trip.TripRecordCar;
import com.sohrab.obd.reader.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;

import app.com.android_obd_reader.R;

/**
 * 作者：Jealous
 * 日期：2021/1/21
 * 描述：
 */
public class ObdTwoReaderService extends IntentService implements DefineObdTwoReader {
    private static final String TAG = "ObdTwoReaderService";
    //OBD-2连接时接收
    public final static char PID_STATUS_SUCCESS = '1';
    private static final int DELAY_FIFTEEN_SECOND = 15000;
    private static final int DELAY_TWO_SECOND = 2000;
    // 如果为true，则用于查找TroubleCode。这用于显示故障的检查活动。
    public boolean mIsFaultCodeRead = true;
    private final IBinder mBinder = new LocalBinder();
    private BluetoothManager mBluetoothManager;//Bluetooth Manager
    private BluetoothAdapter mBluetoothAdapter;//Bluetooth adapter
    private BluetoothSocket mSocket;
    //设置OBD-2连接状态
    private boolean isConnected;
    private boolean mIsRunningSuccess;
    private Intent mIntent = new Intent(ACTION_READ_OBD_REAL_TIME_DATA_CAR);
    private char[] mSupportedPids;

    public ObdTwoReaderService() {
        super("ObdTwoReaderService");
        LogUtils.i("ObdTwoReaderService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        BluetoothDevice bluetoothDevice = intent.getExtras().getParcelable("device");
        if (initiateConnection()) {
            if (!isEnable()) {
                enableBlutooth();
            }
            findObdDevicesAndConnect(bluetoothDevice);
        }
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
        TripRecordCar.getTripTwoRecode(this).clear();
    }

    /**
     * 递归调用此方法，直到服务停止。
     */
    private void findObdDevicesAndConnect(BluetoothDevice bluetoothDevice) {
        if (!isConnected) {
            findPairedDevices(bluetoothDevice);
        }
        if (isConnected) {
            executeCommand();
        }
    }

    /**
     * 在循环中查找成对的OBD-2设备，直到找到并连接或服务停止。
     */
    private void findPairedDevices(BluetoothDevice device) {
        if (mBluetoothAdapter != null & !isConnected) {
            if (device != null) {
                String name = device.getName();
                String v_LINK = "V-LINK";
                String OBD_CAPS = "OBD";//OBD名称
                String OBD_SMALL = "obd";
                if (name != null && (name.contains(OBD_SMALL) || name.contains(OBD_CAPS) || name.toUpperCase().contains(v_LINK))) {
                    try {
                        sendBroadcast(ACTION_OBD_CONNECTION_STATUS_CAR, "正在连接OBD蓝牙设备...");
                        connectOBDDevice(device);
                    } catch (Exception e) {
                        LogUtils.i("connectOBDDevice 返回异常: " + e.getMessage());
                    }
                } else {
                    isConnected = false;
                    ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
                    ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
                    sendBroadcast(ACTION_OBD_CONNECTION_STATUS_CAR, "未检测到OBD设备...");
                }
            }
        }
    }

    /**
     * 将指定的蓝牙车载诊断设备与蓝牙插座连接。
     * 如果蓝牙已连接，则使用init OBD-2命令初始化，
     *
     * @param device OBD蓝牙设备
     * @throws Exception 异常
     */
    public void connectOBDDevice(final BluetoothDevice device) throws Exception {
        try {
            mSocket = (BluetoothSocket) device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class}).invoke(device, 1);
        } catch (Exception e) {
            LogUtils.i("createInsecureRfcommSocket failed");
            closeSocket();
        }
        if (mSocket != null) {
            try {
                mBluetoothAdapter.cancelDiscovery();
                Thread.sleep(100);
                mSocket.connect();
                LogUtils.i("Socket connected");
                ObdPreferences.get(getApplicationContext()).setBlueToothDeviceAddress(device.getAddress());
                ObdPreferences.get(getApplicationContext()).setBlueToothDeviceName(device.getName());
            } catch (Exception e) {
                LogUtils.i("Socket connection  exception :: " + e.getMessage());
                closeSocket();
            }
            boolean isSockedConnected = mSocket.isConnected();
            if (isSockedConnected) {
                try {
                    Thread.sleep(DELAY_TWO_SECOND);
                    LogUtils.i("在新线程中执行reset命令 :: " + Thread.currentThread().getId());
                    final Thread newThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mIsRunningSuccess = false;
                                executeCommand();
                              /*  // 此线程是必需的，因为在Headunit中命令.run方法无限块，因此，线程的最长寿命为15秒，这样就可以处理块了。
                                mIsRunningSuccess = false;
                                new ObdResetCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new LineFeedOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new SpacesOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new SpacesOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new TimeoutCommand(125).run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new SelectProtocolCommand(ObdProtocols.AUTO).run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                                new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);
                              *//*  new ClearDTCCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                                Thread.sleep(200);*/
                                mIsRunningSuccess = true;
                            } catch (Exception e) {
                                mIsRunningSuccess = false;
                                LogUtils.i("在新线程中重置命令异常:: " + e.getMessage());
                            }
                        }
                    });
                    newThread.start();
                    newThread.join(DELAY_FIFTEEN_SECOND);
                    LogUtils.i("线程唤醒以检查重置命令状态  i.e  :: " + Thread.currentThread().getId() + ",  mIsRunningSuccess :: " + mIsRunningSuccess);
                    isSockedConnected = mIsRunningSuccess;
                } catch (Exception e) {
                    LogUtils.i(" 重置命令异常  :: " + e.getMessage());
                    isSockedConnected = false;
                }
            }
            if (mSocket != null && mSocket.isConnected() && isSockedConnected) {
                setConnection();
            } else {
                if (mSupportedPids != null && mSupportedPids.length == 32) {

                    if ((mSupportedPids[12] != PID_STATUS_SUCCESS) || (mSupportedPids[11] != PID_STATUS_SUCCESS)) {
                        // 不支持速度PID
                        sendBroadcast(ACTION_OBD_CONNECTION_STATUS_CAR, getString(R.string.unable_to_connect));
                        return;
                    }
                }
                sendBroadcast(ACTION_OBD_CONNECTION_STATUS_CAR, getString(R.string.obd2_adapter_not_responding));
            }
        }
    }


    /**
     * 一旦OBD-2连接，此方法将执行以连续获取数据，直到OBD断开或服务停止。
     */
    private void executeCommand() {
        LogUtils.i("执行命令线程是 :: " + Thread.currentThread().getId());
        TripRecordCar TripTwoRecord = TripRecordCar.getTripTwoRecode(this);
        ArrayList<ObdCommand> commands = (ArrayList<ObdCommand>) ObdConfiguration.getmObdCommands().clone();
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                LogUtils.i("命令运行:: " + command.getName());
                command.run(mSocket.getInputStream(), mSocket.getOutputStream());
                LogUtils.i("结果是:: " + command.getFormattedResult() + " :: name is :: " + command.getName());
                TripTwoRecord.updateTrip(command.getName(), command);
                if (mIsFaultCodeRead) {
                    try {
                        TroubleCodesCommand troubleCodesCommand = new TroubleCodesCommand();
                        troubleCodesCommand.run(mSocket.getInputStream(), mSocket.getOutputStream());
                        TripTwoRecord.updateTrip(troubleCodesCommand.getName(), troubleCodesCommand);
                        mIsFaultCodeRead = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                LogUtils.i("执行命令异常  :: " + e.getMessage());
                if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                    LogUtils.i("命令异常  :: " + e.getMessage());
                    setDisconnection();
                }
            }
        }
        if (mIntent == null)
            mIntent = new Intent(ACTION_READ_OBD_REAL_TIME_DATA_CAR);
        sendBroadcast(mIntent);

        // 退出循环意味着连接丢失，所以将连接状态设置为false
        isConnected = false;
    }

    /**
     * 发送带有特定动作和数据的广播
     *
     * @param action 特定动作
     * @param data   数据
     */
    private void sendBroadcast(final String action, String data) {
        final Intent intent = new Intent(action);
        intent.putExtra(ObdTwoReaderService.INTENT_OBD_EXTRA_DATA_CAR, data);
        sendBroadcast(intent);
    }

    /**
     * 发送带有特定操作的广播
     *
     * @param action 特定动作
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(true);
        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
        LogUtils.i("Service Created :: ");
    }


    /**
     * @return 检查此设备是否支持蓝牙
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected boolean initiateConnection() {
        boolean isBlueToothSupported = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
        boolean isInitialized = initialize();
        if (!isBlueToothSupported || !isInitialized) {
            Toast.makeText(this, getString(R.string.bluetooth_unsupported), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * @return 检查此设备中是否有可用的BluetoothServices
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                return false;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter != null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeSocket();
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
        TripRecordCar.getTripTwoRecode(this).clear();
    }

    /**
     * 关闭蓝牙插座
     */
    private void closeSocket() {
        LogUtils.i("socket closed :: ");
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                LogUtils.i("socket closing failed :: ");
            }
        }
    }

    /**
     * @return 检查蓝牙是否开启
     */
    public boolean isEnable() {
        if (mBluetoothAdapter == null)
            return false;
        return mBluetoothAdapter.isEnabled();

    }

    /**
     * @return 无需用户交互即可启用蓝牙
     */
    public boolean enableBlutooth() {
        if (mBluetoothAdapter != null)
            return mBluetoothAdapter.enable();
        return false;
    }

    @Override
    public boolean stopService(Intent name) {
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
        return super.stopService(name);

    }

    /**
     * 通过应用程序设置设备断开状态的方法
     */
    public void setDisconnection() {
        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(false);
        isConnected = false;
        closeSocket();
        LogUtils.i("socket disconnected :: ");
        sendBroadcast(ACTION_OBD_CONNECTION_STATUS_CAR, getString(R.string.connect_lost));
    }

    /**
     * 通过应用程序设置设备连接状态的方法
     */
    private void setConnection() {
        ObdPreferences.get(getApplicationContext()).setIsOBDconnected(true);
        isConnected = true;
        sendBroadcast(ACTION_OBD_CONNECTION_STATUS_CAR, getString(R.string.obd_connected));
    }

    /**
     * 创建用于在onBind方法中返回的绑定器实例
     */
    public class LocalBinder extends Binder {
        public ObdTwoReaderService getService() {
            return ObdTwoReaderService.this;
        }
    }
}