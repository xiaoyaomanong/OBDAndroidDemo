package com.sohrab.obd.reader.trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.constants.DefineObdTwoReader;
import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.FuelType;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.engine.RuntimeCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FindFuelTypeCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.com.android_obd_reader.R;

/**
 * 作者：Jealous
 * 日期：2021/1/21
 * 描述：
 */
public class TripRecordCar implements DefineObdTwoReader, Serializable {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    int MINUS_ONE = -1;
    private static final int SPEED_GAP = 20;
    private static final float GRAM_TO_LITRE_GASOLIN = 748.9f;
    private static final float GRAM_TO_LITRE_DIESEL = 850.8f;
    private static final float GRAM_TO_LITRE_CNG = 128.2f;
    private static final float GRAM_TO_LITRE_METHANOL = 786.6f;
    private static final float GRAM_TO_LITRE_ETHANOL = 789f;
    private static final float GRAM_TO_LITRE_PROPANE = 493f;
    private final static String ENGINE_RPM = "Engine RPM";
    private final static String VEHICLE_SPEED = "Vehicle Speed";
    private final static String ENGINE_RUNTIME = "Engine Runtime";
    private final static String MAF = "Mass Air Flow";
    private final static String FUEL_LEVEL = "Fuel Level";
    private final static String FUEL_TYPE = "Fuel Type";
    private final static String INTAKE_MANIFOLD_PRESSURE = "Intake Manifold Pressure";
    private final static String AIR_INTAKE_TEMPERATURE = "Air Intake Temperature";
    private final static String TROUBLE_CODES = "Trouble Codes";
    private final static String AMBIENT_AIR_TEMP = "Ambient Air Temperature";
    private final static String ENGINE_COOLANT_TEMP = "Engine Coolant Temperature";
    private final static String BAROMETRIC_PRESSURE = "Barometric Pressure";
    private final static String FUEL_PRESSURE = "Fuel Pressure";
    private final static String ENGINE_LOAD = "Engine Load";
    private final static String THROTTLE_POS = "Throttle Position";
    private final static String FUEL_CONSUMPTION_RATE = "Fuel Consumption Rate";
    private final static String FUEL_SYSTEM_STATUS = "Fuel System Status";
    private final static String TIMING_ADVANCE = "Timing Advance";
    private final static String PERMANENT_TROUBLE_CODES = "Permanent Trouble Codes";
    private final static String PENDING_TROUBLE_CODES = "Pending Trouble Codes";
    private final static String EQUIV_RATIO = "Command Equivalence Ratio";
    private final static String DISTANCE_TRAVELED_AFTER_CODES_CLEARED = "Distance since codes cleared";
    private final static String CONTROL_MODULE_VOLTAGE = "Control Module Power Supply ";
    private final static String ENGINE_FUEL_RATE = "Engine Fuel Rate";
    private final static String FUEL_RAIL_PRESSURE = "Fuel Rail Pressure";
    private final static String FUEL_RAIL_PRESSURE_manifold = "Fuel Rail Pressure relative to manifold vacuum";
    private final static String VIN = "Vehicle Identification Number (VIN)";
    private final static String DISTANCE_TRAVELED_MIL_ON = "Distance traveled with MIL on";
    private final static String DTC_NUMBER = "Diagnostic Trouble Codes";
    private final static String TIME_SINCE_TC_CLEARED = "Time since trouble codes cleared";
    private final static String REL_THROTTLE_POS = "Relative throttle position";
    private final static String ABS_LOAD = "Absolute load";
    private final static String ENGINE_OIL_TEMP = "Engine oil temperature";
    private final static String AIR_FUEL_RATIO = "Air/Fuel Ratio";
    private final static String WIDEBAND_AIR_FUEL_RATIO = "Wideband Air/Fuel Ratio";
    private final static String DESCRIBE_PROTOCOL = "Describe protocol";
    private final static String DESCRIBE_PROTOCOL_NUMBER = "Describe protocol number";
    private final static String IGNITION_MONITOR = "Ignition monitor";
    private final static String Commanded_EGR = "Commanded EGR";
    @SuppressLint("StaticFieldLeak")
    private static TripRecordCar sInstance;
    private Integer engineRpmMax = 0;
    private String engineRpm;
    private Integer speed = -1;
    private Integer speedMax = 0;
    private String engineRuntime;
    private final long tripStartTime;
    private float idlingDuration;
    private float drivingDuration;
    private long mAddSpeed;
    private long mSpeedCount;
    private float mDistanceTravel;
    private float mMassAirFlow;
    private int mRapidAccTimes;
    private int mRapidDeclTimes;
    private float mInsFuelConsumption = 0.0f;
    private float mDrivingFuelConsumption = 0.0f;
    private float mIdlingFuelConsumption = 0.0f;
    private String mFuelLevel;
    private long mLastTimeStamp;
    private float mFuelTypeValue = 14.7f; //默认值为汽油燃油比
    private String mFuelTypeName;
    private float mDrivingMaf;
    private int mDrivingMafCount;
    private float mIdleMaf;
    private int mIdleMafCount;
    private int mSecondAgoSpeed;
    private float gramToLitre = GRAM_TO_LITRE_GASOLIN;
    private final String mTripIdentifier;
    private boolean mIsMAFSupported = true;
    private boolean mIsEngineRuntimeSupported = true;
    private boolean mIsTempPressureSupported = true;
    private float mIntakeAirTemp = 0.0f;
    private float mIntakePressure = 0.0f;
    private String mFaultCodes = "";
    private String mAmbientAirTemp;
    private String mEngineCoolantTemp;
    private String mEngineOilTemp;
    private String mFuelConsumptionRate;
    private String mFuelSystemStatus;
    private String mFuelPressure;
    private String mCommandedEGR;
    private String mEngineLoad;
    private String mBarometricPressure;
    private String mThrottlePos;
    private String mTimingAdvance;
    private String mPermanentTroubleCode;
    private String mPendingTroubleCode;
    private String mEquivRatio;
    private String mDistanceTraveledAfterCodesCleared;
    private String mControlModuleVoltage;
    private String mFuelRailPressure;
    private String mFuelRailPressurevacuum;
    private String mVehicleIdentificationNumber;
    private String mDistanceTraveledMilOn;
    private String mDtcNumber;
    private String mTimeSinceTcClear;
    private String mRelThottlePos;
    private String mAbsLoad;
    private String mAirFuelRatio;
    private String mWideBandAirFuelRatio;
    private String mDescribeProtocol;
    private String mDescribeProtocolNumber;
    private String mIgnitionMonitor;
    private List<OBDTripEntity> datas = new ArrayList<>();
    private List<OBDTripEntity> data = new ArrayList<>();
    private OBDJsonTripEntity entity = new OBDJsonTripEntity();

