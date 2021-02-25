package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.ui.adapter.VehicleCheckAdapter;
import com.example.obdandroid.ui.entity.MessageCheckEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CircleWelComeView;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.SPUtil;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.service.ObdTwoReaderService;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;
import com.sohrab.obd.reader.trip.OBDTripEntity;
import com.sohrab.obd.reader.trip.TripRecordCar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.reduceAndCumulativeFrequency_URL;
import static com.example.obdandroid.config.APIConfig.addRemind_URL;
import static com.example.obdandroid.config.APIConfig.addTestRecord_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;
import static com.sohrab.obd.reader.constants.DefineObdTwoReader.ACTION_OBD_CONNECTION_STATUS_CAR;
import static com.sohrab.obd.reader.constants.DefineObdTwoReader.ACTION_READ_OBD_REAL_TIME_DATA_CAR;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：车辆检测
 */
public class VehicleCheckFragment extends BaseFragment {
    private Context context;
    private CircleWelComeView circleView;
    private TextView btStart;
    private VehicleCheckAdapter adapter;
    private RecyclerView recycleCheckContent;
    private SPUtil spUtil;
    private TextView tvConnectObd;
    private LinearLayout layoutCar;
    private ImageView ivCarLogo;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private LocalBroadcastManager localBroadcastManager;
    private CarReceiver receiver;

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
        circleView = getView(R.id.circleView);
        btStart = getView(R.id.btStart);
        recycleCheckContent = getView(R.id.recycleCheckContent);
        tvConnectObd = getView(R.id.tv_connect_obd);
        layoutCar = getView(R.id.layout_Car);
        ivCarLogo = getView(R.id.ivCarLogo);
        tvAutomobileBrandName = getView(R.id.tvAutomobileBrandName);
        tvModelName = getView(R.id.tvModelName);
        spUtil = new SPUtil(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_READ_OBD_REAL_TIME_DATA_CAR);
        intentFilter.addAction(ACTION_OBD_CONNECTION_STATUS_CAR);
        context.registerReceiver(mObdReaderReceiver, intentFilter);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleCheckContent.setLayoutManager(manager);
        //获取实例
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        initReceiver();
        /*
         *  配置obd：在arrayList中添加所需命令并设置为ObdConfiguration。
         *  如果您没有设置任何命令或传递null，那么将请求所有命令OBD command。   *
         */
        ObdConfiguration.setmObdCommands(context, null);//传递null意味着我们现在正在执行所有OBD命令，但是您应该添加必需的命令以便像上面注释的行一样快速检索。
        // 设定每升汽油价格，以便计算汽油成本。默认值为7$/l
        float gasPrice = 7; // 每升，你应该根据你的要求初始化。
        ObdPreferences.get(context).setGasPrice(gasPrice);
        getVehicleInfoById(getToken(), spUtil.getString("vehicleId", ""));
        btStart.setOnClickListener(v -> {
            if (spUtil.getString(Constant.CONNECT_BT_KEY, "").equals("ON")) {
                btStart.setText("开始检测");
                tvConnectObd.setVisibility(View.GONE);
                circleView.start();
                if (!TextUtils.isEmpty(ObdPreferences.get(context).getBlueToothDeviceAddress())) {
                    connectBtDevice(ObdPreferences.get(context).getBlueToothDeviceAddress());
                } else {
                    new Handler().postDelayed(() -> {
                        if (circleView.isDiffuse()) {
                            circleView.stop();
                            btStart.setText("检测失败");
                        }
                        showResult(null);
                        showToast(getString(R.string.text_bluetooth_error_connecting));
                    }, 3000);
                }
            } else {
                showTipDialog("请连接OBD设备", TipDialog.TYPE_WARNING);
            }
        });
        //addRemind(getUserId(), addJsonContent(TripRecordCar.getTripTwoRecode(context).getOBDJson()), getToken());
        circleView.setCallEndListener(() -> LogE("333333"));
    }

    /**
     * @param token     用户Token
     * @param appUserId APP用户ID
     *                  减少使用次数和累计使用次数
     */
    private void reduceAndCumulativeFrequency(String token, String appUserId) {
        OkHttpUtils.post().url(SERVER_URL + reduceAndCumulativeFrequency_URL).
                addParam("token", token).
                addParam("appUserId", appUserId).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                LogE("减少使用次数和累计使用次数:"+response);
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {

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
                }
            }
        });
    }

    /**
     * @param messages 检测数据
     *                 展示OBD检测数据
     */
    public void showResult(List<OBDTripEntity> messages) {
        initAinm();
        if (adapter == null) {
            adapter = new VehicleCheckAdapter(context);
            adapter.setList(messages);
            getHoldingActivity().runOnUiThread(() -> recycleCheckContent.setAdapter(adapter));
        } else {
            getHoldingActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }
    }

    /**
     * @param vehicleId 车辆ID
     * @param testData  检测数据
     * @param appUserId APP用户ID
     * @param token     用户Token
     *                  添加检测信息
     */
    private void addTestRecord(String vehicleId, String testData, String appUserId, String token) {
        OkHttpUtils.post().url(SERVER_URL + addTestRecord_URL).
                addParam("vehicleId", vehicleId).
                addParam("testData", testData).
                addParam("appUserId", appUserId).
                addParam("token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    Intent intent = new Intent("com.android.Record");//创建发送广播的Action
                    localBroadcastManager.sendBroadcast(intent);                               //发送本地广播
                }
            }
        });
    }

    /**
     * @param appUserId APP用户ID
     * @param content   消息内容
     * @param token     用户Token
     */
    private void addRemind(String appUserId, String content, String token) {
        OkHttpUtils.post().url(SERVER_URL + addRemind_URL).
                addParam("appUserId", appUserId).
                addParam("remindType", "2").
                addParam("content", content).
                addParam("title", "全车检测自动生成体检报告").
                addParam("token", token).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ResultEntity entity = JSON.parseObject(response, ResultEntity.class);
                if (entity.isSuccess()) {
                    Intent intent = new Intent("com.android.Remind");//创建发送广播的Action
                    localBroadcastManager.sendBroadcast(intent);
                }
            }
        });
    }

    private String addJsonContent(OBDJsonTripEntity tripEntity) {
        MessageCheckEntity entity = new MessageCheckEntity();
        entity.setCreateTime(AppDateUtils.getTodayDateTime());
        if (TextUtils.isEmpty(tripEntity.getFaultCodes())) {
            entity.setContent("通过检测,无故障码,您的车辆很健康!");
            entity.setDetails("");
        } else {
            entity.setContent("通过检测,发现你的车辆有故障，请及时处理!");
            entity.setDetails(JSON.toJSONString(tripEntity));
        }
        return JSON.toJSONString(entity);
    }

    private void initAinm() {
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.left);
        //得到一个LayoutAnimationController对象；
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        //设置控件显示的顺序；
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        //设置控件显示间隔时间；
        lac.setDelay(0.2f);
        //为ListView设置LayoutAnimationController属性；
        recycleCheckContent.setLayoutAnimation(lac);
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
        //启动服务，该服务将在后台执行连接，并执行命令，直到您停止
        Intent intent = new Intent(context, ObdTwoReaderService.class);
        intent.putExtra("device", bluetoothDevice);
        intent.putExtra("isConnected", ObdPreferences.get(context).getBlueToothDeviceConnect());
        context.startService(intent);
    }

    /**
     * 处理建立的蓝牙连接...
     */
    private void onConnect(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        layoutCar.setVisibility(View.VISIBLE);
    }

    /**
     * 处理蓝牙连接断开
     */
    private void onDisconnect() {
        showResult(null);
    }

    /**
     * 接收OBD连接状态和实时数据的广播接收器
     */
    private final BroadcastReceiver mObdReaderReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_OBD_CONNECTION_STATUS_CAR)) {
                String connectionStatusMsg = intent.getStringExtra(ObdTwoReaderService.INTENT_OBD_EXTRA_DATA_CAR);
                if (connectionStatusMsg.equals(getString(R.string.obd_connected))) {
                    //OBD连接在OBD连接之后做什么
                    onConnect(connectionStatusMsg);
                } else if (connectionStatusMsg.equals(getString(R.string.connect_lost))) {
                    //OBD断开连接断开后做什么
                    onDisconnect();
                }
            } else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA_CAR)) {
                TripRecordCar tripRecordCar = TripRecordCar.getTripTwoRecode(context);
                if (circleView.isDiffuse()) {
                    circleView.stop();
                    btStart.setText("检测完成");
                }
                showResult(tripRecordCar.getTripMap());
                addTestRecord(spUtil.getString("vehicleId", ""), JSON.toJSONString(tripRecordCar.getOBDJson()), getUserId(), getToken());
                reduceAndCumulativeFrequency(getToken(), getUserId());
                addRemind(getUserId(), addJsonContent(tripRecordCar.getOBDJson()), getToken());
            }
        }
    };

    private void showTipDialog(String msg, int type) {
        TipDialog.show(context, msg, TipDialog.TYPE_ERROR, type);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter("com.android.ObdCar");
        receiver = new CarReceiver();
        //绑定
        localBroadcastManager.registerReceiver(receiver, intentFilter);

    }

    private class CarReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String vehicleId = intent.getStringExtra(Intent.EXTRA_TEXT);
            getVehicleInfoById(getToken(), vehicleId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销接收器
        context.unregisterReceiver(mObdReaderReceiver);
        //解绑
        localBroadcastManager.unregisterReceiver(receiver);
        //停止服务
        context.stopService(new Intent(context, ObdTwoReaderService.class));
        // 这将停止后台线程，如果任何运行立即。
        ObdPreferences.get(context).setServiceRunningStatus(false);
    }
}