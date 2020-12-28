package com.example.obdandroid.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.service.BltService;
import com.example.obdandroid.service.BlueToothReceiver;
import com.example.obdandroid.ui.adapter.BluetoothSimpleAdapter;
import com.example.obdandroid.ui.entity.BluRxBean;
import com.example.obdandroid.ui.entity.BluetoothEntity;
import com.example.obdandroid.utils.BltManager;
import com.example.obdandroid.utils.BluetoothManager;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.factory.ThreadPoolProxyFactory;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static android.app.Activity.RESULT_OK;

/**
 * 作者：Jealous
 * 日期：2020/12/23 0023
 * 描述：
 */
public class HomeFragment extends BaseFragment {
    private Context context;
    private TitleBar titleBar;
    private Button scan;
    private TextView localblumessage;
    private Button sousuo;
    private TextView bluemessage;
    private RecyclerView recycleBluetooth;

    /**
     * 操作模式
     */
    public enum MODE {
        OFFLINE,//< OFFLINE mode
        ONLINE,    //< ONLINE mode
    }

    private BluetoothAdapter bluetoothadapter;
    private List<BluetoothEntity> list;
    private List<BluetoothDevice> listdevice;
    private BlueToothReceiver blueToothReceiver = new BlueToothReceiver();
    private int connectsuccess = 12;//连接成功
    private BluetoothSimpleAdapter adapter;
    private DialogUtils dialogUtils;

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
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
        scan = getView(R.id.scan);
        localblumessage = getView(R.id.localblumessage);
        sousuo = getView(R.id.sousuo);
        bluemessage = getView(R.id.bluemessage);
        recycleBluetooth = getView(R.id.recycleBluetooth);
        titleBar.setTitle("汽车扫描");
        dialogUtils = new DialogUtils(context);
        context.registerReceiver(blueToothReceiver, blueToothReceiver.makeFilter());
        EventBus.getDefault().register(this);
        BltManager.getInstance().initBltManager(context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleBluetooth.setLayoutManager(manager);
        adapter = new BluetoothSimpleAdapter(context);
        initBluetooth();
        init();
        sousuo.setOnClickListener(v -> {
            if (!getBlueIsEnable()) {
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enabler, 1);
            } else {
                startScan();
            }
        });

        scan.setOnClickListener(v -> {
            //获取本地蓝牙名称
            String name = bluetoothadapter.getName();
            //获取本地蓝牙地址
            String address = bluetoothadapter.getAddress();
            localblumessage.setText("本地蓝牙名称:" + name + "本地蓝牙地址:" + address);
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
                LogE("333333");
            }
        });
    }

    /**
     * 初始化蓝牙设备
     */
    private void initBluetooth() {
        bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothadapter == null) {
            Toast.makeText(context, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 组件初始化
     */
    private void init() {
        list = new ArrayList<>();
        listdevice = new ArrayList<>();
        adapter.setClickCallBack((entity, position) -> {
            if (entity.getState().equals("已配对")) {
                dialogUtils.showProgressDialog("正在连接");
                ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BluetoothManager.connect(listdevice.get(position), new BluetoothManager.ConnBluetoothSocketListener() {
                                @Override
                                public void connectMsg(int code, String msg) {

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else {
                try {
                    //如果想要取消已经配对的设备，只需要将creatBond改为removeBond
                    Method method = BluetoothDevice.class.getMethod("createBond");
                    Log.e(context.getPackageName(), "开始配对");
                    method.invoke(listdevice.get(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 开始扫描蓝牙
     */
    private void startScan() {
        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivity(enabler);
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            bluemessage.setText("");
            listdevice.clear();
        }
        ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(() -> BltService.getInstance().startBluService());// 开启蓝牙服务端
        Acp.getInstance(context).request(new AcpOptions.Builder()
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .build(), new AcpListener() {
            @Override
            public void onGranted() {
                if (bluetoothadapter.isDiscovering()) {
                    bluetoothadapter.cancelDiscovery();
                }
                bluetoothadapter.startDiscovery();
            }

            @Override
            public void onDenied(List<String> permissions) {

            }
        });
    }

    /**
     * @return 判断蓝牙是否开启
     */
    public boolean getBlueIsEnable() {
        return bluetoothadapter.isEnabled();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                startScan();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(blueToothReceiver);
        EventBus.getDefault().unregister(this);
    }

    /**
     * EventBus 异步
     * 1:找到设备
     * 2：扫描完成
     * 3：开始扫描
     * 4.配对成功
     * 12:连接成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(BluRxBean bluRxBean) {
        switch (bluRxBean.getId()) {
            case 1:
                listdevice.add(bluRxBean.getBluetoothDevice());
                LogE("设备名称:" + bluRxBean.getBluetoothDevice().getName());
                // 添加到列表
                bluemessage.append(bluRxBean.getBluetoothDevice().getName() + ":"
                        + bluRxBean.getBluetoothDevice().getAddress() + "\n");
                BluetoothEntity entity = new BluetoothEntity();
                entity.setBluetoothDeviceName(bluRxBean.getBluetoothDevice().getName());
                if (bluRxBean.getBluetoothDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                    entity.setState("未配对");
                } else {
                    entity.setState("已配对");
                }
                list.add(entity);
                adapter.setList(list);
                recycleBluetooth.setAdapter(adapter);
                break;
            case 2:
                dialogUtils.dismiss();
                TipDialog.show(context, "扫描完成", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                break;
            case 12:
                dialogUtils.dismiss();
                TipDialog.show(context, "连接成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                break;
            case 3:
                dialogUtils.dismiss();
                TipDialog.show(context, "正在扫描", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                break;
            default:
                break;
        }
    }

    /***
     * 蓝牙连接代码,项目中连接会使用封装的工具类，在这里提取重写
     */
    private void connect(BluetoothDevice bluetoothDevice) {
        try {
            mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(Constant.SPP_UUID);
            if (mBluetoothSocket != null) {
                MainApplication.bluetoothSocket = mBluetoothSocket;
                if (bluetoothadapter.isDiscovering()) {
                    bluetoothadapter.cancelDiscovery();
                }
                if (!mBluetoothSocket.isConnected()) {
                    mBluetoothSocket.connect();
                }
                EventBus.getDefault().post(new BluRxBean(connectsuccess, bluetoothDevice));
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                mBluetoothSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * 配对成功后的蓝牙套接字
     */
    private BluetoothSocket mBluetoothSocket;
}