    private TripRecordCar() {
        tripStartTime = System.currentTimeMillis();
        mTripIdentifier = UUID.randomUUID().toString();
    }

    public static TripRecordCar getTripTwoRecode(Context context) {
        sContext = context;
        if (sInstance == null)
            sInstance = new TripRecordCar();
        return sInstance;
    }

    public void clear() {
        sInstance = null;
    }

    public void setSpeed(Integer currentSpeed) {
        calculateIdlingAndDrivingTime(currentSpeed);
        findRapidAccAndDeclTimes(currentSpeed);
        speed = currentSpeed;
        if (speedMax < currentSpeed)
            speedMax = currentSpeed;
        // find travelled distance
        if (speed != 0) {
            mAddSpeed += speed;
            mSpeedCount++;
            mDistanceTravel = (mAddSpeed / mSpeedCount * (drivingDuration / (60 * 60 * 1000)));
        }
        data.add(new OBDTripEntity("时速", speed + " km/h", R.drawable.icon_speed));
        entity.setSpeed(speed + " km/h");
        data.add(new OBDTripEntity("最高时速", speedMax + " km/h", R.drawable.icon_max_speed));
        entity.setSpeedMax(speedMax + " km/h");
    }

    private void findRapidAccAndDeclTimes(Integer currentSpeed) {
        if (speed == -1)
            return;
        if (System.currentTimeMillis() - mLastTimeStamp > 1000) {

            int speedDiff = currentSpeed - mSecondAgoSpeed;
            boolean acceleration = speedDiff > 0;
            if (Math.abs(speedDiff) > SPEED_GAP) {
                if (acceleration)
                    mRapidAccTimes++;
                else
                    mRapidDeclTimes++;
            }
            mSecondAgoSpeed = currentSpeed;
            mLastTimeStamp = System.currentTimeMillis();
        }
        data.add(new OBDTripEntity("速降次数", mRapidDeclTimes + "", R.drawable.icon_js_times));
        entity.setRapidDeclTimes(mRapidDeclTimes + "");
        data.add(new OBDTripEntity("加速次数", mRapidAccTimes + " ", R.drawable.icon_add_times));
        entity.setRapidAccTimes(mRapidAccTimes + "");
    }

