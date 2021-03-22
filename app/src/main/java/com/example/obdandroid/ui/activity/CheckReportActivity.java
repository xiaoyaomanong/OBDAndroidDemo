package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.utils.AppDateUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.example.obdandroid.config.CheckRecord;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;

import java.util.ArrayList;

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
    private TextView tvBodyNo;
    private TextView tvChassisNO;
    private TextView tvNetWorkNO;
    private TextView tvSpeed;
    private TextView tvEngineRpm;
    private TextView tvMassAirFlow;
    private TextView tvEngineRuntime;
    private TextView tvFuelLevel;
    private TextView tvIntakePressure;
    private TextView tvIntakeAirTemp;
    private TextView tvAmbientAirTemp;
    private TextView tvEngineCoolantTemp;
    private TextView tvEngineOilTemp;
    private TextView tvFuelConsumptionRate;
    private TextView tvFuelPressure;
    private TextView tvEngineLoad;
    private TextView tvBarometricPressure;
    private TextView tvRelThottlePos;
    private TextView tvTimingAdvance;
    private TextView tvEquivRatio;
    private TextView tvDistanceTraveledAfterCodesCleared;
    private TextView tvControlModuleVoltage;
    private TextView tvFuelRailPressure;
    private TextView tvVehicleIdentificationNumber;
    private TextView tvDistanceTraveledMilOn;
    private TextView tvDtcNumber;
    private TextView tvAbsLoad;
    private TextView tvAirFuelRatio;
    private TextView tvDescribeProtocol;
    private TextView tvIgnitionMonitor;
    private TextView tvShortTermBank1;
    private TextView tvShortTermBank2;
    private TextView tvLongTermBank1;
    private TextView tvLongTermBank2;
    private Context context;
    private final ArrayList<String> dynamicSystemList = new ArrayList<>();
    private final ArrayList<String> bodyList = new ArrayList<>();
    private final ArrayList<String> chassisList = new ArrayList<>();
    private final ArrayList<String> netWorkList = new ArrayList<>();
    private final ArrayList<String> dynamicSystemNOList = new ArrayList<>();
    private final ArrayList<String> bodyNOList = new ArrayList<>();
    private final ArrayList<String> chassisNOList = new ArrayList<>();
    private final ArrayList<String> netWorkNOList = new ArrayList<>();


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
        context = this;
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
        tvBodyNo = findViewById(R.id.tvBodyNo);
        tvChassisNO = findViewById(R.id.tvChassisNO);
        tvNetWorkNO = findViewById(R.id.tvNetWorkNO);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvEngineRpm = findViewById(R.id.tvEngineRpm);
        tvMassAirFlow = findViewById(R.id.tvMassAirFlow);
        tvEngineRuntime = findViewById(R.id.tvEngineRuntime);
        tvFuelLevel = findViewById(R.id.tvFuelLevel);
        tvIntakePressure = findViewById(R.id.tvIntakePressure);
        tvIntakeAirTemp = findViewById(R.id.tvIntakeAirTemp);
        tvAmbientAirTemp = findViewById(R.id.tvAmbientAirTemp);
        tvEngineCoolantTemp = findViewById(R.id.tvEngineCoolantTemp);
        tvEngineOilTemp = findViewById(R.id.tvEngineOilTemp);
        tvFuelConsumptionRate = findViewById(R.id.tvFuelConsumptionRate);
        tvFuelPressure = findViewById(R.id.tvFuelPressure);
        tvEngineLoad = findViewById(R.id.tvEngineLoad);
        tvBarometricPressure = findViewById(R.id.tvBarometricPressure);
        tvRelThottlePos = findViewById(R.id.tvRelThottlePos);
        tvTimingAdvance = findViewById(R.id.tvTimingAdvance);
        tvEquivRatio = findViewById(R.id.tvEquivRatio);
        tvDistanceTraveledAfterCodesCleared = findViewById(R.id.tvDistanceTraveledAfterCodesCleared);
        tvControlModuleVoltage = findViewById(R.id.tvControlModuleVoltage);
        tvFuelRailPressure = findViewById(R.id.tvFuelRailPressure);
        tvVehicleIdentificationNumber = findViewById(R.id.tvVehicleIdentificationNumber);
        tvDistanceTraveledMilOn = findViewById(R.id.tvDistanceTraveledMilOn);
        tvDtcNumber = findViewById(R.id.tvDtcNumber);
        tvAbsLoad = findViewById(R.id.tvAbsLoad);
        tvAirFuelRatio = findViewById(R.id.tvAirFuelRatio);
        tvDescribeProtocol = findViewById(R.id.tvDescribeProtocol);
        tvIgnitionMonitor = findViewById(R.id.tvIgnitionMonitor);
        tvShortTermBank1 = findViewById(R.id.tvShortTermBank1);
        tvShortTermBank2 = findViewById(R.id.tvShortTermBank2);
        tvLongTermBank1 = findViewById(R.id.tvLongTermBank1);
        tvLongTermBank2 = findViewById(R.id.tvLongTermBank2);
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
        String msg;
        if (TextUtils.isEmpty(entity.getFaultCodes()) && TextUtils.isEmpty(entity.getPendingTroubleCode())) {
            msg = "全部通过";
            tvCheckResult.setText("你的车辆很健康");
        } else {
            msg = "未通过";
            tvCheckResult.setText("你的车辆有问题,请及时检修");
        }
        tvCheckNum.setText("检测8项" + msg);
        int power = 0;
        int body = 0;
        int chassis = 0;
        int netWork = 0;
        String[] troubleCodes;
        if (!TextUtils.isEmpty(entity.getFaultCodes())) {
            troubleCodes = entity.getFaultCodes().replaceAll("[\r\n]", ",").split(",");
            for (String troubleCode : troubleCodes) {
                if (troubleCode.startsWith("P")) {
                    power = power + 1;
                    dynamicSystemList.add(troubleCode);
                }
                if (troubleCode.startsWith("C")) {
                    chassis = chassis + 1;
                    chassisList.add(troubleCode);
                }
                if (troubleCode.startsWith("B")) {
                    body = body + 1;
                    bodyList.add(troubleCode);
                }
                if (troubleCode.startsWith("U")) {
                    netWork = netWork + 1;
                    netWorkList.add(troubleCode);
                }
            }
        }

        if (power == 0) {
            tvDynamicSystem.setText("检测通过");
            tvDynamicSystem.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvDynamicSystem.setText("检测未通过,发现" + power + "个故障码");
            tvDynamicSystem.setTextColor(getResources().getColor(R.color.red));
            tvDynamicSystem.setOnClickListener(v -> {
                Intent intent = new Intent(context, TroubleCodeActivity.class);
                intent.putStringArrayListExtra(Constant.ACT_FLAG, dynamicSystemList);
                startActivity(intent);
            });
        }
        if (body == 0) {
            tvBody.setText("检测通过");
            tvBody.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvBody.setText("检测未通过,发现" + body + "个故障码");
            tvBody.setTextColor(getResources().getColor(R.color.red));
            tvBody.setOnClickListener(v -> {
                Intent intent = new Intent(context, TroubleCodeActivity.class);
                intent.putStringArrayListExtra(Constant.ACT_FLAG, bodyList);
                startActivity(intent);
            });
        }
        if (chassis == 0) {
            tvChassis.setText("检测通过");
            tvChassis.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvChassis.setText("检测未通过,发现" + chassis + "个故障码");
            tvChassis.setTextColor(getResources().getColor(R.color.red));
            tvChassis.setOnClickListener(v -> {
                Intent intent = new Intent(context, TroubleCodeActivity.class);
                intent.putStringArrayListExtra(Constant.ACT_FLAG, chassisList);
                startActivity(intent);
            });
        }
        if (netWork == 0) {
            tvNetWork.setText("检测通过");
            tvNetWork.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvNetWork.setText("检测未通过,发现" + netWork + "个故障码");
            tvNetWork.setTextColor(getResources().getColor(R.color.red));
            tvNetWork.setOnClickListener(v -> {
                Intent intent = new Intent(context, TroubleCodeActivity.class);
                intent.putStringArrayListExtra(Constant.ACT_FLAG, netWorkList);
                startActivity(intent);
            });
        }
        int powerNO = 0;
        int bodyNO = 0;
        int chassisNO = 0;
        int netWorkNO = 0;
        String[] pendingTroubleCodes;
        if (!TextUtils.isEmpty(entity.getPendingTroubleCode())) {
            pendingTroubleCodes = entity.getPendingTroubleCode().replaceAll("[\r\n]", ",").split(",");
            for (String pendingTroubleCode : pendingTroubleCodes) {
                if (pendingTroubleCode.startsWith("P")) {
                    powerNO = powerNO + 1;
                    dynamicSystemNOList.add(pendingTroubleCode);
                }
                if (pendingTroubleCode.startsWith("C")) {
                    chassisNO = chassisNO + 1;
                    chassisNOList.add(pendingTroubleCode);
                }
                if (pendingTroubleCode.startsWith("B")) {
                    bodyNO = bodyNO + 1;
                    bodyNOList.add(pendingTroubleCode);
                }
                if (pendingTroubleCode.startsWith("U")) {
                    netWorkNO = netWorkNO + 1;
                    netWorkNOList.add(pendingTroubleCode);
                }
            }
        }


        if (powerNO == 0) {
            tvDynamicSystemNO.setText("检测通过");
            tvDynamicSystemNO.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvDynamicSystemNO.setText("检测未通过,发现" + powerNO + "个故障码");
            tvDynamicSystemNO.setTextColor(getResources().getColor(R.color.red));
            tvDynamicSystemNO.setOnClickListener(v -> {
                Intent intent = new Intent(context, TroubleCodeActivity.class);
                intent.putStringArrayListExtra(Constant.ACT_FLAG, dynamicSystemNOList);
                startActivity(intent);
            });
        }
        if (bodyNO == 0) {
            tvBodyNo.setText("检测通过");
            tvBodyNo.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvBodyNo.setText("检测未通过,发现" + bodyNO + "个故障码");
            tvBodyNo.setTextColor(getResources().getColor(R.color.red));
            tvBodyNo.setOnClickListener(v -> {
                Intent intent = new Intent(context, TroubleCodeActivity.class);
                intent.putStringArrayListExtra(Constant.ACT_FLAG, bodyNOList);
                startActivity(intent);
            });
        }
        if (chassisNO == 0) {
            tvChassisNO.setText("检测通过");
            tvChassisNO.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvChassisNO.setText("检测未通过,发现" + chassisNO + "个故障码");
            tvChassisNO.setTextColor(getResources().getColor(R.color.red));
            tvChassisNO.setOnClickListener(v -> {
                Intent intent = new Intent(context, TroubleCodeActivity.class);
                intent.putStringArrayListExtra(Constant.ACT_FLAG, chassisNOList);
                startActivity(intent);
            });
        }
        if (netWorkNO == 0) {
            tvNetWorkNO.setText("检测通过");
            tvNetWorkNO.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvNetWorkNO.setText("检测未通过,发现" + netWorkNO + "个故障码");
            tvNetWorkNO.setTextColor(getResources().getColor(R.color.red));
            tvNetWorkNO.setOnClickListener(v -> {
                Intent intent = new Intent(context, TroubleCodeActivity.class);
                intent.putStringArrayListExtra(Constant.ACT_FLAG, netWorkNOList);
                startActivity(intent);
            });
        }

        tvSpeed.setText(entity.getSpeed());
        tvEngineRpm.setText(entity.getEngineRpm());
        tvMassAirFlow.setText(entity.getMassAirFlow());
        tvEngineRuntime.setText(entity.getEngineRuntime());
        tvFuelLevel.setText(entity.getFuelLevel());
        tvIntakePressure.setText(entity.getIntakePressure());
        tvIntakeAirTemp.setText(entity.getIntakeAirTemp());
        tvAmbientAirTemp.setText(entity.getAmbientAirTemp());
        tvEngineCoolantTemp.setText(entity.getEngineCoolantTemp());
        tvEngineOilTemp.setText(entity.getEngineOilTemp());
        tvFuelConsumptionRate.setText(entity.getFuelConsumptionRate());
        tvFuelPressure.setText(entity.getFuelPressure());
        tvEngineLoad.setText(entity.getEngineLoad());
        tvBarometricPressure.setText(entity.getBarometricPressure());
        tvRelThottlePos.setText(entity.getRelThottlePos());
        tvTimingAdvance.setText(entity.getTimingAdvance());
        tvEquivRatio.setText(entity.getEquivRatio());
        tvDistanceTraveledAfterCodesCleared.setText(entity.getDistanceTraveledAfterCodesCleared());
        tvControlModuleVoltage.setText(entity.getControlModuleVoltage());
        tvFuelRailPressure.setText(entity.getFuelRailPressure());
        tvVehicleIdentificationNumber.setText(entity.getVehicleIdentificationNumber());
        tvDistanceTraveledMilOn.setText(entity.getDistanceTraveledMilOn());
        tvDtcNumber.setText(entity.getDtcNumber());
        tvAbsLoad.setText(entity.getAbsLoad());
        tvAirFuelRatio.setText(entity.getAirFuelRatio());
        tvDescribeProtocol.setText(entity.getDescribeProtocol());
        tvIgnitionMonitor.setText(entity.getIgnitionMonitor());
        tvShortTermBank1.setText(entity.getShortTermBank1());
        tvShortTermBank2.setText(entity.getShortTermBank2());
        tvLongTermBank1.setText(entity.getLongTermBank1());
        tvLongTermBank2.setText(entity.getLongTermBank2());
    }
}