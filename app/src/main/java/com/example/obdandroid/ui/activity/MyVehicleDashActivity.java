package com.example.obdandroid.ui.activity;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.obd2.Command;
import com.example.obdandroid.ui.obd2.Response;
import com.example.obdandroid.ui.obd2.command.ClearDTCsCommand;
import com.example.obdandroid.ui.obd2.command.DTCsCommand;
import com.example.obdandroid.ui.obd2.command.FrozeCommand;
import com.example.obdandroid.ui.obd2.command.LiveCommand;
import com.example.obdandroid.ui.obd2.command.OxygenMonitorCommand;
import com.example.obdandroid.ui.obd2.command.PendingDTCsCommand;
import com.example.obdandroid.ui.obd2.command.VehicleInformationCommand;
import com.example.obdandroid.ui.obd2.elm327.Commander;
import com.example.obdandroid.utils.JumpUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.io.IOException;
import java.util.Map;

import javax.script.ScriptException;


/**
 * 作者：Jealous
 * 日期：2021/1/26 0026
 * 描述：
 */
public class MyVehicleDashActivity extends BaseActivity {
    private final Commander commander = new Commander();
    @Override
    protected int getContentViewId() {
        return R.layout.activity_dash;
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
        LinearLayout layoutDashOne = findViewById(R.id.layoutDashOne);
        LinearLayout layoutDashTwo = findViewById(R.id.layoutDashTwo);
        layoutDashOne.setOnClickListener(v -> JumpUtil.startAct(context, VehicleDashOneActivity.class));
        layoutDashTwo.setOnClickListener(v -> JumpUtil.startAct(context, VehicleDashTwoActivity.class));
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
        getOBDData(MainApplication.getBluetoothSocket());
    }

    private void getOBDData(BluetoothSocket socket) {
        try {
            commander.setCommunicationInterface(socket.getOutputStream(), socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //liveData();
        frozeData();
        getDTC();
        getOxygenMonitorData();
        getPendingDTC();
        getVehicleInfo();

    }


    /**
     * 获取实时数据
     */
    private void liveData() {
        try {
            for (Map.Entry<String, Command> entry : LiveCommand.getService01Commands().entrySet()) {
                LogE("PID:" + entry.getKey());
                Response response=commander.sendCommand(entry.getValue());
                LogE("结果:" + response.getFormattedString());
            }
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取冻结帧
     */
    private void frozeData() {
        try {
            for (Map.Entry<String, FrozeCommand> entry : FrozeCommand.getService02Commands().entrySet()) {
                LogE("PID:" + entry.getKey());
                LogE("结果:" + commander.sendCommand(entry.getValue()).getFormattedString());
            }
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除故障码
     */
    private void clearDTC() {
        try {
            LogE("清除故障码：" + commander.sendCommand(new ClearDTCsCommand()).getFormattedString());
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取故障码
     */
    private void getDTC() {
        try {
            LogE("故障码：" + commander.sendCommand(new DTCsCommand()).getFormattedString());
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }


    /**
     * 请求氧传感器监测测试结果
     */
    private void getOxygenMonitorData() {
        try {
            for (Map.Entry<String, Command> entry : OxygenMonitorCommand.getService05Commands().entrySet()) {
                LogE("PID:" + entry.getKey());
                LogE("结果:" + commander.sendCommand(entry.getValue()).getFormattedString());
            }
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }


    /**
     * 请求未解决故障码
     */
    private void getPendingDTC() {
        try {
            LogE("未解决故障码：" + commander.sendCommand(new PendingDTCsCommand()).getFormattedString());
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }


    /**
     * 请求未解决故障码
     */
    private void getVehicleInfo() {
        try {
            for (Map.Entry<String, Command> entry : VehicleInformationCommand.getService09Commands().entrySet()) {
                LogE("PID:" + entry.getKey());
                LogE("结果:" + commander.sendCommand(entry.getValue()).getFormattedString());
            }
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }
}