    private void calculateIdlingAndDrivingTime(int currentSpeed) {

        long currentTime = System.currentTimeMillis();
        if ((speed == -1 || speed == 0) && currentSpeed == 0) {
            idlingDuration = currentTime - tripStartTime - drivingDuration;
            data.add(new OBDTripEntity("空闲时间", (idlingDuration / 60000) + " 分", R.drawable.icon_kxsj));
            entity.setIdlingDuration((idlingDuration / 60000) + " 分");
        }
        drivingDuration = currentTime - tripStartTime - idlingDuration;
        data.add(new OBDTripEntity("行驶时间", (drivingDuration / 60000) + " 分", R.drawable.icon_driving_time));
        entity.setDrivingDuration((drivingDuration / 60000) + " 分");
    }

    private void calculateMaf() {

        if (mIntakePressure > 0 && mIntakeAirTemp > 0) {
            float rpm = Float.parseFloat(engineRpm);
            float imap = ((rpm * mIntakePressure) / mIntakeAirTemp) / 2;
            //   float engineDisp = ObdReaderApplication.getInstance().getLoggedInUser().getDisp();
            float engineDisp = 2;
            float maf = (imap / 60.0f) * (85.0f / 100.0f) * (engineDisp) * ((28.97f) / (8.314f));
            findInsFualConsumption(maf);
        }
    }


    public Integer getSpeed() {
        if (speed == -1)
            return 0;

        return speed;
    }


    public Integer getEngineRpmMax() {
        return this.engineRpmMax;
    }

    public float getDrivingDuration() {
        return drivingDuration / 60000; //time in minutes
    }

    public float getIdlingDuration() {
        return (idlingDuration / 60000); // time in minutes
    }


    public Integer getSpeedMax() {
        return speedMax;
    }

    public float getmDistanceTravel() {
        return mDistanceTravel;
    }

    public int getmRapidAccTimes() {
        return mRapidAccTimes;
    }

    public int getmRapidDeclTimes() {
        return mRapidDeclTimes;
    }

    public String getEngineRuntime() {
        return engineRuntime;
    }

    public String getmTripIdentifier() {
        return mTripIdentifier;
    }

    public float getmGasCost() {
        return (mIsMAFSupported || mIsTempPressureSupported) ? (mIdlingFuelConsumption + mDrivingFuelConsumption) * ObdPreferences.get(sContext.getApplicationContext()).getGasPrice() : MINUS_ONE;
    }

    public boolean ismIsMAFSupported() {
        return mIsMAFSupported;
    }

    public void setmIsMAFSupported(boolean mIsMAFSupported) {
        this.mIsMAFSupported = mIsMAFSupported;
    }

    public boolean ismIsEngineRuntimeSupported() {
        return mIsEngineRuntimeSupported;
    }

    public void setmIsEngineRuntimeSupported(boolean mIsEngineRuntimeSupported) {
        this.mIsEngineRuntimeSupported = mIsEngineRuntimeSupported;
    }

    public void findInsFualConsumption(float massAirFlow) {
        if (speed > 0)
            mInsFuelConsumption = 100 * (massAirFlow / (mFuelTypeValue * gramToLitre) * 3600) / speed; // in  litre/100km
        findIdleAndDrivingFuelConsumtion(massAirFlow);
        data.add(new OBDTripEntity("瞬时油耗", ((mIsMAFSupported || mIsTempPressureSupported) ? mInsFuelConsumption : MINUS_ONE) + " L/100km", R.drawable.icon_ssyh));
        entity.setInsFuelConsumption(((mIsMAFSupported || mIsTempPressureSupported) ? mInsFuelConsumption : MINUS_ONE) + " L");
    }

    public float getmInsFuelConsumption() {
        return (mIsMAFSupported || mIsTempPressureSupported) ? mInsFuelConsumption : MINUS_ONE;
    }

