package com.example.obdandroid.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.BluetoothSimpleAdapter;
import com.example.obdandroid.ui.entity.BluetoothDeviceEntity;
import com.example.obdandroid.utils.ToastUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.obdandroid.config.Constant.REQUEST_ENABLE_BT;

/**
 * 作者：Jealous
 * 日期：2020/12/29 0029
 * 描述：蓝牙设备列表
 */
public class BluetoothDeviceActivity extends BaseActivity {
    private RecyclerView recycleBluetoothDevice;
    private BluetoothSimpleAdapter simpleAdapter;

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
        Context context = this;
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        recycleBluetoothDevice = findViewById(R.id.recycle_BluetoothDevice);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleBluetoothDevice.setLayoutManager(manager);
        simpleAdapter = new BluetoothSimpleAdapter(context);
        checkBluetooth();
        initBlueTooth();
        simpleAdapter.setClickCallBack(device -> {
            Intent intent = new Intent();
            intent.putExtra("bluetoothDeviceNumber", device);
            setResult(97, intent);
            finish();
        });
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
            for (BluetoothDevice bluetoothDevice : devices) {bluetoothDevice.getBondState();
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