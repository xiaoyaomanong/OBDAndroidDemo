package com.example.obdandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.listener.CallEndListener;
import com.example.obdandroid.ui.adapter.VehicleCheckAdapter;
import com.example.obdandroid.ui.entity.MessageCheckEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CircleWelComeView;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.obdCommand.control.PendingTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.PermanentTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.TroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.protocol.EchoOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.LineFeedOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ResetTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SelectProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SpacesOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.TimeoutCommand;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;
import com.sohrab.obd.reader.trip.OBDTripEntity;
import com.sohrab.obd.reader.trip.TripRecordCar;
import com.sohrab.obd.reader.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.addRemind_URL;
import static com.example.obdandroid.config.APIConfig.addTestRecord_URL;
import static com.example.obdandroid.config.APIConfig.getVehicleInfoById_URL;
import static com.example.obdandroid.config.APIConfig.reduceAndCumulativeFrequency_URL;

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
    private BluetoothSocket mSocket;
    private boolean mIsFaultCodeRead = true;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 12) {
                if (!TextUtils.isEmpty(ObdPreferences.get(context).getBlueToothDeviceAddress())) {
                    connectBluetooth(ObdPreferences.get(context).getBlueToothDeviceAddress(), 11);
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

            } else if (msg.arg1 == 11) {
                circleView.start();
                btStart.setText("开始检测");
                tvConnectObd.setVisibility(View.GONE);
                btStart.setEnabled(false);
                executeCommand();
            } else if (msg.arg1 == 10) {
                clearCodes(mSocket);
            } else {
                throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };


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
        recycleCheckContent = getView(R.id.recycleCheckContent);
        tvConnectObd = getView(R.id.tv_connect_obd);
        layoutCar = getView(R.id.layout_Car);
        ivCarLogo = getView(R.id.ivCarLogo);
        tvAutomobileBrandName = getView(R.id.tvAutomobileBrandName);
        tvModelName = getView(R.id.tvModelName);
        spUtil = new SPUtil(context);
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
                new Thread(() -> circleView.start()).start();
                Message message = new Message();
                message.arg1 = 12;
                handler.sendMessage(message);
            } else {
                showTipDialog("请连接OBD设备", TipDialog.TYPE_WARNING);
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
                if (titleBar.getRightTitle().toString().equals("清除故障")) {
                    connectBluetooth(ObdPreferences.get(context).getBlueToothDeviceAddress(), 10);
                }
            }
        });
    }

    private void connectBluetooth(String address, int arg) {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        try {
            mSocket = (BluetoothSocket) device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class}).invoke(device, 1);
        } catch (Exception e) {
            LogE("createInsecureRfcommSocket failed");
        }
        try {
            Thread.sleep(500);
            mSocket.connect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        executeCommandCheck(address, arg);
    }

    /**
     * 一旦OBD-2连接，此方法将执行以连续获取数据，直到OBD断开或服务停止。
     */
    private void executeCommandCheck(String address, int arg) {
        boolean isSockedConnected = mSocket.isConnected();
        if (!isSockedConnected) {
            connectBluetooth(address, arg);
        }
        if (isSockedConnected) {
            layoutCar.setVisibility(View.VISIBLE);
            final Thread newThread = new Thread(() -> {
                try {
                    // 此线程是必需的，因为在Headunit中命令.run方法无限块，因此，线程的最长寿命为15秒，这样就可以处理块了。
                    new ObdResetCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                    new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                    new LineFeedOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                    new SpacesOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                    new TimeoutCommand(125).run(mSocket.getInputStream(), mSocket.getOutputStream());
                    new SelectProtocolCommand(ObdProtocols.AUTO).run(mSocket.getInputStream(), mSocket.getOutputStream());
                    new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                    Thread.sleep(100);
                    Message message = new Message();
                    message.arg1 = arg;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    LogUtils.i("在新线程中重置命令异常:: " + e.getMessage());
                }
            });
            newThread.start();
        }
    }

    private void executeCommand() {
        LogE("在新线程中执行reset命令 :: " + Thread.currentThread().getId());
        TripRecordCar TripTwoRecord = TripRecordCar.getTripTwoRecode(context);
        TripTwoRecord.getTripMap().clear();
        ArrayList<ObdCommand> commands = (ArrayList<ObdCommand>) ObdConfiguration.getmObdCommands().clone();
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                command.run(mSocket.getInputStream(), mSocket.getOutputStream());
                LogE("结果是: " + command.getFormattedResult() + " :: name is :: " + command.getName());
                TripTwoRecord.updateTrip(command.getName(), command);
                if (mIsFaultCodeRead) {
                    try {
                        ModifiedTroubleCodesObdCommand troubleCodesCommand = new ModifiedTroubleCodesObdCommand();//故障代码
                        troubleCodesCommand.run(mSocket.getInputStream(), mSocket.getOutputStream());
                        TripTwoRecord.updateTrip(troubleCodesCommand.getName(), troubleCodesCommand);

                        ModifiedPermanentTroubleCodesCommand permanentTroubleCodesCommand = new ModifiedPermanentTroubleCodesCommand();//永久故障码
                        permanentTroubleCodesCommand.run(mSocket.getInputStream(), mSocket.getOutputStream());
                        TripTwoRecord.updateTrip(permanentTroubleCodesCommand.getName(), permanentTroubleCodesCommand);

                        ModifiedPendingTroubleCodesCommand pendingTroubleCodesCommand = new ModifiedPendingTroubleCodesCommand();//未解决故障码
                        pendingTroubleCodesCommand.run(mSocket.getInputStream(), mSocket.getOutputStream());
                        TripTwoRecord.updateTrip(pendingTroubleCodesCommand.getName(), pendingTroubleCodesCommand);
                        titleBar.setRightTitle("清除故障");
                        mIsFaultCodeRead = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                LogE("执行命令异常  :: " + e.getMessage());
                if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                    LogE("命令异常  :: " + e.getMessage());
                }
            }
        }

        if (circleView.isDiffuse()) {
            circleView.stop();
            btStart.setText("检测完成");
            btStart.setEnabled(true);
        }
        showResult(TripTwoRecord.getTripMap());
        addTestRecord(spUtil.getString("vehicleId", ""), JSON.toJSONString(TripTwoRecord.getOBDJson()), getUserId(), getToken());
        reduceAndCumulativeFrequency(getToken(), getUserId());
        addRemind(getUserId(), addJsonContent(TripTwoRecord.getOBDJson()), getToken());

    }

    public static class ModifiedTroubleCodesObdCommand extends TroubleCodesCommand {
        @Override
        public String getResult() {
            //输出中删除不必要的响应，因为这会导致错误的错误代码
            return rawData.replace("SEARCHING...", "").replace("NODATA", "");
        }
    }

    public static class ModifiedPermanentTroubleCodesCommand extends PermanentTroubleCodesCommand {
        @Override
        public String getResult() {
            //输出中删除不必要的响应，因为这会导致错误的错误代码
            return rawData.replace("SEARCHING...", "").replace("NODATA", "");
        }
    }

    public static class ModifiedPendingTroubleCodesCommand extends PendingTroubleCodesCommand {
        @Override
        public String getResult() {
            //输出中删除不必要的响应，因为这会导致错误的错误代码
            return rawData.replace("SEARCHING...", "").replace("NODATA", "");
        }
    }

    /**
     * @param socket 蓝牙套接字
     *               清除故障码
     */
    private void clearCodes(BluetoothSocket socket) {
        boolean conn = socket.isConnected();
        LogE("连接：" + conn);
        try {
            LogE("测试复位=====尝试重置");
            new ObdResetCommand().run(socket.getInputStream(), socket.getOutputStream());
            LogE("开始清除");
            ResetTroubleCodesCommand clear = new ResetTroubleCodesCommand();
            clear.run(socket.getInputStream(), socket.getOutputStream());
            String result = clear.getFormattedResult();
            LogE("重置结果: " + result);
            if (!TextUtils.isEmpty(result)) {
                titleBar.setRightTitle("");
                Intent intent = new Intent("com.android.Record");//创建发送广播的Action
                localBroadcastManager.sendBroadcast(intent);  //发送本地广播
            }
        } catch (Exception e) {
            LogE("建立连接时出错。 -> " + e.getMessage());
        }
    }

    public void closeSocket(BluetoothSocket sock) {
        if (sock != null)
            // close socket
            try {
                sock.close();
            } catch (IOException e) {
                LogE(e.getMessage());
            }
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
                LogE("减少使用次数和累计使用次数:" + response);
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
            if (spUtil.getString(Constant.CONNECT_BT_KEY, "").equals("ON")) {
                adapter.setMsg("OBD设备已连接,请进行检测");
            } else {
                adapter.setMsg("请连接OBD设备,进行检测");
            }
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
                    localBroadcastManager.sendBroadcast(intent);  //发送本地广播
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
        //解绑
        localBroadcastManager.unregisterReceiver(receiver);
    }
}