    public void setEngineRpm(String value) {
        engineRpm = value;
        if (value != null && this.engineRpmMax < Integer.parseInt(value)) {
            this.engineRpmMax = Integer.parseInt(value);
        }
        data.add(new OBDTripEntity("发动机转速", TextUtils.isEmpty(engineRpm) ? "" : engineRpm, R.drawable.icon_engine_zs));
        entity.setEngineRpm(TextUtils.isEmpty(engineRpm) ? "" : engineRpm);
        data.add(new OBDTripEntity("最大转速", engineRpmMax + "", R.drawable.icon_max_engine_zs));
        entity.setEngineRpmMax(String.valueOf(engineRpmMax));
    }

    public void findIdleAndDrivingFuelConsumtion(float currentMaf) {
        float literPerSecond = 0;
        if (speed > 0) {
            mDrivingMaf += currentMaf;
            mDrivingMafCount++;
            literPerSecond = ((((mDrivingMaf / mDrivingMafCount) / mFuelTypeValue) / gramToLitre));
            mDrivingFuelConsumption = (literPerSecond * (drivingDuration / 1000));

        } else {
            mIdleMaf += currentMaf;
            mIdleMafCount++;
            literPerSecond = ((((mIdleMaf / mIdleMafCount) / mFuelTypeValue) / gramToLitre));
            mIdlingFuelConsumption = (literPerSecond * (idlingDuration / 1000));
        }
        data.add(new OBDTripEntity("怠速空气流量", mIdleMaf + " g/s", R.drawable.icon_dskqll));
        entity.setIdleMaf(mIdleMaf + " g/s");
        data.add(new OBDTripEntity("驱动空气流量", mDrivingMaf + " g/s", R.drawable.icon_qdkqll));
        entity.setDrivingMaf(mDrivingMaf + " g/s");
        data.add(new OBDTripEntity("行驶油耗", ((mIsMAFSupported || mIsTempPressureSupported) ? mDrivingFuelConsumption : MINUS_ONE) + " L", R.drawable.icon_xsyh));
        data.add(new OBDTripEntity("怠速油耗", ((mIsMAFSupported || mIsTempPressureSupported) ? mIdlingFuelConsumption : MINUS_ONE) + " L", R.drawable.icon_dsyh));
        entity.setDrivingFuelConsumption(((mIsMAFSupported || mIsTempPressureSupported) ? mDrivingFuelConsumption : MINUS_ONE) + " L");
        entity.setIdlingFuelConsumption(((mIsMAFSupported || mIsTempPressureSupported) ? mIdlingFuelConsumption : MINUS_ONE) + " L");

    }


    public float getmDrivingFuelConsumption() {
        return (mIsMAFSupported || mIsTempPressureSupported) ? mDrivingFuelConsumption : MINUS_ONE;
    }

    public float getmIdlingFuelConsumption() {
        return (mIsMAFSupported || mIsTempPressureSupported) ? mIdlingFuelConsumption : MINUS_ONE;
    }

    public String getEngineRpm() {
        return engineRpm;
    }

    public String getmFaultCodes() {
        return mFaultCodes;
    }

