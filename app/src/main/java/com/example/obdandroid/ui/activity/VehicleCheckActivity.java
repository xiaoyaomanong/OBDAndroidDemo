package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.adapter.VehicleCheckAdapter;
import com.example.obdandroid.ui.entity.AddTestRecordEntity;
import com.example.obdandroid.ui.entity.MessageCheckEntity;
import com.example.obdandroid.ui.entity.ResultEntity;
import com.example.obdandroid.ui.entity.VehicleInfoEntity;
import com.example.obdandroid.ui.view.CircleWelComeView;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.ExceptionHandler;
import com.example.obdandroid.utils.SPUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ResetTroubleCodesCommand;
import com.example.obdandroid.config.CheckRecord;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;
import com.sohrab.obd.reader.trip.OBDTripEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * 日期：2021/3/4 0004
 * 描述：
 */
public class VehicleCheckActivity extends BaseActivity {
    private Context context;
    private TitleBar titleBar;
    private CircleWelComeView circleView;
    private TextView btStart;
    private VehicleCheckAdapter adapter;
    private RecyclerView recycleCheckContent;
    private SPUtil spUtil;
    private LinearLayout layoutCar;
    private ImageView ivCarLogo;
    private TextView tvAutomobileBrandName;
    private TextView tvModelName;
    private TextView tvOBDState;
    private LinearLayout layoutLook;
    private ImageView ivNext;
    private TextView tvCommandResult;
    private LocalBroadcastManager localBroadcastManager;
    @SuppressLint("HandlerLeak")
    private CheckRecord tripRecord;
    private static final int COMPLETED = 0;
    private static final int COMPLETES = 1;
    private static final int COMPLETEO = 2;
    private int size;
    private DialogUtils dialogUtils;

