package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.service.BtCommService;
import com.example.obdandroid.service.CommService;
import com.example.obdandroid.ui.adapter.HomeAdapter;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.DividerGridItemDecoration;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.ToastUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.obdandroid.config.Constant.BT_ADDRESS_KEY;
import static com.example.obdandroid.config.Constant.BT_NAME_KEY;
import static com.example.obdandroid.config.Constant.CONNECT_BT_KEY;
import static com.example.obdandroid.config.Constant.DEVICE_ADDRESS;
import static com.example.obdandroid.config.Constant.DEVICE_NAME;
import static com.example.obdandroid.config.Constant.DISPLAY_UPDATE_TIME;
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
public class HomeFragment extends BaseFragment {
    private Context context;
    private TitleBar titleBar;
    private BluetoothAdapter bluetoothadapter;
    private List<HashMap<String, Object>> blueList;
    private int yourChoice;
    private SPUtil spUtil;
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
        mConnectedDeviceName = spUtil.getString(DEVICE_NAME, "");
        mConnectedDeviceAddress = spUtil.getString(DEVICE_ADDRESS, "");
        DialogUtils dialogUtils = new DialogUtils(context);
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
        setDefaultMode(mode);
        if (!TextUtils.isEmpty(mConnectedDeviceAddress)) {
            connectBtDevice(mConnectedDeviceAddress);
        } else {
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
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle("已配对蓝牙设备");
        singleChoiceDialog.setIcon(R.drawable.icon_bluetooth);
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                (dialog, which) -> yourChoice = which);
        singleChoiceDialog.setPositiveButton("确定",
                (dialog, which) -> {
                    if (yourChoice != -1) {
                        connectBtDevice(itemsAddress[yourChoice]);
                    }
                });
        singleChoiceDialog.show();
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
     * 启动与所选蓝牙设备的连接
     *
     * @param address bluetooth device address
     */
    private void connectBtDevice(String address) {
        // 获取BluetoothDevice对象
        BluetoothDevice device = bluetoothadapter.getRemoteDevice(address);
        /*
         * 尝试连接到设备
         * BT通讯服务的成员对象
         */
        CommService mCommService = new BtCommService(context, mHandler);
        mCommService.connect(device, true);
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

    }

    /**
     * 处理蓝牙连接丢失...
     */
    private void onDisconnect() {
        mode = MODE.OFFLINE;
        titleBar.setLeftTitle("OFFLINE");
        titleBar.setRightIcon(R.drawable.action_disconnect);
        spUtil.put(CONNECT_BT_KEY, "OFFLINE");
    }

    private void setDefaultMode(MODE mode) {
        if (mode.equals(MODE.OFFLINE)) {
            titleBar.setLeftTitle("OFFLINE");
            titleBar.setRightIcon(R.drawable.action_disconnect);
            spUtil.put(CONNECT_BT_KEY, "OFFLINE");
        }
    }
}