    public void updateTrip(String name, ObdCommand command) {
        switch (name) {
            case VEHICLE_SPEED:
                setSpeed(((SpeedCommand) command).getMetricSpeed());
                break;
            case ENGINE_RPM:
                setEngineRpm(command.getCalculatedResult());
                setmIsEngineRuntimeSupported(true);
                break;
            case MAF:
                mMassAirFlow = Float.parseFloat(command.getFormattedResult());
                data.add(new OBDTripEntity("空气质量流量", mMassAirFlow + "", R.drawable.icon_kqzlll));
                entity.setMassAirFlow(mMassAirFlow + "");
                findInsFualConsumption(Float.parseFloat(command.getFormattedResult()));
                setmIsMAFSupported(true);
                break;

            case ENGINE_RUNTIME:
                engineRuntime = command.getFormattedResult();
                data.add(new OBDTripEntity("引擎运行时间", TextUtils.isEmpty(engineRuntime) ? "" : engineRuntime, R.drawable.icon_engine_time));
                entity.setEngineRuntime(TextUtils.isEmpty(engineRuntime) ? "" : engineRuntime);
                break;

            case FUEL_LEVEL:
                mFuelLevel = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油油位", TextUtils.isEmpty(mFuelLevel) ? "" : mFuelLevel, R.drawable.icon_fuel_level));
                entity.setFuelLevel(TextUtils.isEmpty(mFuelLevel) ? "" : mFuelLevel + "%");
                break;
            case FUEL_TYPE:
                if (ObdPreferences.get(sContext.getApplicationContext()).getFuelType() == 0)
                    getFuelTypeValue(command.getFormattedResult());
                break;
            case INTAKE_MANIFOLD_PRESSURE:
                mIntakePressure = Float.parseFloat(command.getCalculatedResult());
                data.add(new OBDTripEntity("进气歧管压力", mIntakePressure + " kpa", R.drawable.icon_intake_pressure));
                entity.setIntakePressure(mIntakePressure + " kpa");
                calculateMaf();
                break;
            case AIR_INTAKE_TEMPERATURE:
                mIntakeAirTemp = Float.parseFloat(command.getCalculatedResult()) + 273.15f;
                data.add(new OBDTripEntity("进气温度", mIntakeAirTemp + " ℃", R.drawable.icon_intake_air_temp));
                entity.setIntakeAirTemp(mIntakeAirTemp + " ℃");
                calculateMaf();
                break;

            case TROUBLE_CODES:
                mFaultCodes = command.getFormattedResult();
                data.add(new OBDTripEntity("故障代码", TextUtils.isEmpty(mFaultCodes) ? "" : mFaultCodes, R.drawable.icon_troublecode_one));
                entity.setFaultCodes(TextUtils.isEmpty(mFaultCodes) ? "" : mFaultCodes);
                break;
            case AMBIENT_AIR_TEMP:
                mAmbientAirTemp = command.getFormattedResult();
                data.add(new OBDTripEntity("环境空气温度", TextUtils.isEmpty(mAmbientAirTemp) ? "" : mAmbientAirTemp + " ℃", R.drawable.icon_hjkqwd));
                entity.setAmbientAirTemp(TextUtils.isEmpty(mAmbientAirTemp) ? "" : mAmbientAirTemp);
                break;

            case ENGINE_COOLANT_TEMP:
                mEngineCoolantTemp = command.getFormattedResult();
                data.add(new OBDTripEntity("发动机冷却液温度", TextUtils.isEmpty(mEngineCoolantTemp) ? "" : mEngineCoolantTemp + " ℃", R.drawable.icon_fdjlqywd));
                entity.setEngineCoolantTemp(TextUtils.isEmpty(mEngineCoolantTemp) ? "" : mEngineCoolantTemp);
                break;

            case ENGINE_OIL_TEMP:
                mEngineOilTemp = command.getFormattedResult();
                data.add(new OBDTripEntity("发动机油温", TextUtils.isEmpty(mEngineOilTemp) ? "" : mEngineOilTemp, R.drawable.icon_fdjyw));
                entity.setEngineOilTemp(TextUtils.isEmpty(mEngineOilTemp) ? "" : mEngineOilTemp);
                break;

            case FUEL_CONSUMPTION_RATE:
                mFuelConsumptionRate = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油消耗率", TextUtils.isEmpty(mFuelConsumptionRate) ? "" : mFuelConsumptionRate, R.drawable.icon_ryxhl));
                entity.setFuelConsumptionRate(TextUtils.isEmpty(mFuelConsumptionRate) ? "" : mFuelConsumptionRate);
                break;
            case FUEL_SYSTEM_STATUS:
                mFuelSystemStatus = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油系统状态", mFuelSystemStatus, R.drawable.icon_system_tats));
                entity.setFuelSystemStatus(mFuelSystemStatus);
                break;
            case FUEL_PRESSURE:
                mFuelPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油压力", TextUtils.isEmpty(mFuelPressure) ? "" : mFuelPressure, R.drawable.icon_ryyl));
                entity.setFuelPressure(TextUtils.isEmpty(mFuelPressure) ? "" : mFuelPressure);
                break;
            case ENGINE_LOAD:
                mEngineLoad = command.getFormattedResult();
                data.add(new OBDTripEntity("发动机负荷", TextUtils.isEmpty(mEngineLoad) ? "" : mEngineLoad, R.drawable.icon_fdjfh));
                entity.setEngineLoad(TextUtils.isEmpty(mEngineLoad) ? "" : mEngineLoad);
                break;
            case Commanded_EGR:
                mCommandedEGR = command.getFormattedResult();
                data.add(new OBDTripEntity("指令EGR", TextUtils.isEmpty(mCommandedEGR) ? "" : mCommandedEGR, R.drawable.icon_zlegr));
                entity.setRmCommandedEGR(TextUtils.isEmpty(mCommandedEGR) ? "" : mCommandedEGR);
                break;
            case BAROMETRIC_PRESSURE:
                mBarometricPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("大气压", TextUtils.isEmpty(mBarometricPressure) ? "" : mBarometricPressure, R.drawable.icon_dqy));
                entity.setBarometricPressure(TextUtils.isEmpty(mBarometricPressure) ? "" : mBarometricPressure);
                break;

            case THROTTLE_POS:
                mThrottlePos = command.getFormattedResult();
                data.add(new OBDTripEntity("节气门位置", TextUtils.isEmpty(mThrottlePos) ? "" : mThrottlePos, R.drawable.icon_jqmwz));
                entity.setRelThottlePos(TextUtils.isEmpty(mThrottlePos) ? "" : mThrottlePos);
                break;

            case TIMING_ADVANCE:
                mTimingAdvance = command.getFormattedResult();
                data.add(new OBDTripEntity("定时提前", TextUtils.isEmpty(mTimingAdvance) ? "" : mTimingAdvance, R.drawable.icon_timing_advance));
                entity.setTimingAdvance(TextUtils.isEmpty(mTimingAdvance) ? "" : mTimingAdvance);
                break;

            case PERMANENT_TROUBLE_CODES:
                mPermanentTroubleCode = command.getFormattedResult();
                data.add(new OBDTripEntity("永久性故障代码", TextUtils.isEmpty(mPermanentTroubleCode) ? "" : mPermanentTroubleCode, R.drawable.icon_troublecode_three));
                entity.setPermanentTroubleCode(TextUtils.isEmpty(mPermanentTroubleCode) ? "" : mPermanentTroubleCode);
                break;

            case PENDING_TROUBLE_CODES:
                mPendingTroubleCode = command.getFormattedResult();
                data.add(new OBDTripEntity("未决故障代码", TextUtils.isEmpty(mPendingTroubleCode) ? "" : mPendingTroubleCode, R.drawable.icon_troublecode_two));
                entity.setPendingTroubleCode(TextUtils.isEmpty(mPendingTroubleCode) ? "" : mPendingTroubleCode);
                break;

            case EQUIV_RATIO:
                mEquivRatio = command.getFormattedResult();
                data.add(new OBDTripEntity("指令当量比", TextUtils.isEmpty(mEquivRatio) ? "" : mEquivRatio, R.drawable.icon_equiev_tatio));
                entity.setEquivRatio(TextUtils.isEmpty(mEquivRatio) ? "" : mEquivRatio);
                break;

            case DISTANCE_TRAVELED_AFTER_CODES_CLEARED:
                mDistanceTraveledAfterCodesCleared = command.getFormattedResult();
                data.add(new OBDTripEntity("代码清除后的距离", TextUtils.isEmpty(mDistanceTraveledAfterCodesCleared) ? "" : mDistanceTraveledAfterCodesCleared, R.drawable.icon_distance_code_clear));
                entity.setDistanceTraveledAfterCodesCleared(mDistanceTraveledAfterCodesCleared);
                break;

            case CONTROL_MODULE_VOLTAGE:
                mControlModuleVoltage = command.getFormattedResult();
                data.add(new OBDTripEntity("控制模组电压", TextUtils.isEmpty(mControlModuleVoltage) ? "" : mControlModuleVoltage, R.drawable.icon_control_model_voltage));
                entity.setControlModuleVoltage(TextUtils.isEmpty(mControlModuleVoltage) ? "" : mControlModuleVoltage);
                break;

            case FUEL_RAIL_PRESSURE:
                mFuelRailPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油分供管压力", TextUtils.isEmpty(mFuelRailPressure) ? "" : mFuelRailPressure, R.drawable.icon_fule_rail_pressure));
                entity.setFuelRailPressure(TextUtils.isEmpty(mFuelRailPressure) ? "" : mFuelRailPressure);
                break;
            case FUEL_RAIL_PRESSURE_manifold:
                mFuelRailPressurevacuum = command.getFormattedResult();
                data.add(new OBDTripEntity("相对于歧管真空的燃油分供管压力", TextUtils.isEmpty(mFuelRailPressurevacuum) ? "" : mFuelRailPressurevacuum, R.drawable.icon_fyg_yl));
                entity.setFuelRailPressurevacuum(TextUtils.isEmpty(mFuelRailPressurevacuum) ? "" : mFuelRailPressurevacuum);
                break;
            case VIN:
                mVehicleIdentificationNumber = command.getFormattedResult();
                data.add(new OBDTripEntity("车辆识别号（VIN）", TextUtils.isEmpty(mVehicleIdentificationNumber) ? "" : mVehicleIdentificationNumber, R.drawable.icon_vin));
                entity.setVehicleIdentificationNumber(TextUtils.isEmpty(mVehicleIdentificationNumber) ? "" : mVehicleIdentificationNumber);
                break;

            case DISTANCE_TRAVELED_MIL_ON:
                mDistanceTraveledMilOn = command.getFormattedResult();
                data.add(new OBDTripEntity("MIL开时行驶的距离", TextUtils.isEmpty(mDistanceTraveledMilOn) ? "" : mDistanceTraveledMilOn, R.drawable.icon_mil_jl));
                entity.setDistanceTraveledMilOn(mDistanceTraveledMilOn);
                break;

            case DTC_NUMBER:
                mDtcNumber = command.getFormattedResult();
                data.add(new OBDTripEntity("自从DTC清除后的监控状态", TextUtils.isEmpty(mDtcNumber) ? "" : mDtcNumber, R.drawable.icon_dtc_number));
                entity.setDtcNumber(TextUtils.isEmpty(mDtcNumber) ? "" : mDtcNumber);
                break;

            case TIME_SINCE_TC_CLEARED:
                mTimeSinceTcClear = command.getFormattedResult();
                break;

            case REL_THROTTLE_POS:
                mRelThottlePos = command.getFormattedResult();
                break;

            case ABS_LOAD:
                mAbsLoad = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对负荷", TextUtils.isEmpty(mAbsLoad) ? "" : mAbsLoad, R.drawable.icon_abs));
                entity.setAbsLoad(TextUtils.isEmpty(mAbsLoad) ? "" : mAbsLoad);
                break;

            case AIR_FUEL_RATIO:
                mAirFuelRatio = command.getFormattedResult();
                data.add(new OBDTripEntity("空燃比", TextUtils.isEmpty(mAirFuelRatio) ? "" : mAirFuelRatio, R.drawable.icon_krb));
                entity.setAirFuelRatio(TextUtils.isEmpty(mAirFuelRatio) ? "" : mAirFuelRatio);
                break;

            case WIDEBAND_AIR_FUEL_RATIO:
                mWideBandAirFuelRatio = command.getFormattedResult();
                data.add(new OBDTripEntity("宽带空燃比", TextUtils.isEmpty(mWideBandAirFuelRatio) ? "" : mWideBandAirFuelRatio, R.drawable.icon_af));
                entity.setWideBandAirFuelRatio(TextUtils.isEmpty(mWideBandAirFuelRatio) ? "" : mWideBandAirFuelRatio);
                break;

            case DESCRIBE_PROTOCOL:
                mDescribeProtocol = command.getFormattedResult();
                data.add(new OBDTripEntity("描述协议", TextUtils.isEmpty(mDescribeProtocol) ? "" : mDescribeProtocol, R.drawable.icon_msxy));
                entity.setDescribeProtocol(TextUtils.isEmpty(mDescribeProtocol) ? "" : mDescribeProtocol);
                break;
            case DESCRIBE_PROTOCOL_NUMBER:
                mDescribeProtocolNumber = command.getFormattedResult();
                data.add(new OBDTripEntity("描述协议编号", TextUtils.isEmpty(mDescribeProtocolNumber) ? "" : mDescribeProtocolNumber, R.drawable.icon_xybh));
                entity.setDescribeProtocolNumber(TextUtils.isEmpty(mDescribeProtocolNumber) ? "" : mDescribeProtocolNumber);
                break;
            case IGNITION_MONITOR:
                mIgnitionMonitor = command.getFormattedResult();
                data.add(new OBDTripEntity("点火监视器", TextUtils.isEmpty(mIgnitionMonitor) ? "" : mIgnitionMonitor, R.drawable.icon_fire));
                entity.setIgnitionMonitor(TextUtils.isEmpty(mIgnitionMonitor) ? "" : mIgnitionMonitor);
                break;
        }
        datas.addAll(data);
        setTripMap(datas);
        setOBDJson(entity);
    }

    private void getFuelTypeValue(String fuelType) {
        float fuelTypeValue = 0;
        if (FuelType.GASOLINE.getDescription().equals(fuelType)) {
            fuelTypeValue = 14.7f;
            gramToLitre = GRAM_TO_LITRE_GASOLIN;
        } else if (FuelType.PROPANE.getDescription().equals(fuelType)) {
            fuelTypeValue = 15.5f;
            gramToLitre = GRAM_TO_LITRE_PROPANE;
        } else if (FuelType.ETHANOL.getDescription().equals(fuelType)) {
            fuelTypeValue = 9f;
            gramToLitre = GRAM_TO_LITRE_ETHANOL;
        } else if (FuelType.METHANOL.getDescription().equals(fuelType)) {
            fuelTypeValue = 6.4f;
            gramToLitre = GRAM_TO_LITRE_METHANOL;
        } else if (FuelType.DIESEL.getDescription().equals(fuelType)) {
            fuelTypeValue = 14.6f;
            gramToLitre = GRAM_TO_LITRE_DIESEL;
        } else if (FuelType.CNG.getDescription().equals(fuelType)) {
            fuelTypeValue = 17.2f;
            gramToLitre = GRAM_TO_LITRE_CNG;
        }
        if (fuelTypeValue != 0) {
            ObdPreferences.get(sContext.getApplicationContext()).setFuelType(mFuelTypeValue);
            mFuelTypeValue = fuelTypeValue;
        }
        data.add(new OBDTripEntity("燃料类型", mFuelTypeValue + "", R.drawable.icon_fuel_type_record));
        entity.setFuelTypeValue(String.valueOf(mFuelTypeValue));
    }

    public void setmIsTempPressureSupported(boolean mIsTempPressureSupported) {
        this.mIsTempPressureSupported = mIsTempPressureSupported;
    }

    public boolean ismIsTempPressureSupported() {
        return mIsTempPressureSupported;
    }

    public String getmAmbientAirTemp() {
        return mAmbientAirTemp;
    }

    public String getmEngineCoolantTemp() {
        return mEngineCoolantTemp;
    }

    public String getmEngineOilTemp() {
        return mEngineOilTemp;
    }

    public String getmFuelConsumptionRate() {
        return mFuelConsumptionRate;
    }

    public String getmFuelSystemStatus() {
        return mFuelSystemStatus;
    }


    public String getmFuelPressure() {
        return mFuelPressure;
    }

    public String getmCommandedEGR() {
        return mCommandedEGR;
    }

    public String getmEngineLoad() {
        return mEngineLoad;
    }

    public String getmBarometricPressure() {
        return mBarometricPressure;
    }

    public String getmThrottlePos() {
        return mThrottlePos;
    }

    public String getmTimingAdvance() {
        return mTimingAdvance;
    }

    public String getmPermanentTroubleCode() {
        return mPermanentTroubleCode;
    }

    public String getmPendingTroubleCode() {
        return mPendingTroubleCode;
    }

    public String getmEquivRatio() {
        return mEquivRatio;
    }

    public String getmDistanceTraveledAfterCodesCleared() {
        return mDistanceTraveledAfterCodesCleared;
    }

    public String getmControlModuleVoltage() {
        return mControlModuleVoltage;
    }

    public String getmFuelRailPressure() {
        return mFuelRailPressure;
    }

    public String getmFuelRailPressurevacuum() {
        return mFuelRailPressurevacuum;
    }

    public String getmVehicleIdentificationNumber() {
        return mVehicleIdentificationNumber;
    }

    public String getmDistanceTraveledMilOn() {
        return mDistanceTraveledMilOn;
    }

    public String getmDtcNumber() {
        return mDtcNumber;
    }

    public String getmTimeSinceTcClear() {
        return mTimeSinceTcClear;
    }

    public String getmRelThottlePos() {
        return mRelThottlePos;
    }

    public String getmAbsLoad() {
        return mAbsLoad;
    }

    public String getmAirFuelRatio() {
        return mAirFuelRatio;
    }

    public String getmWideBandAirFuelRatio() {
        return mWideBandAirFuelRatio;
    }

    public String getmDescribeProtocol() {
        return mDescribeProtocol;
    }

    public String getmDescribeProtocolNumber() {
        return mDescribeProtocolNumber;
    }

    public String getmIgnitionMonitor() {
        return mIgnitionMonitor;
    }

    public List<OBDTripEntity> getTripMap() {
        return data;
    }

    public void setTripMap(List<OBDTripEntity> data) {
        this.datas = data;
    }

    public OBDJsonTripEntity getOBDJson() {
        return entity;
    }

    public void setOBDJson(OBDJsonTripEntity entity) {
        this.entity = entity;
    }
}