    private File saveSpacePath;
    private File localErrorSave;
    private StringBuilder sb = new StringBuilder();
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == COMPLETED) {
                if (circleView.isDiffuse()) {
                    circleView.stop();
                    btStart.setText("检测完成");
                    btStart.setEnabled(true);
                }
                titleBar.setRightTitle("清除故障");
                layoutLook.setVisibility(View.VISIBLE);
                tvCommandResult.setText("生成检测报告已生成,请查看详细内容!");
                ivNext.setVisibility(View.VISIBLE);
                tvOBDState.setVisibility(View.GONE);
                showResult(tripRecord.getTripMap());
                addTestRecord(spUtil.getString("vehicleId", ""), JSON.toJSONString(tripRecord.getOBDJson()), getUserId(), getToken());
                reduceAndCumulativeFrequency(getToken(), getUserId());
                layoutLook.setOnClickListener(v -> {
                    Intent intent = new Intent(context, CheckReportActivity.class);
                    intent.putExtra("data", tripRecord);
                    startActivity(intent);
                });
            }
            if (msg.what == COMPLETES) {
                showTipDialog("故障码清除成功", TipDialog.TYPE_FINISH);
                titleBar.setRightTitle("");
                dialogUtils.dismiss();
                Intent intent = new Intent("com.android.Record");//创建发送广播的Action
                localBroadcastManager.sendBroadcast(intent);  //发送本地广播
            }
            if (msg.what == COMPLETEO) {
                double current = (double) msg.obj;
                double per = (current / size) * 100.0;
                btStart.setText(String.format("%.2f", per) + "%");
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_vehicle_check;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        super.initView();
        context = this;
        titleBar = getView(R.id.titleBar);
        circleView = getView(R.id.circleView);
        btStart = getView(R.id.btStart);
        recycleCheckContent = getView(R.id.recycleCheckContent);
        layoutCar = getView(R.id.layout_Car);
        ivCarLogo = getView(R.id.ivCarLogo);
        tvAutomobileBrandName = getView(R.id.tvAutomobileBrandName);
        tvModelName = getView(R.id.tvModelName);
        layoutLook = findViewById(R.id.layoutLook);
        ivNext = findViewById(R.id.ivNext);
        tvOBDState = findViewById(R.id.tvOBDState);
        tvCommandResult = findViewById(R.id.tvCommandResult);
        spUtil = new SPUtil(context);
        dialogUtils = new DialogUtils(context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recycleCheckContent.setLayoutManager(manager);
        //获取实例
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        CheckRecord.getTriRecode(context, getToken()).clear();
        tripRecord = CheckRecord.getTriRecode(context, getToken());
        // 设定每升汽油价格，以便计算汽油成本。默认值为7$/l
        float gasPrice = 7; // 每升，你应该根据你的要求初始化。
        ObdPreferences.get(context).setGasPrice(gasPrice);
        getVehicleInfoById(getToken(), spUtil.getString("vehicleId", ""));
        boolean isConn = MainApplication.getBluetoothSocket().isConnected();
        if (isConn) {
            tvOBDState.setText("OBD已连接,请进行检测");
        } else {
            tvOBDState.setText("OBD连接失败");
        }
        btStart.setOnClickListener(v -> {
            if (isConn) {
                circleView.start();
                btStart.setText("开始检测");
                tvOBDState.setVisibility(View.VISIBLE);
                tvOBDState.setText("正在检测中....");
                btStart.setEnabled(false);
                layoutCar.setVisibility(View.VISIBLE);
                layoutLook.setVisibility(View.GONE);
                new Thread(() -> executeCommand(MainApplication.getBluetoothSocket())).start();
            } else {
                showTipDialog("请连接OBD设备", TipDialog.TYPE_WARNING);
            }
        });
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                setResult(101, new Intent());
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                if (titleBar.getRightTitle().toString().equals("清除故障")) {
                    if (MainApplication.getBluetoothSocket().isConnected()) {
                        dialogUtils.showProgressDialog("正在清除故障码");
                        new Thread(() -> clearCodes(MainApplication.getBluetoothSocket())).start();
                    }
                }
            }
        });
        // initConfig();
    }

    private void executeCommand(BluetoothSocket socket) {
        tripRecord.getTripMap().clear();
        ArrayList<ObdCommand> commands = ObdConfiguration.getObdCommands(ModeTrim.MODE_01);
        size = commands.size();
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                command.run(socket.getInputStream(), socket.getOutputStream());
                // LogE("结果是: " + command.getFormattedResult() + " :: name is :: " + command.getName());
                Message msg = new Message();
                msg.what = COMPLETEO;
                msg.obj = (double) (i + 1);
                handler.sendMessage(msg);
                tripRecord.updateTrip(command.getName(), command);
            } catch (Exception e) {
                LogE("执行命令异常  :: " + e.getMessage());
                //writeErrorToLocal(e);
            }
        }
        Message msg = new Message();
        msg.what = COMPLETED;
        handler.sendMessage(msg);
    }


    public void initConfig() {
        saveSpacePath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/ODBCar/OBDLog/");
        localErrorSave = new File(saveSpacePath, "OBDPID.txt");
        if (!saveSpacePath.exists()) {
            saveSpacePath.mkdirs();
        }
        if (!localErrorSave.exists()) {
            try {
                localErrorSave.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void writeErrorToLocal(Exception e) {
        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(localErrorSave, true));
            String line = "\n----------------------------------------------------------------------------------------\n";
            sb.append(line);
            sb.append("\tat ");
            sb.append(e.getMessage());
            fos.write(sb.toString());
            fos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 清除故障码
     */
    private void clearCodes(BluetoothSocket socket) {
        try {
            LogE("测试复位=====尝试重置");
            new ObdResetCommand().run(socket.getInputStream(), socket.getOutputStream());
            LogE("开始清除");
            ResetTroubleCodesCommand clear = new ResetTroubleCodesCommand();
            clear.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            String result = clear.getFormattedResult();
            LogE("重置结果: " + result);
            if (!TextUtils.isEmpty(result)) {
                Message msg = new Message();
                msg.what = COMPLETES;
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            LogE("建立连接时出错。 -> " + e.getMessage());
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
        adapter = new VehicleCheckAdapter(context);
        adapter.setList(messages);
        recycleCheckContent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                addParam("token", token).
                addParam("platformType", "1").
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                AddTestRecordEntity entity = JSON.parseObject(response, AddTestRecordEntity.class);
                if (entity.isSuccess()) {
                    Intent intent = new Intent("com.android.Record");//创建发送广播的Action
                    localBroadcastManager.sendBroadcast(intent);  //发送本地广播
                    addRemind(getUserId(), addJsonContent(entity.getData(), tripRecord.getOBDJson()), getToken(), String.valueOf(entity.getData().getId()));
                }
            }
        });
    }

    /**
     * @param appUserId APP用户ID
     * @param content   消息内容
     * @param token     用户Token
     */
    private void addRemind(String appUserId, String content, String token, String testResultId) {
        OkHttpUtils.post().url(SERVER_URL + addRemind_URL).
                addParam("appUserId", appUserId).
                addParam("remindType", "2").
                addParam("content", content).
                addParam("title", tvAutomobileBrandName.getText().toString() + " · " + tvModelName.getText().toString() + "检测报告").
                addParam("token", token).
                addParam("testResultId", testResultId).
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

    private String addJsonContent(AddTestRecordEntity.DataEntity dataEntity, OBDJsonTripEntity tripEntity) {
        MessageCheckEntity entity = new MessageCheckEntity();
        entity.setCreateTime(AppDateUtils.getTodayDateTimeHms());
        if (TextUtils.isEmpty(tripEntity.getFaultCodes())) {
            entity.setContent("您的车辆很健康!");
        } else {
            String[] troubleCodes = tripEntity.getFaultCodes().replaceAll("[\r\n]", ",").split(",");
            entity.setContent("你的车辆有" + troubleCodes.length + "个故障，请及时处理!");
        }
        entity.setDetails(String.valueOf(dataEntity.getId()));
        entity.setPlatformType(dataEntity.getPlatformType());
        entity.setVehicleName(tvAutomobileBrandName.getText().toString() + " · " + tvModelName.getText().toString());
        return JSON.toJSONString(entity);
    }

    private void initAinm() {
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.left);
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
}