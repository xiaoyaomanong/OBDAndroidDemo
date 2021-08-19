package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.utils.JumpUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.kongzue.dialog.v2.TipDialog;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;

/**
 * 作者：Jealous
 * 日期：2021/1/29 0029
 * 描述：车检详情
 */
public class CheckRecordDetailsActivity extends BaseActivity {
    private TextView tvSpeed;
    private TextView tvEngineRpm;
    private TextView tvIdlingDuration;
    private TextView tvDrivingDuration;
    private TextView tvSpeedMax;
    private TextView tvEngineRpmMax;
    private TextView tvMRapidDeclTimes;
    private TextView tvMRapidAccTimes;
    private TextView tvMIdleMaf;
    private TextView tvMDrivingMaf;
    private TextView tvMInsFuelConsumption;
    private TextView tvDrivingFuelConsumption;
    private TextView tvIdlingFuelConsumption;
    private TextView tvMIgnitionMonitor;
    private TextView tvMDescribeProtocolNumber;
    private TextView tvMDescribeProtocol;
    private TextView tvMWideBandAirFuelRatio;
    private TextView tvMAirFuelRatio;
    private TextView tvMEngineOilTemp;
    private TextView tvMCommandedEGR;
    private TextView tvMAbsLoad;
    private TextView tvMDistanceTraveledMilOn;
    private TextView tvMVehicleIdentificationNumber;
    private TextView tvFuelRailPressureVacuum;
    private TextView tvMFuelRailPressure;
    private TextView tvMControlModuleVoltage;
    private TextView tvDistanceTraveledAfter;
    private TextView tvMEquivRatio;
    private TextView tvMDtcNumber;
    private TextView tvMTimingAdvance;
    private TextView tvMFuelConsumptionRate;
    private TextView tvMFuelSystemStatus;
    private TextView tvMFuelTypeValue;
    private TextView tvMFuelLevel;
    private TextView tvMPermanentTroubleCode;
    private TextView tvMPendingTroubleCode;
    private TextView tvMFaultCodes;
    private TextView tvMThrottlePos;
    private TextView tvMMassAirFlow;
    private TextView tvEngineRuntime;
    private TextView tvMEngineLoad;
    private TextView tvMIntakePressure;
    private TextView tvMFuelPressure;
    private TextView tvMBarometricPressure;
    private TextView tvMEngineCoolantTemp;
    private TextView tvMAmbientAirTemp;
    private TextView tvMIntakeAirTemp;
    private LinearLayout layoutPermanentTroubleCode;
    private LinearLayout layoutPendingTroubleCode;
    private LinearLayout layoutFaultCodes;
    private Context context;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_check_record_details;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        OBDJsonTripEntity mTripEntity = (OBDJsonTripEntity) getIntent().getSerializableExtra("data");
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        tvSpeed = findViewById(R.id.tv_speed);
        tvEngineRpm = findViewById(R.id.tv_engineRpm);
        tvIdlingDuration = findViewById(R.id.tv_IdlingDuration);
        tvDrivingDuration = findViewById(R.id.tv_DrivingDuration);
        tvSpeedMax = findViewById(R.id.tv_speedMax);
        tvEngineRpmMax = findViewById(R.id.tv_engineRpmMax);
        tvMRapidDeclTimes = findViewById(R.id.tv_mRapidDeclTimes);
        tvMRapidAccTimes = findViewById(R.id.tv_mRapidAccTimes);
        tvMIdleMaf = findViewById(R.id.tv_mIdleMaf);
        tvMDrivingMaf = findViewById(R.id.tv_mDrivingMaf);
        tvMInsFuelConsumption = findViewById(R.id.tv_mInsFuelConsumption);
        tvDrivingFuelConsumption = findViewById(R.id.tv_DrivingFuelConsumption);
        tvIdlingFuelConsumption = findViewById(R.id.tv_IdlingFuelConsumption);
        tvMIgnitionMonitor = findViewById(R.id.tv_mIgnitionMonitor);
        tvMDescribeProtocolNumber = findViewById(R.id.tv_mDescribeProtocolNumber);
        tvMDescribeProtocol = findViewById(R.id.tv_mDescribeProtocol);
        tvMWideBandAirFuelRatio = findViewById(R.id.tv_mWideBandAirFuelRatio);
        tvMAirFuelRatio = findViewById(R.id.tv_mAirFuelRatio);
        tvMEngineOilTemp = findViewById(R.id.tv_mEngineOilTemp);
        tvMCommandedEGR = findViewById(R.id.tv_mCommandedEGR);
        tvMAbsLoad = findViewById(R.id.tv_mAbsLoad);
        tvMDistanceTraveledMilOn = findViewById(R.id.tv_mDistanceTraveledMilOn);
        tvMVehicleIdentificationNumber = findViewById(R.id.tv_mVehicleIdentificationNumber);
        tvFuelRailPressureVacuum = findViewById(R.id.tv_FuelRail_Pressure_vacuum);
        tvMFuelRailPressure = findViewById(R.id.tv_mFuelRailPressure);
        tvMControlModuleVoltage = findViewById(R.id.tv_mControlModuleVoltage);
        tvDistanceTraveledAfter = findViewById(R.id.tv_DistanceTraveledAfter);
        tvMEquivRatio = findViewById(R.id.tv_mEquivRatio);
        tvMDtcNumber = findViewById(R.id.tv_mDtcNumber);
        tvMTimingAdvance = findViewById(R.id.tv_mTimingAdvance);
        tvMFuelConsumptionRate = findViewById(R.id.tv_mFuelConsumptionRate);
        tvMFuelSystemStatus = findViewById(R.id.tv_mFuelSystemStatus);
        tvMFuelTypeValue = findViewById(R.id.tv_mFuelTypeValue);
        tvMFuelLevel = findViewById(R.id.tv_mFuelLevel);
        tvMPermanentTroubleCode = findViewById(R.id.tv_mPermanentTroubleCode);
        tvMPendingTroubleCode = findViewById(R.id.tv_mPendingTroubleCode);
        tvMFaultCodes = findViewById(R.id.tv_mFaultCodes);
        tvMThrottlePos = findViewById(R.id.tv_mThrottlePos);
        tvMMassAirFlow = findViewById(R.id.tv_mMassAirFlow);
        tvEngineRuntime = findViewById(R.id.tv_engineRuntime);
        tvMEngineLoad = findViewById(R.id.tv_mEngineLoad);
        tvMIntakePressure = findViewById(R.id.tv_mIntakePressure);
        tvMFuelPressure = findViewById(R.id.tv_mFuelPressure);
        tvMBarometricPressure = findViewById(R.id.tv_mBarometricPressure);
        tvMEngineCoolantTemp = findViewById(R.id.tv_mEngineCoolantTemp);
        tvMAmbientAirTemp = findViewById(R.id.tv_mAmbientAirTemp);
        tvMIntakeAirTemp = findViewById(R.id.tv_mIntakeAirTemp);
        layoutPermanentTroubleCode = findViewById(R.id.layoutPermanentTroubleCode);
        layoutPendingTroubleCode = findViewById(R.id.layoutPendingTroubleCode);
        layoutFaultCodes = findViewById(R.id.layoutFaultCodes);
        setView(mTripEntity);
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

