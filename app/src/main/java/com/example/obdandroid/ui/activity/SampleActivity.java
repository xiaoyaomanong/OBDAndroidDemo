package com.example.obdandroid.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.TripRecord;

import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_OBD_CONNECTION_STATUS;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_READ_OBD_REAL_TIME_DATA;

/**
 * Created by sohrab on 30/11/2017.
 * 显示OBD数据的示例
 */
public class SampleActivity extends AppCompatActivity {

    private TextView mObdInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_obd);
        mObdInfoTextView = findViewById(R.id.tv_obd_info);

        /**
         *  配置obd：在arrayList中添加所需命令并设置为ObdConfiguration。
         *  如果您没有设置任何命令或传递null，那么将请求所有命令OBD command。   *
         */
        // 传递null意味着我们现在正在执行所有OBD命令，但是您应该添加必需的命令以便像上面注释的行一样快速检索。
        ObdConfiguration.setmObdCommands(this, null);


        // 设定每升汽油价格，以便计算汽油成本。默认值为7$/l
        float gasPrice = 7; // 每升，你应该根据你的要求初始化。
        ObdPreferences.get(this).setGasPrice(gasPrice);
        /**
         * 用一些与OBD连接状态有关的动作注册接收器
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_READ_OBD_REAL_TIME_DATA);
        intentFilter.addAction(ACTION_OBD_CONNECTION_STATUS);
        registerReceiver(mObdReaderReceiver, intentFilter);

        //启动服务，该服务将在后台执行连接，并执行命令，直到您停止
        Intent intent=new Intent(this, ObdReaderService.class);
        startService(intent);
    }

    /**
     * 接收OBD连接状态和实时数据的广播接收器
     */
    private final BroadcastReceiver mObdReaderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            mObdInfoTextView.setVisibility(View.VISIBLE);
            String action = intent.getAction();
            if (action.equals(ACTION_OBD_CONNECTION_STATUS)) {
                String connectionStatusMsg = intent.getStringExtra(ObdReaderService.INTENT_OBD_EXTRA_DATA);
                mObdInfoTextView.setText(connectionStatusMsg);
                Toast.makeText(SampleActivity.this, connectionStatusMsg, Toast.LENGTH_SHORT).show();
                if (connectionStatusMsg.equals(getString(R.string.obd_connected))) {
                    //OBD连接在OBD连接之后做什么
                } else if (connectionStatusMsg.equals(getString(R.string.connect_lost))) {
                    //OBD断开连接断开后做什么
                } else {
                    // 在这里您可以检查OBD连接和配对状态
                }
            } else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA)) {
                TripRecord tripRecord = TripRecord.getTripRecode(SampleActivity.this);
                mObdInfoTextView.setText(tripRecord.toString());
                // 在这里，您可以使用getter方法从TripRecord获取实时数据，如
                //tripRecord.getSpeed();
                //tripRecord.getEngineRpm();
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销接收器
        unregisterReceiver(mObdReaderReceiver);
        //停止服务
        stopService(new Intent(this, ObdReaderService.class));
        // 这将停止后台线程，如果任何运行立即。
        ObdPreferences.get(this).setServiceRunningStatus(false);
    }
}
