package com.example.obdandroid.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.example.obdandroid.ui.entity.DefaultCodeEntity;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.enums.FuelType;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.trip.OBDJsonTripEntity;
import com.sohrab.obd.reader.trip.OBDTripEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.obdandroid.config.APIConfig.SERVER_URL;
import static com.example.obdandroid.config.APIConfig.getInquireAboutFaultCodeDetails_URL;
import static com.example.obdandroid.config.OBDConfig.ABS_EVA_SYSTEM_VAPOR_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.ABS_LOAD;
import static com.example.obdandroid.config.OBDConfig.ABS_THROTTLE_POS_B;
import static com.example.obdandroid.config.OBDConfig.ABS_THROTTLE_POS_C;
import static com.example.obdandroid.config.OBDConfig.ACC_PEDAL_POS_D;
import static com.example.obdandroid.config.OBDConfig.ACC_PEDAL_POS_E;
import static com.example.obdandroid.config.OBDConfig.ACC_PEDAL_POS_F;
import static com.example.obdandroid.config.OBDConfig.ACTUAL_ENGINE_TORQUE;
import static com.example.obdandroid.config.OBDConfig.AIR_FUEL_RATIO;
import static com.example.obdandroid.config.OBDConfig.AIR_INTAKE_TEMPERATURE;
import static com.example.obdandroid.config.OBDConfig.AMBIENT_AIR_TEMP;
import static com.example.obdandroid.config.OBDConfig.BAROMETRIC_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.CONTROL_MODULE_VOLTAGE;
import static com.example.obdandroid.config.OBDConfig.Catalyst_Temperature_Bank_1_Sensor_1;
import static com.example.obdandroid.config.OBDConfig.Catalyst_Temperature_Bank_1_Sensor_2;
import static com.example.obdandroid.config.OBDConfig.Catalyst_Temperature_Bank_2_Sensor_1;
import static com.example.obdandroid.config.OBDConfig.Catalyst_Temperature_Bank_2_Sensor_2;
import static com.example.obdandroid.config.OBDConfig.DESCRIBE_PROTOCOL;
import static com.example.obdandroid.config.OBDConfig.DESCRIBE_PROTOCOL_NUMBER;
import static com.example.obdandroid.config.OBDConfig.DISTANCE_TRAVELED_AFTER_CODES_CLEARED;
import static com.example.obdandroid.config.OBDConfig.DISTANCE_TRAVELED_MIL_ON;
import static com.example.obdandroid.config.OBDConfig.DPF_TEMP;
import static com.example.obdandroid.config.OBDConfig.DRIVER_ENGINE_TORQUE;
import static com.example.obdandroid.config.OBDConfig.DTC_NUMBER;
import static com.example.obdandroid.config.OBDConfig.EGR;
import static com.example.obdandroid.config.OBDConfig.EGR_ERROR;
import static com.example.obdandroid.config.OBDConfig.ENGINE_COOLANT_TEMP;
import static com.example.obdandroid.config.OBDConfig.ENGINE_FRICTION_PERCENT_TORQUE;
import static com.example.obdandroid.config.OBDConfig.ENGINE_LOAD;
import static com.example.obdandroid.config.OBDConfig.ENGINE_OIL_TEMP;
import static com.example.obdandroid.config.OBDConfig.ENGINE_REFERENCE_TORQUE;
import static com.example.obdandroid.config.OBDConfig.ENGINE_RPM;
import static com.example.obdandroid.config.OBDConfig.ENGINE_RUNTIME;
import static com.example.obdandroid.config.OBDConfig.ETHANOL_FUEL_RATE;
import static com.example.obdandroid.config.OBDConfig.EVAPORATE_PURGE;
import static com.example.obdandroid.config.OBDConfig.EVA_SYSTEM_VAPOR_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.FUEL_CONSUMPTION_RATE;
import static com.example.obdandroid.config.OBDConfig.FUEL_LEVEL;
import static com.example.obdandroid.config.OBDConfig.FUEL_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.FUEL_RAIL_ABS_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.FUEL_RAIL_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.FUEL_RAIL_PRESSURE_manifold;
import static com.example.obdandroid.config.OBDConfig.FUEL_SYSTEM_STATUS;
import static com.example.obdandroid.config.OBDConfig.FUEL_TYPE;
import static com.example.obdandroid.config.OBDConfig.GRAM_TO_LITRE_CNG;
import static com.example.obdandroid.config.OBDConfig.GRAM_TO_LITRE_DIESEL;
import static com.example.obdandroid.config.OBDConfig.GRAM_TO_LITRE_ETHANOL;
import static com.example.obdandroid.config.OBDConfig.GRAM_TO_LITRE_GASOLINE;
import static com.example.obdandroid.config.OBDConfig.GRAM_TO_LITRE_METHANOL;
import static com.example.obdandroid.config.OBDConfig.GRAM_TO_LITRE_PROPANE;
import static com.example.obdandroid.config.OBDConfig.HY_BATTERY_PACK_LIFE;
import static com.example.obdandroid.config.OBDConfig.IGNITION_MONITOR;
import static com.example.obdandroid.config.OBDConfig.INTAKE_MANIFOLD_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.LONG_A_BANK1_B_BANK3;
import static com.example.obdandroid.config.OBDConfig.LONG_A_BANK2_B_BANK4;
import static com.example.obdandroid.config.OBDConfig.LONG_TERM_BANK_1;
import static com.example.obdandroid.config.OBDConfig.LONG_TERM_BANK_2;
import static com.example.obdandroid.config.OBDConfig.MAF;
import static com.example.obdandroid.config.OBDConfig.MAX_AIR_FLOW_MASS_RATE;
import static com.example.obdandroid.config.OBDConfig.PENDING_TROUBLE_CODES;
import static com.example.obdandroid.config.OBDConfig.PERMANENT_TROUBLE_CODES;
import static com.example.obdandroid.config.OBDConfig.REL_ACCELERATOR_PEDAL_POS;
import static com.example.obdandroid.config.OBDConfig.REL_THROTTLE_POS;
import static com.example.obdandroid.config.OBDConfig.SHORT_A_BANK1_B_BANK3;
import static com.example.obdandroid.config.OBDConfig.SHORT_A_BANK2_B_BANK4;
import static com.example.obdandroid.config.OBDConfig.SHORT_TERM_BANK_1;
import static com.example.obdandroid.config.OBDConfig.SHORT_TERM_BANK_2;
import static com.example.obdandroid.config.OBDConfig.SPEED_GAP;
import static com.example.obdandroid.config.OBDConfig.SYSTEM_VAPOR_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.THROTTLE_ACTUATOR;
import static com.example.obdandroid.config.OBDConfig.THROTTLE_POS;
import static com.example.obdandroid.config.OBDConfig.TIME_SINCE_TC_CLEARED;
import static com.example.obdandroid.config.OBDConfig.TIME_TRAVELED_MIL_ON;
import static com.example.obdandroid.config.OBDConfig.TIMING_ADVANCE;
import static com.example.obdandroid.config.OBDConfig.TROUBLE_CODES;
import static com.example.obdandroid.config.OBDConfig.VEHICLE_SPEED;
import static com.example.obdandroid.config.OBDConfig.VIN;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_1;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_2;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_3;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_4;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_5;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_6;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_7;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_8;
import static com.example.obdandroid.config.OBDConfig.WarmUpSinceCodesCleared;

