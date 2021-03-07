package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.utils.AppDateUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.trip.CheckRecord;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;

/**
 * 作者：Jealous
 * 日期：2021/3/7
 * 描述：
 */
public class CheckReportActivity extends BaseActivity {
    private TextView tvCheckTime;
    private TextView tvCheckNum;
    private TextView tvCheckResult;
    private TextView tvDynamicSystem;
    private TextView tvBody;
    private TextView tvChassis;
    private TextView tvNetWork;
    private TextView tvDynamicSystemNO;
    private TextView tvBodyNO;
    private TextView tvChassisNO;
    private TextView tvNetWorkNO;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_check_report;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        Context context = this;
        CheckRecord tripRecord = (CheckRecord) getIntent().getSerializableExtra("data");
        TitleBar titleBar = findViewById(R.id.titleBar);
        tvCheckTime = findViewById(R.id.tvCheckTime);
        tvCheckNum = findViewById(R.id.tvCheckNum);
        tvCheckResult = findViewById(R.id.tvCheckResult);
        tvDynamicSystem = findViewById(R.id.tvDynamicSystem);
        tvBody = findViewById(R.id.tvBody);
        tvChassis = findViewById(R.id.tvChassis);
        tvNetWork = findViewById(R.id.tvNetWork);
        tvDynamicSystemNO = findViewById(R.id.tvDynamicSystemNO);
        tvBodyNO = findViewById(R.id.tvBodyNo);
        tvChassisNO = findViewById(R.id.tvChassisNO);
        tvNetWorkNO = findViewById(R.id.tvNetWorkNO);
        setView(tripRecord);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
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

    @SuppressLint("SetTextI18n")
    private void setView(CheckRecord tripRecord) {
        OBDJsonTripEntity entity = tripRecord.getOBDJson();
        tvCheckTime.setText(AppDateUtils.getTodayDateTimeHms());
        String msg = "";
        if (TextUtils.isEmpty(entity.getFaultCodes()) && TextUtils.isEmpty(entity.getPendingTroubleCode())) {
            msg = "全部通过";
            tvCheckResult.setText("你的车辆很健康");
        } else {
            msg = "未通过";
            tvCheckResult.setText("你的车辆有问题,请及时检修");
        }
        tvCheckNum.setText("检测" + tripRecord.getTripMap().size() + "项" + msg);
        String[] troubleCodes = entity.getFaultCodes().replaceAll("\r|\n", ",").split(",");
        int p = 0;
        int c = 0;
        int b = 0;
        int u = 0;
        for (String troubleCode : troubleCodes) {
            if (troubleCode.contains("P")) {
                p = p + 1;
            }
            if (troubleCode.contains("C")) {
                c = c + 1;
            }
            if (troubleCode.contains("B")) {
                b = b + 1;
            }
            if (troubleCode.contains("U")) {
                u = u + 1;
            }
        }
        if (p==0) {
            tvDynamicSystem.setText("(1) 动力系统检测     检测通过,无故障码");
        }else {
            tvDynamicSystem.setText("(1) 动力系统检测     检测未通过,发现"+p+"个故障码" );
        }
        if (c==0) {
            tvBody.setText("(1) 车身电脑控制系统     检测通过,无故障码");
        }else {
            tvBody.setText("(1) 车身电脑控制系统     检测未通过,发现"+c+"个故障码" );
        }
        if (b==0) {
            tvChassis.setText("(1) 底盘电脑控制系统     检测通过,无故障码");
        }else {
            tvChassis.setText("(1) 底盘电脑控制系统     检测未通过,发现"+b+"个故障码" );
        }
        if (u==0) {
            tvNetWork.setText("(1) 网络通讯系统     检测通过,无故障码");
        }else {
            tvNetWork.setText("(1) 网络通讯系统     检测未通过,发现"+u+"个故障码" );
        }


        String[] pendingTroubleCodes = entity.getPendingTroubleCode().replaceAll("\r|\n", ",").split(",");
        int pn = 0;
        int cn = 0;
        int bn = 0;
        int un = 0;
        for (String pendingTroubleCode : pendingTroubleCodes) {
            if (pendingTroubleCode.contains("P")) {
                pn = pn + 1;
            }
            if (pendingTroubleCode.contains("C")) {
                cn = cn + 1;
            }
            if (pendingTroubleCode.contains("B")) {
                bn = bn + 1;
            }
            if (pendingTroubleCode.contains("U")) {
                un = un + 1;
            }
        }
        if (p==0) {
            tvDynamicSystemNO.setText("(1) 动力系统检测     检测通过,无故障码");
        }else {
            tvDynamicSystemNO.setText("(1) 动力系统检测     检测未通过,发现"+pn+"个故障码" );
        }
        if (c==0) {
            tvBodyNO.setText("(1) 车身电脑控制系统     检测通过,无故障码");
        }else {
            tvBodyNO.setText("(1) 车身电脑控制系统     检测未通过,发现"+cn+"个故障码" );
        }
        if (b==0) {
            tvChassisNO.setText("(1) 底盘电脑控制系统     检测通过,无故障码");
        }else {
            tvChassisNO.setText("(1) 底盘电脑控制系统     检测未通过,发现"+bn+"个故障码" );
        }
        if (u==0) {
            tvNetWorkNO.setText("(1) 网络通讯系统     检测通过,无故障码");
        }else {
            tvNetWorkNO.setText("(1) 网络通讯系统     检测未通过,发现"+un+"个故障码" );
        }
    }
}