    @SuppressLint("SetTextI18n")
    private void setView(OBDJsonTripEntity entity) {
        tvSpeed.setText(entity.getSpeed());
        tvEngineRpm.setText(entity.getEngineRpm());
        tvIdlingDuration.setText(entity.getIdlingDuration());
        tvDrivingDuration.setText(entity.getDrivingDuration());
        tvSpeedMax.setText(entity.getSpeedMax());
        tvEngineRpmMax.setText(entity.getEngineRpmMax());
        tvMRapidDeclTimes.setText(entity.getRapidDeclTimes());
        tvMRapidAccTimes.setText(entity.getRapidAccTimes());
        tvMIdleMaf.setText(entity.getIdleMaf());
        tvMDrivingMaf.setText(entity.getDrivingMaf());
        tvMInsFuelConsumption.setText(entity.getInsFuelConsumption());
        tvDrivingFuelConsumption.setText(entity.getDrivingFuelConsumption());
        tvIdlingFuelConsumption.setText(entity.getIdlingFuelConsumption());
        tvMIgnitionMonitor.setText(entity.getIgnitionMonitor());
        tvMDescribeProtocolNumber.setText(entity.getDescribeProtocolNumber());
        tvMDescribeProtocol.setText(entity.getDescribeProtocol());
        tvMWideBandAirFuelRatio.setText(entity.getWideBandAirFuelRatio());
        tvMAirFuelRatio.setText(entity.getAirFuelRatio());
        tvMEngineOilTemp.setText(entity.getEngineOilTemp());
        tvMCommandedEGR.setText(entity.getRmCommandedEGR());
        tvMAbsLoad.setText(entity.getAbsLoad());
        tvMDistanceTraveledMilOn.setText(entity.getDistanceTraveledMilOn());
        tvMVehicleIdentificationNumber.setText(entity.getVehicleIdentificationNumber());
        tvFuelRailPressureVacuum.setText(entity.getFuelRailPressurevacuum());
        tvMFuelRailPressure.setText(entity.getFuelRailPressure());
        tvMControlModuleVoltage.setText(entity.getControlModuleVoltage());
        tvDistanceTraveledAfter.setText(entity.getDistanceTraveledAfterCodesCleared());
        tvMEquivRatio.setText(entity.getEquivRatio());
        tvMDtcNumber.setText(entity.getDtcNumber());
        tvMTimingAdvance.setText(entity.getTimingAdvance());
        tvMFuelConsumptionRate.setText(entity.getFuelConsumptionRate());
        tvMFuelSystemStatus.setText(entity.getFuelSystemStatus());
        tvMFuelTypeValue.setText(entity.getFuelTypeValue());
        tvMFuelLevel.setText(entity.getFuelLevel());
        if (!TextUtils.isEmpty(entity.getPermanentTroubleCode())) {
            tvMPermanentTroubleCode.setText(entity.getPermanentTroubleCode().replaceAll("[\r\n]", ",").split(",").length + "");
        }
        if (!TextUtils.isEmpty(entity.getPendingTroubleCode())) {
            tvMPendingTroubleCode.setText(entity.getPendingTroubleCode().replaceAll("[\r\n]", ",").split(",").length + "");
        }
        if (!TextUtils.isEmpty(entity.getFaultCodes())) {
            tvMFaultCodes.setText(entity.getFaultCodes().replaceAll("[\r\n]", ",").split(",").length + "");
        }
        tvMThrottlePos.setText(entity.getRelThottlePos());
        tvMMassAirFlow.setText(entity.getMassAirFlow());
        tvEngineRuntime.setText(entity.getEngineRuntime());
        tvMEngineLoad.setText(entity.getEngineLoad());
        tvMIntakePressure.setText(entity.getIntakePressure());
        tvMFuelPressure.setText(entity.getFuelPressure());
        tvMBarometricPressure.setText(entity.getBarometricPressure());
        tvMEngineCoolantTemp.setText(entity.getEngineCoolantTemp());
        tvMAmbientAirTemp.setText(entity.getAmbientAirTemp());
        tvMIntakeAirTemp.setText(entity.getIntakeAirTemp());
        layoutFaultCodes.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(entity.getFaultCodes())) {
                JumpUtil.startActToData(context, TroubleCodeQueryActivity.class, entity.getFaultCodes(), 0);
            } else {
                showTipDialog();
            }
        });
        layoutPendingTroubleCode.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(entity.getPendingTroubleCode())) {
                JumpUtil.startActToData(context, TroubleCodeQueryActivity.class, entity.getPendingTroubleCode(), 0);
            } else {
                showTipDialog();
            }
        });
        layoutPermanentTroubleCode.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(entity.getPermanentTroubleCode())) {
                JumpUtil.startActToData(context, TroubleCodeQueryActivity.class, entity.getPermanentTroubleCode(), 0);
            } else {
                showTipDialog();
            }
        });
    }

    private void showTipDialog() {
        TipDialog.show(context, "没有故障码可查询", TipDialog.TYPE_ERROR, TipDialog.TYPE_WARNING);
    }
}