/**
 * 作者：Jealous
 * 日期：2021/3/6
 * 描述：
 */
public class CheckRecord implements Serializable {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    @SuppressLint("StaticFieldLeak")
    private static CheckRecord sInstance;
    private int engineRpmMax = 0;
    private String engineRpm = "";
    private int speed = -1;
    private int speedMax = 0;
    private final long tripStartTime;
    private float idlingDuration;
    private float drivingDuration;
    private float AverageSpeed;
    private long mAddSpeed;
    private long mSpeedCount;
    private float mDistanceTravel;
    private int mRapidAccTimes = 0;
    private int mRapidDecTimes = 0;
    private float mInsFuelConsumption = 0.0f;
    private float mDrivingFuelConsumption = 0.0f;
    private float mIdlingFuelConsumption = 0.0f;
    private long mLastTimeStamp;
    private float mFuelTypeValue = 14.7f; //默认值为汽油燃油比
    private float mDrivingMaf;
    private int mDrivingMafCount;
    private float mIdleMaf;
    private int mIdleMafCount;
    private int mSecondAgoSpeed;
    private float gramToLitre = GRAM_TO_LITRE_GASOLINE;
    private float mIntakeAirTemp = 0.0f;
    private float mIntakePressure = 0.0f;

    private String mEngineCoolantTemp;
    private String mEngineOilTemp;
    private String mFuelConsumptionRate;
    private String mFuelPressure;
    private String mThrottlePos;
    private String mFuelRailPressure = "";
    private static String mToken;
    private final StringBuilder DefaultCode;
    private final StringBuilder permanentCode;
    private final StringBuilder pendingCode;
    private List<OBDTripEntity> entityList = new ArrayList<>();
    private final List<OBDTripEntity> data = new ArrayList<>();
    private OBDJsonTripEntity entity = new OBDJsonTripEntity();

    private CheckRecord() {
        tripStartTime = System.currentTimeMillis();
        DefaultCode = new StringBuilder();
        permanentCode = new StringBuilder();
        pendingCode = new StringBuilder();
    }

    public static CheckRecord getTriRecode(Context context, String token) {
        sContext = context;
        mToken = token;
        if (sInstance == null)
            sInstance = new CheckRecord();
        return sInstance;
    }

    public void clear() {
        sInstance = null;
    }

    @SuppressLint("SetTextI18n")
    public void updateTrip(String name, ObdCommand command) {
        switch (name) {
            case VEHICLE_SPEED://时速
                setSpeed(((SpeedCommand) command).getMetricSpeed());
                break;
            case ENGINE_RPM://转速
                setEngineRpm(command.getCalculatedResult());
                break;
            case MAF://MAF空气流量速率
                float mMassAirFlow = Float.parseFloat(command.getFormattedResult());
                findInsFuelConsumption(mMassAirFlow);
                break;
            case ENGINE_RUNTIME:
                String engineRuntime = command.getFormattedResult();
                data.add(new OBDTripEntity("引擎启动后的运行时间", TextUtils.isEmpty(engineRuntime) ? "N/A" : engineRuntime));
                entity.setEngineRuntime(TextUtils.isEmpty(engineRuntime) ? "N/A" : engineRuntime);
                break;
            case FUEL_LEVEL:
                String mFuelLevel = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油油位", TextUtils.isEmpty(mFuelLevel) ? "N/A" : mFuelLevel));
                entity.setFuelLevel(TextUtils.isEmpty(mFuelLevel) ? "N/A" : mFuelLevel);
                break;
            case FUEL_TYPE:
                if (ObdPreferences.get(sContext.getApplicationContext()).getFuelType() == 0)
                    getFuelTypeValue(command.getFormattedResult());
                break;
            case INTAKE_MANIFOLD_PRESSURE:
                mIntakePressure = Float.parseFloat(command.getCalculatedResult());
                data.add(new OBDTripEntity("进气歧管压力", mIntakePressure + " kpa"));
                entity.setIntakePressure(mIntakePressure + " kpa");
                calculateMaf();
                break;
            case AIR_INTAKE_TEMPERATURE:
                mIntakeAirTemp = Float.parseFloat(command.getCalculatedResult()) + 273.15f;
                data.add(new OBDTripEntity("油箱空气温度", mIntakeAirTemp + " ℃"));
                entity.setIntakeAirTemp(mIntakeAirTemp + " ℃");
                calculateMaf();
                break;
            case AMBIENT_AIR_TEMP:
                String mAmbientAirTemp = command.getFormattedResult();
                data.add(new OBDTripEntity("环境空气温度", TextUtils.isEmpty(mAmbientAirTemp) ? "N/A" : mAmbientAirTemp + " ℃"));
                entity.setAmbientAirTemp(TextUtils.isEmpty(mAmbientAirTemp) ? "N/A" : mAmbientAirTemp);
                break;
            case ENGINE_COOLANT_TEMP:
                mEngineCoolantTemp = command.getFormattedResult();
                data.add(new OBDTripEntity("引擎冷媒温度", TextUtils.isEmpty(mEngineCoolantTemp) ? "N/A" : mEngineCoolantTemp + " ℃"));
                entity.setEngineCoolantTemp(TextUtils.isEmpty(mEngineCoolantTemp) ? "N/A" : mEngineCoolantTemp);
                break;

            case ENGINE_OIL_TEMP:
                mEngineOilTemp = command.getFormattedResult();
                data.add(new OBDTripEntity("引擎油温", TextUtils.isEmpty(mEngineOilTemp) ? "N/A" : mEngineOilTemp));
                entity.setEngineOilTemp(TextUtils.isEmpty(mEngineOilTemp) ? "N/A" : mEngineOilTemp);
                break;

            case FUEL_CONSUMPTION_RATE:
                mFuelConsumptionRate = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油消耗率", TextUtils.isEmpty(mFuelConsumptionRate) ? "N/A" : mFuelConsumptionRate));
                entity.setFuelConsumptionRate(TextUtils.isEmpty(mFuelConsumptionRate) ? "N/A" : mFuelConsumptionRate);
                break;
            case FUEL_SYSTEM_STATUS:
                String mFuelSystemStatus = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油系统状态", TextUtils.isEmpty(mFuelSystemStatus) ? "N/A" : mFuelSystemStatus));
                entity.setFuelSystemStatus(mFuelSystemStatus);
                break;
            case FUEL_PRESSURE:
                mFuelPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("油压", TextUtils.isEmpty(mFuelPressure) ? "N/A" : mFuelPressure));
                entity.setFuelPressure(TextUtils.isEmpty(mFuelPressure) ? "N/A" : mFuelPressure);
                break;
            case ENGINE_LOAD:
                String mEngineLoad = command.getFormattedResult();
                data.add(new OBDTripEntity("引擎载荷", TextUtils.isEmpty(mEngineLoad) ? "N/A" : mEngineLoad));
                entity.setEngineLoad(TextUtils.isEmpty(mEngineLoad) ? "N/A" : mEngineLoad);
                break;
            case EGR:
                String mEGR = command.getFormattedResult();
                data.add(new OBDTripEntity("废气循环", TextUtils.isEmpty(mEGR) ? "N/A" : mEGR));
                entity.setRmCommandedEGR(TextUtils.isEmpty(mEGR) ? "N/A" : mEGR);
                break;
            case EGR_ERROR:
                String mEGRError = command.getFormattedResult();
                data.add(new OBDTripEntity("废气循环错误", TextUtils.isEmpty(mEGRError) ? "N/A" : mEGRError));
                entity.setEgrError(TextUtils.isEmpty(mEGRError) ? "N/A" : mEGRError);
                break;
            case EVAPORATE_PURGE:
                String mEvaporatePurge = command.getFormattedResult();
                data.add(new OBDTripEntity("蒸发净化", TextUtils.isEmpty(mEvaporatePurge) ? "N/A" : mEvaporatePurge));
                entity.setEvaporaivePurge(TextUtils.isEmpty(mEvaporatePurge) ? "N/A" : mEvaporatePurge);
                break;
            case BAROMETRIC_PRESSURE:
                String mBarometricPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对大气压", TextUtils.isEmpty(mBarometricPressure) ? "N/A" : mBarometricPressure));
                entity.setBarometricPressure(TextUtils.isEmpty(mBarometricPressure) ? "N/A" : mBarometricPressure);
                break;
            case THROTTLE_POS:
                mThrottlePos = command.getFormattedResult();
                data.add(new OBDTripEntity("油门位置", TextUtils.isEmpty(mThrottlePos) ? "N/A" : mThrottlePos));
                entity.setThottlePos(TextUtils.isEmpty(mThrottlePos) ? "N/A" : mThrottlePos);
                break;
            case TIMING_ADVANCE:
                String mTimingAdvance = command.getFormattedResult();
                data.add(new OBDTripEntity("点火提前值", TextUtils.isEmpty(mTimingAdvance) ? "N/A" : mTimingAdvance));
                entity.setTimingAdvance(TextUtils.isEmpty(mTimingAdvance) ? "N/A" : mTimingAdvance);
                break;
            case WarmUpSinceCodesCleared:
                String mWarmUpSinceCodesCleared = command.getFormattedResult();
                data.add(new OBDTripEntity("代码清除后的预热", TextUtils.isEmpty(mWarmUpSinceCodesCleared) ? "N/A" : mWarmUpSinceCodesCleared));
                entity.setWarmUpSinceCodesCleared(TextUtils.isEmpty(mWarmUpSinceCodesCleared) ? "N/A" : mWarmUpSinceCodesCleared);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_1:
                String mWideBandAirFuelRatioOne = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器1 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioOne) ? "N/A" : mWideBandAirFuelRatioOne));
                entity.setWideBandAirFuelRatioOne(TextUtils.isEmpty(mWideBandAirFuelRatioOne) ? "N/A" : mWideBandAirFuelRatioOne);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_2:
                String mWideBandAirFuelRatioTwo = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器2 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioTwo) ? "N/A" : mWideBandAirFuelRatioTwo));
                entity.setWideBandAirFuelRatioTwo(TextUtils.isEmpty(mWideBandAirFuelRatioTwo) ? "N/A" : mWideBandAirFuelRatioTwo);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_3:
                String mWideBandAirFuelRatioThree = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器3 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioThree) ? "N/A" : mWideBandAirFuelRatioThree));
                entity.setWideBandAirFuelRatioThree(TextUtils.isEmpty(mWideBandAirFuelRatioThree) ? "N/A" : mWideBandAirFuelRatioThree);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_4:
                String mWideBandAirFuelRatioFour = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器4 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioFour) ? "N/A" : mWideBandAirFuelRatioFour));
                entity.setWideBandAirFuelRatioFour(TextUtils.isEmpty(mWideBandAirFuelRatioFour) ? "N/A" : mWideBandAirFuelRatioFour);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_5:
                String mWideBandAirFuelRatioFive = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器5 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioFive) ? "N/A" : mWideBandAirFuelRatioFive));
                entity.setWideBandAirFuelRatioFive(TextUtils.isEmpty(mWideBandAirFuelRatioFive) ? "N/A" : mWideBandAirFuelRatioFive);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_6:
                String mWideBandAirFuelRatioSix = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器6 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioSix) ? "N/A" : mWideBandAirFuelRatioSix));
                entity.setWideBandAirFuelRatioSix(TextUtils.isEmpty(mWideBandAirFuelRatioSix) ? "N/A" : mWideBandAirFuelRatioSix);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_7:
                String mWideBandAirFuelRatioSeven = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器7 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioSeven) ? "N/A" : mWideBandAirFuelRatioSeven));
                entity.setWideBandAirFuelRatioSeven(TextUtils.isEmpty(mWideBandAirFuelRatioSeven) ? "N/A" : mWideBandAirFuelRatioSeven);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_8:
                String mWideBandAirFuelRatioEight = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器8 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioEight) ? "N/A" : mWideBandAirFuelRatioEight));
                entity.setWideBandAirFuelRatioEight(TextUtils.isEmpty(mWideBandAirFuelRatioEight) ? "N/A" : mWideBandAirFuelRatioEight);
                break;
            case Catalyst_Temperature_Bank_1_Sensor_1:
                String mCatalystTemperatureBank1Sensor1 = command.getFormattedResult();
                data.add(new OBDTripEntity("催化剂温度:Bank1,感测器1", TextUtils.isEmpty(mCatalystTemperatureBank1Sensor1) ? "N/A" : mCatalystTemperatureBank1Sensor1));
                entity.setCatalystTemperatureBank1Sensor1(TextUtils.isEmpty(mCatalystTemperatureBank1Sensor1) ? "N/A" : mCatalystTemperatureBank1Sensor1);
                break;
            case Catalyst_Temperature_Bank_2_Sensor_1:
                String mCatalystTemperatureBank2Sensor1 = command.getFormattedResult();
                data.add(new OBDTripEntity("催化剂温度:Bank2,感测器1", TextUtils.isEmpty(mCatalystTemperatureBank2Sensor1) ? "N/A" : mCatalystTemperatureBank2Sensor1));
                entity.setCatalystTemperatureBank2Sensor1(TextUtils.isEmpty(mCatalystTemperatureBank2Sensor1) ? "N/A" : mCatalystTemperatureBank2Sensor1);
                break;
            case Catalyst_Temperature_Bank_1_Sensor_2:
                String mCatalystTemperatureBank1Sensor2 = command.getFormattedResult();
                data.add(new OBDTripEntity("催化剂温度:Bank1,感测器2", TextUtils.isEmpty(mCatalystTemperatureBank1Sensor2) ? "N/A" : mCatalystTemperatureBank1Sensor2));
                entity.setCatalystTemperatureBank1Sensor2(TextUtils.isEmpty(mCatalystTemperatureBank1Sensor2) ? "N/A" : mCatalystTemperatureBank1Sensor2);
                break;
            case Catalyst_Temperature_Bank_2_Sensor_2:
                String mCatalystTemperatureBank2Sensor2 = command.getFormattedResult();
                data.add(new OBDTripEntity("催化剂温度:Bank2,感测器2", TextUtils.isEmpty(mCatalystTemperatureBank2Sensor2) ? "N/A" : mCatalystTemperatureBank2Sensor2));
                entity.setCatalystTemperatureBank2Sensor2(TextUtils.isEmpty(mCatalystTemperatureBank2Sensor2) ? "N/A" : mCatalystTemperatureBank2Sensor2);
                break;
            case ABS_THROTTLE_POS_B:
                String mAbsThrottlePosb = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对油门位置B", TextUtils.isEmpty(mAbsThrottlePosb) ? "N/A" : mAbsThrottlePosb));
                entity.setAbsThrottlePosb(TextUtils.isEmpty(mAbsThrottlePosb) ? "N/A" : mAbsThrottlePosb);
                break;
            case ABS_THROTTLE_POS_C:
                String mAbsThrottlePosc = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对油门位置C", TextUtils.isEmpty(mAbsThrottlePosc) ? "N/A" : mAbsThrottlePosc));
                entity.setAbsThrottlePosc(TextUtils.isEmpty(mAbsThrottlePosc) ? "N/A" : mAbsThrottlePosc);
                break;
            case ACC_PEDAL_POS_D:
                String mAccPedalPosd = command.getFormattedResult();
                data.add(new OBDTripEntity("加速踏板位置D", TextUtils.isEmpty(mAccPedalPosd) ? "N/A" : mAccPedalPosd));
                entity.setAccPedalPosd(TextUtils.isEmpty(mAccPedalPosd) ? "N/A" : mAccPedalPosd);
                break;
            case ACC_PEDAL_POS_E:
                String mAccPedalPose = command.getFormattedResult();
                data.add(new OBDTripEntity("加速踏板位置E", TextUtils.isEmpty(mAccPedalPose) ? "N/A" : mAccPedalPose));
                entity.setAccPedalPose(TextUtils.isEmpty(mAccPedalPose) ? "N/A" : mAccPedalPose);
                break;
            case ACC_PEDAL_POS_F:
                String mAccPedalPosf = command.getFormattedResult();
                data.add(new OBDTripEntity("加速踏板位置F", TextUtils.isEmpty(mAccPedalPosf) ? "N/A" : mAccPedalPosf));
                entity.setAccPedalPosf(TextUtils.isEmpty(mAccPedalPosf) ? "N/A" : mAccPedalPosf);
                break;
            case THROTTLE_ACTUATOR:
                String mThrottleActuator = command.getFormattedResult();
                data.add(new OBDTripEntity("油门执行器控制值", TextUtils.isEmpty(mThrottleActuator) ? "N/A" : mThrottleActuator));
                entity.setThrottleActuator(TextUtils.isEmpty(mThrottleActuator) ? "N/A" : mThrottleActuator);
                break;
            case TIME_TRAVELED_MIL_ON:
                String mTimeRunwithMILOn = command.getFormattedResult();
                data.add(new OBDTripEntity("MIL灯亮的行驶时间", TextUtils.isEmpty(mTimeRunwithMILOn) ? "N/A" : mTimeRunwithMILOn));
                entity.setTimeRunWithMILOn(TextUtils.isEmpty(mTimeRunwithMILOn) ? "N/A" : mTimeRunwithMILOn);
                break;
            case MAX_AIR_FLOW_MASS_RATE:
                String mMaxAirFlowMassRate = command.getFormattedResult();
                data.add(new OBDTripEntity("质量空气流量计的最大空气流率", TextUtils.isEmpty(mMaxAirFlowMassRate) ? "N/A" : mMaxAirFlowMassRate));
                entity.setMaxAirFlowMassRate(TextUtils.isEmpty(mMaxAirFlowMassRate) ? "N/A" : mMaxAirFlowMassRate);
                break;
            case ETHANOL_FUEL_RATE:
                String mEthanolFuelRate = command.getFormattedResult();
                data.add(new OBDTripEntity("乙醇燃料百分比", TextUtils.isEmpty(mEthanolFuelRate) ? "N/A" : mEthanolFuelRate));
                entity.setEthanolFuelRate(TextUtils.isEmpty(mEthanolFuelRate) ? "N/A" : mEthanolFuelRate);
                break;
            case ABS_EVA_SYSTEM_VAPOR_PRESSURE:
                String mAbsEvapSystemVaporPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("蒸发系统绝对蒸气压力", TextUtils.isEmpty(mAbsEvapSystemVaporPressure) ? "N/A" : mAbsEvapSystemVaporPressure));
                entity.setAbsEvapSystemVaporPressure(TextUtils.isEmpty(mAbsEvapSystemVaporPressure) ? "N/A" : mAbsEvapSystemVaporPressure);
                break;
            case EVA_SYSTEM_VAPOR_PRESSURE:
                String mEvapSystemVaporPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("蒸发系统(相对)蒸气压力", TextUtils.isEmpty(mEvapSystemVaporPressure) ? "N/A" : mEvapSystemVaporPressure));
                entity.setEvapSystemVaporPressure(TextUtils.isEmpty(mEvapSystemVaporPressure) ? "N/A" : mEvapSystemVaporPressure);
                break;
            case SHORT_A_BANK1_B_BANK3:
                String mShortA_BANK1_B_BANK3 = command.getFormattedResult();
                data.add(new OBDTripEntity("第二侧氧气侦测器短期修正,A:bank 1,B:bank 3", TextUtils.isEmpty(mShortA_BANK1_B_BANK3) ? "N/A" : mShortA_BANK1_B_BANK3));
                entity.setShortA_BANK1_B_BANK3(TextUtils.isEmpty(mShortA_BANK1_B_BANK3) ? "N/A" : mShortA_BANK1_B_BANK3);
                break;
            case LONG_A_BANK1_B_BANK3:
                String mLong_A_BANK1_B_BANK3 = command.getFormattedResult();
                data.add(new OBDTripEntity("第二侧氧气侦测器长期修正,A:bank 1,B:bank 3", TextUtils.isEmpty(mLong_A_BANK1_B_BANK3) ? "N/A" : mLong_A_BANK1_B_BANK3));
                entity.setLong_A_BANK1_B_BANK3(TextUtils.isEmpty(mLong_A_BANK1_B_BANK3) ? "" : mLong_A_BANK1_B_BANK3);
                break;
            case SHORT_A_BANK2_B_BANK4:
                String mShort_A_BANK2_B_BANK4 = command.getFormattedResult();
                data.add(new OBDTripEntity("第二侧氧气侦测器短期修正,A:bank 2,B:bank 4", TextUtils.isEmpty(mShort_A_BANK2_B_BANK4) ? "N/A" : mShort_A_BANK2_B_BANK4));
                entity.setShort_A_BANK2_B_BANK4(TextUtils.isEmpty(mShort_A_BANK2_B_BANK4) ? "N/A" : mShort_A_BANK2_B_BANK4);
                break;
            case LONG_A_BANK2_B_BANK4:
                String mLong_A_BANK2_B_BANK4 = command.getFormattedResult();
                data.add(new OBDTripEntity("第二侧氧气侦测器长期修正,A:bank 2,B:bank 4", TextUtils.isEmpty(mLong_A_BANK2_B_BANK4) ? "N/A" : mLong_A_BANK2_B_BANK4));
                entity.setLong_A_BANK2_B_BANK4(TextUtils.isEmpty(mLong_A_BANK2_B_BANK4) ? "N/A" : mLong_A_BANK2_B_BANK4);
                break;
            case FUEL_RAIL_ABS_PRESSURE:
                String mFuelRailAbsPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("高压共轨绝对压力", TextUtils.isEmpty(mFuelRailAbsPressure) ? "N/A" : mFuelRailAbsPressure));
                entity.setFuelRailAbsPressure(TextUtils.isEmpty(mFuelRailAbsPressure) ? "N/A" : mFuelRailAbsPressure);
                break;
            case REL_ACCELERATOR_PEDAL_POS:
                String mRelAccPedalPos = command.getFormattedResult();
                data.add(new OBDTripEntity("加速踏板相对位置", TextUtils.isEmpty(mRelAccPedalPos) ? "N/A" : mRelAccPedalPos));
                entity.setRelAccPedalPos(TextUtils.isEmpty(mRelAccPedalPos) ? "N/A" : mRelAccPedalPos);
                break;
            case HY_BATTERY_PACK_LIFE:
                String mHyBatteryPackLife = command.getFormattedResult();
                data.add(new OBDTripEntity("油电混合电池组剩下寿命", TextUtils.isEmpty(mHyBatteryPackLife) ? "N/A" : mHyBatteryPackLife));
                entity.setHyBatteryPackLife(TextUtils.isEmpty(mHyBatteryPackLife) ? "N/A" : mHyBatteryPackLife);
                break;
            case DRIVER_ENGINE_TORQUE:
                String mDriverEngineTorque = command.getFormattedResult();
                data.add(new OBDTripEntity("驾驶的引擎命令-扭矩百分比", TextUtils.isEmpty(mDriverEngineTorque) ? "N/A" : mDriverEngineTorque));
                entity.setHyBatteryPackLife(TextUtils.isEmpty(mDriverEngineTorque) ? "N/A" : mDriverEngineTorque);
                break;
            case ACTUAL_ENGINE_TORQUE:
                String mActualEngineTorque = command.getFormattedResult();
                data.add(new OBDTripEntity("实际引擎-扭矩百分比", TextUtils.isEmpty(mActualEngineTorque) ? "N/A" : mActualEngineTorque));
                entity.setActualEngineTorque(TextUtils.isEmpty(mActualEngineTorque) ? "N/A" : mActualEngineTorque);
                break;
            case ENGINE_REFERENCE_TORQUE:
                String mEngineReferenceTorque = command.getFormattedResult();
                data.add(new OBDTripEntity("引擎参考扭矩", TextUtils.isEmpty(mEngineReferenceTorque) ? "N/A" : mEngineReferenceTorque));
                entity.setEngineReferenceTorque(TextUtils.isEmpty(mEngineReferenceTorque) ? "N/A" : mEngineReferenceTorque);
                break;
            case DPF_TEMP:
                String mDPFTemp = command.getFormattedResult();
                data.add(new OBDTripEntity("柴油粒子过滤器(DPF)温度", TextUtils.isEmpty(mDPFTemp) ? "N/A" : mDPFTemp));
                entity.setDPFTemp(TextUtils.isEmpty(mDPFTemp) ? "N/A" : mDPFTemp);
                break;
            case ENGINE_FRICTION_PERCENT_TORQUE:
                String mEngineFrictionPercentTorque = command.getFormattedResult();
                data.add(new OBDTripEntity("引擎摩擦力-扭矩百分比", TextUtils.isEmpty(mEngineFrictionPercentTorque) ? "N/A" : mEngineFrictionPercentTorque));
                entity.setEngineFrictionPercentTorque(TextUtils.isEmpty(mEngineFrictionPercentTorque) ? "N/A" : mEngineFrictionPercentTorque);
                break;
            case DISTANCE_TRAVELED_AFTER_CODES_CLEARED:
                String mDistanceTraveledAfterCodesCleared = command.getFormattedResult();
                data.add(new OBDTripEntity("故障码清除后行驶里程", TextUtils.isEmpty(mDistanceTraveledAfterCodesCleared) ? "N/A" : mDistanceTraveledAfterCodesCleared));
                entity.setDistanceTraveledAfterCodesCleared(TextUtils.isEmpty(mDistanceTraveledAfterCodesCleared) ? "N/A" : mDistanceTraveledAfterCodesCleared);
                break;
            case CONTROL_MODULE_VOLTAGE:
                String mControlModuleVoltage = command.getFormattedResult();
                data.add(new OBDTripEntity("控制模组电压", TextUtils.isEmpty(mControlModuleVoltage) ? "N/A" : mControlModuleVoltage));
                entity.setControlModuleVoltage(TextUtils.isEmpty(mControlModuleVoltage) ? "N/A" : mControlModuleVoltage);
                break;
            case FUEL_RAIL_PRESSURE:
                mFuelRailPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("油轨压力(柴油或汽油直喷)", TextUtils.isEmpty(mFuelRailPressure) ? "N/A" : mFuelRailPressure));
                entity.setFuelRailPressure(TextUtils.isEmpty(mFuelRailPressure) ? "N/A" : mFuelRailPressure);
                break;
            case FUEL_RAIL_PRESSURE_manifold:
                String mFuelRailPressureVacuum = command.getFormattedResult();
                data.add(new OBDTripEntity("油轨压力(相对进气歧管真空度)", TextUtils.isEmpty(mFuelRailPressureVacuum) ? "N/A" : mFuelRailPressureVacuum));
                entity.setFuelRailPressurevacuum(TextUtils.isEmpty(mFuelRailPressureVacuum) ? "N/A" : mFuelRailPressureVacuum);
                break;
            case VIN:
                String mVehicleIdentificationNumber = command.getFormattedResult();
                data.add(new OBDTripEntity("车辆识别号(VIN)", TextUtils.isEmpty(mVehicleIdentificationNumber) ? "N/A" : mVehicleIdentificationNumber));
                entity.setVehicleIdentificationNumber(TextUtils.isEmpty(mVehicleIdentificationNumber) ? "N/A" : mVehicleIdentificationNumber);
                break;
            case DISTANCE_TRAVELED_MIL_ON:
                String mDistanceTraveledMilOn = command.getFormattedResult();
                data.add(new OBDTripEntity("故障指示灯(MIL)亮时行驶的距离", TextUtils.isEmpty(mDistanceTraveledMilOn) ? "N/A" : mDistanceTraveledMilOn));
                entity.setDistanceTraveledMilOn(TextUtils.isEmpty(mDistanceTraveledMilOn) ? "N/A" : mDistanceTraveledMilOn);
                break;
            case DTC_NUMBER:
                String mDtcNumber = command.getFormattedResult();
                data.add(new OBDTripEntity("自从DTC清除后的监控状态", TextUtils.isEmpty(mDtcNumber) ? "N/A" : mDtcNumber));
                entity.setDtcNumber(TextUtils.isEmpty(mDtcNumber) ? "N/A" : mDtcNumber);
                break;
            case SYSTEM_VAPOR_PRESSURE:
                String mSystemVaporPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("系统蒸汽压力", TextUtils.isEmpty(mSystemVaporPressure) ? "N/A" : mSystemVaporPressure));
                entity.setSystemVaporPressure(TextUtils.isEmpty(mSystemVaporPressure) ? "N/A" : mSystemVaporPressure);
                break;
            case TIME_SINCE_TC_CLEARED:
                String mTimeSinceTcClear = command.getFormattedResult();
                data.add(new OBDTripEntity("故障代码清除后的时间", TextUtils.isEmpty(mTimeSinceTcClear) ? "N/A" : mTimeSinceTcClear));
                entity.setTimeSinceTcClear(TextUtils.isEmpty(mTimeSinceTcClear) ? "N/A" : mTimeSinceTcClear);
                break;
            case REL_THROTTLE_POS:
                String mRelThottlePos = command.getFormattedResult();
                data.add(new OBDTripEntity("相对油门位置", TextUtils.isEmpty(mRelThottlePos) ? "N/A" : mRelThottlePos));
                entity.setRelThottlePos(TextUtils.isEmpty(mRelThottlePos) ? "N/A" : mRelThottlePos);
                break;
            case ABS_LOAD:
                String mAbsLoad = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对负荷", TextUtils.isEmpty(mAbsLoad) ? "N/A" : mAbsLoad));
                entity.setAbsLoad(TextUtils.isEmpty(mAbsLoad) ? "N/A" : mAbsLoad);
                break;
            case AIR_FUEL_RATIO:
                String mAirFuelRatio = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油-空气命令等效比", TextUtils.isEmpty(mAirFuelRatio) ? "N/A" : mAirFuelRatio));
                entity.setAirFuelRatio(TextUtils.isEmpty(mAirFuelRatio) ? "N/A" : mAirFuelRatio);
                break;
            case DESCRIBE_PROTOCOL:
                String mDescribeProtocol = command.getFormattedResult();
                data.add(new OBDTripEntity("协议", TextUtils.isEmpty(mDescribeProtocol) ? "N/A" : mDescribeProtocol));
                entity.setDescribeProtocol(TextUtils.isEmpty(mDescribeProtocol) ? "N/A" : mDescribeProtocol);
                break;
            case DESCRIBE_PROTOCOL_NUMBER:
                String mDescribeProtocolNumber = command.getFormattedResult();
                data.add(new OBDTripEntity("协议编号", TextUtils.isEmpty(mDescribeProtocolNumber) ? "N/A" : mDescribeProtocolNumber));
                entity.setDescribeProtocolNumber(TextUtils.isEmpty(mDescribeProtocolNumber) ? "N/A" : mDescribeProtocolNumber);
                break;
            case IGNITION_MONITOR:
                String mIgnitionMonitor = command.getFormattedResult();
                data.add(new OBDTripEntity("点火监视器", TextUtils.isEmpty(mIgnitionMonitor) ? "N/A" : mIgnitionMonitor));
                entity.setIgnitionMonitor(TextUtils.isEmpty(mIgnitionMonitor) ? "N/A" : mIgnitionMonitor);
                break;
            case SHORT_TERM_BANK_1:
                String mShortTermBank1 = command.getFormattedResult();
                data.add(new OBDTripEntity("短期燃油调节库1", TextUtils.isEmpty(mShortTermBank1) ? "N/A" : mShortTermBank1));
                entity.setShortTermBank1(TextUtils.isEmpty(mShortTermBank1) ? "N/A" : mShortTermBank1);
                break;
            case SHORT_TERM_BANK_2:
                String mShortTermBank2 = command.getFormattedResult();
                data.add(new OBDTripEntity("短期燃油调节库2", TextUtils.isEmpty(mShortTermBank2) ? "N/A" : mShortTermBank2));
                entity.setShortTermBank2(TextUtils.isEmpty(mShortTermBank2) ? "N/A" : mShortTermBank2);
                break;
            case LONG_TERM_BANK_1:
                String mLongTermBank1 = command.getFormattedResult();
                data.add(new OBDTripEntity("长期燃油调节库1", TextUtils.isEmpty(mLongTermBank1) ? "N/A" : mLongTermBank1));
                entity.setLongTermBank1(TextUtils.isEmpty(mLongTermBank1) ? "N/A" : mLongTermBank1);
                break;
            case LONG_TERM_BANK_2:
                String mLongTermBank2 = command.getFormattedResult();
                data.add(new OBDTripEntity("长期燃油调节库2", TextUtils.isEmpty(mLongTermBank2) ? "N/A" : mLongTermBank2));
                entity.setLongTermBank2(TextUtils.isEmpty(mLongTermBank2) ? "N/A" : mLongTermBank2);
                break;
            case TROUBLE_CODES:
                String mFaultCodes = command.getFormattedResult();
                String[] troubleCodes = mFaultCodes.replaceAll("[\r\n]", ",").split(",");
                DefaultCode.delete(0, DefaultCode.length());
                for (String troubleCode : troubleCodes) {
                    getDefaultCode(mToken, troubleCode, DefaultCode);
                }
                break;
            case PERMANENT_TROUBLE_CODES:
                String mPermanentTroubleCode = command.getFormattedResult();
                String[] PermanentTroubleCodes = mPermanentTroubleCode.replaceAll("[\r\n]", ",").split(",");
                permanentCode.delete(0, permanentCode.length());
                for (String permanentTroubleCode : PermanentTroubleCodes) {
                    getPermanentCode(mToken, permanentTroubleCode, permanentCode);
                }
                break;
            case PENDING_TROUBLE_CODES:
                String mPendingTroubleCode = command.getFormattedResult();
                String[] PendingTroubleCodes = mPendingTroubleCode.replaceAll("[\r\n]", ",").split(",");
                pendingCode.delete(0, pendingCode.length());
                for (String pendingTroubleCode : PendingTroubleCodes) {
                    getDefaultPendingCode(mToken, pendingTroubleCode, pendingCode);
                }
                break;
        }
        entityList.addAll(data);
        setTripMap(entityList);
        setOBDJson(entity);
    }


    /**
     * @param token     用户token
     * @param faultCode 故障码
     * @param codes     故障码存储器
     *                  检测故障码
     */
    private void getDefaultCode(String token, String faultCode, StringBuilder codes) {
        OkHttpUtils.get().url(SERVER_URL + getInquireAboutFaultCodeDetails_URL).
                addParam("token", token).
                addParam("faultCode", faultCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                DefaultCodeEntity codeEntity = JSON.parseObject(response, DefaultCodeEntity.class);
                if (codeEntity.getSuccess()) {
                    if (codeEntity.getData().isDisplay()) {
                        codes.append(codeEntity.getData().getFaultCode());
                        codes.append('\n');
                    }
                    data.add(new OBDTripEntity("故障代码", TextUtils.isEmpty(codes.toString()) ? "" : codes.toString()));
                    entity.setFaultCodes(TextUtils.isEmpty(codes.toString()) ? "" : codes.toString());
                }
            }
        });
    }

    /**
     * @param token     用户token
     * @param faultCode 故障码
     * @param codes     故障码存储器
     *                  检测永久性故障代码
     */
    private void getPermanentCode(String token, String faultCode, StringBuilder codes) {
        OkHttpUtils.get().url(SERVER_URL + getInquireAboutFaultCodeDetails_URL).
                addParam("token", token).
                addParam("faultCode", faultCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                DefaultCodeEntity codeEntity = JSON.parseObject(response, DefaultCodeEntity.class);
                if (codeEntity.getSuccess()) {
                    if (codeEntity.getData().isDisplay()) {
                        codes.append(codeEntity.getData().getFaultCode());
                        codes.append('\n');
                    }
                    data.add(new OBDTripEntity("永久性故障代码", TextUtils.isEmpty(codes.toString()) ? "" : codes.toString()));
                    entity.setPermanentTroubleCode(TextUtils.isEmpty(codes.toString()) ? "" : codes.toString());
                }
            }
        });
    }

    /**
     * @param token     用户token
     * @param faultCode 故障码
     * @param codes     故障码存储器
     *                  检测未决故障代码
     */
    private void getDefaultPendingCode(String token, String faultCode, StringBuilder codes) {
        OkHttpUtils.get().url(SERVER_URL + getInquireAboutFaultCodeDetails_URL).
                addParam("token", token).
                addParam("faultCode", faultCode).
                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                DefaultCodeEntity codeEntity = JSON.parseObject(response, DefaultCodeEntity.class);
                if (codeEntity.getSuccess()) {
                    if (codeEntity.getData().isDisplay()) {
                        codes.append(codeEntity.getData().getFaultCode());
                        codes.append('\n');
                    }
                    data.add(new OBDTripEntity("未决故障代码", TextUtils.isEmpty(codes.toString()) ? "" : codes.toString()));
                    entity.setPendingTroubleCode(TextUtils.isEmpty(codes.toString()) ? "" : codes.toString());
                }
            }
        });
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public void setSpeed(int currentSpeed) {
        calculateIdlingAndDrivingTime(currentSpeed);
        findRapidAcctAndDecTimes(currentSpeed);
        speed = currentSpeed;
        if (speedMax < currentSpeed)
            speedMax = currentSpeed;
        if (speed != 0) {
            mAddSpeed += speed;
            mSpeedCount++;
            mDistanceTravel = (mAddSpeed / mSpeedCount * (drivingDuration / (60 * 60 * 1000)));
        }
        if (drivingDuration <= 0) {
            AverageSpeed = 0f;
        } else {
            AverageSpeed = mDistanceTravel / (drivingDuration / (60 * 60 * 1000));
        }
        data.add(new OBDTripEntity("时速", speed + " km/h"));
        entity.setSpeed(speed + " km/h");
        data.add(new OBDTripEntity("最高时速", speedMax + " km/h"));
        entity.setSpeedMax(speedMax + " km/h");
    }

    private void findRapidAcctAndDecTimes(int currentSpeed) {
        if (speed == -1)
            return;
        if (System.currentTimeMillis() - mLastTimeStamp > 1000) {

            int speedDiff = currentSpeed - mSecondAgoSpeed;
            boolean acceleration = speedDiff > 0;
            if (Math.abs(speedDiff) > SPEED_GAP) {
                if (acceleration)
                    mRapidAccTimes++;
                else
                    mRapidDecTimes++;
            }
            mSecondAgoSpeed = currentSpeed;
            mLastTimeStamp = System.currentTimeMillis();
        }
        data.add(new OBDTripEntity("降速次数", mRapidDecTimes + ""));
        entity.setRapidDeclTimes(mRapidDecTimes + "");
        data.add(new OBDTripEntity("加速次数", mRapidAccTimes + ""));
        entity.setRapidAccTimes(mRapidAccTimes + "");
    }

    private void calculateIdlingAndDrivingTime(int currentSpeed) {
        long currentTime = System.currentTimeMillis();
        if ((speed == -1 || speed == 0) && currentSpeed == 0) {
            idlingDuration = currentTime - tripStartTime - drivingDuration;
            data.add(new OBDTripEntity("空闲时间", (idlingDuration / 60000) + " 分"));
            entity.setIdlingDuration((idlingDuration / 60000) + " 分");
        }
        drivingDuration = currentTime - tripStartTime - idlingDuration;
        data.add(new OBDTripEntity("行驶时间", (drivingDuration / 60000) + " 分"));
        entity.setDrivingDuration((drivingDuration / 60000) + " 分");
    }

    private void calculateMaf() {
        if (mIntakePressure > 0 && mIntakeAirTemp > 0) {
            float rpm = Float.parseFloat(engineRpm);
            float imap = ((rpm * mIntakePressure) / mIntakeAirTemp) / 2;
            float engineDisp = 2;
            float maf = (imap / 60.0f) * (85.0f / 100.0f) * (engineDisp) * ((28.97f) / (8.314f));
            findInsFuelConsumption(maf);
        }
    }


    private void getFuelTypeValue(String fuelType) {
        float fuelTypeValue = 0;
        if (FuelType.GASOLINE.getDescription().equals(fuelType)) {
            fuelTypeValue = 14.7f;
            gramToLitre = GRAM_TO_LITRE_GASOLINE;
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
        data.add(new OBDTripEntity("燃料类型", mFuelTypeValue + ""));
        entity.setFuelTypeValue(String.valueOf(mFuelTypeValue));
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


    public Integer getSpeedMax() {
        return speedMax;
    }

    public float getAverageSpeed() {
        return AverageSpeed;
    }

    public void findInsFuelConsumption(float massAirFlow) {
        data.add(new OBDTripEntity("MAF空气流量速率", massAirFlow + ""));
        entity.setMassAirFlow(massAirFlow + "");
        if (speed > 0)
            mInsFuelConsumption = 100 * (massAirFlow / (mFuelTypeValue * gramToLitre) * 3600) / speed; // in  litre/100km
        findIdleAndDrivingFuelCons(massAirFlow);
    }

    public void setEngineRpm(String currentRPM) {
        engineRpm = currentRPM;
        if (currentRPM != null && this.engineRpmMax < Integer.parseInt(currentRPM)) {
            this.engineRpmMax = Integer.parseInt(currentRPM);
        }
        data.add(new OBDTripEntity("引擎转速", TextUtils.isEmpty(engineRpm) ? "N/A" : engineRpm));
        entity.setEngineRpm(TextUtils.isEmpty(engineRpm) ? "" : engineRpm);
        data.add(new OBDTripEntity("最大转速", engineRpmMax + " "));
        entity.setEngineRpmMax(String.valueOf(engineRpmMax));
    }

    public void findIdleAndDrivingFuelCons(float currentMaf) {
        float literPerSecond;
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
        data.add(new OBDTripEntity("怠速空气流量", mIdleMaf + " g/s"));
        entity.setIdleMaf(mIdleMaf + " g/s");
        data.add(new OBDTripEntity("驱动空气流量", mDrivingMaf + " g/s"));
        entity.setDrivingMaf(mDrivingMaf + " g/s");
        data.add(new OBDTripEntity("瞬时油耗", mInsFuelConsumption + " L/100km"));
        entity.setInsFuelConsumption(mInsFuelConsumption + " L");
        data.add(new OBDTripEntity("行驶油耗", mDrivingFuelConsumption + " L"));
        data.add(new OBDTripEntity("怠速油耗", mIdlingFuelConsumption + " L"));
        entity.setDrivingFuelConsumption(mDrivingFuelConsumption + " L");
        entity.setIdlingFuelConsumption(mIdlingFuelConsumption + " L");
    }



    public String getEngineRpm() {
        return engineRpm;
    }


    public String getEngineCoolantTemp() {
        return mEngineCoolantTemp;
    }

    public String getEngineOilTemp() {
        return mEngineOilTemp;
    }

    public String getFuelConsumptionRate() {
        return mFuelConsumptionRate;
    }

    public String getFuelPressure() {
        return mFuelPressure;
    }

    public String getThrottlePos() {
        return mThrottlePos;
    }

    public String getFuelRailPressure() {
        return mFuelRailPressure;
    }



    public List<OBDTripEntity> getTripMap() {
        return removeDuplicate(data);
    }

    private List<OBDTripEntity> removeDuplicate(List<OBDTripEntity> list) {
        List<OBDTripEntity> returnList = new ArrayList<>();
        Map<String, OBDTripEntity> map = new HashMap<>();
        for (OBDTripEntity people : list) {
            String key = people.getName();
            if (map.containsKey(key)) {
                map.remove(key);
            }
            map.put(key, people);
        }
        for (Map.Entry<String, OBDTripEntity> entry : map.entrySet()) {
            returnList.add(entry.getValue());
        }
        return returnList;
    }

    public void setTripMap(List<OBDTripEntity> entityList) {
        this.entityList = entityList;
    }

    public OBDJsonTripEntity getOBDJson() {
        return entity;
    }

    public void setOBDJson(OBDJsonTripEntity entity) {
        this.entity = entity;
    }
}
