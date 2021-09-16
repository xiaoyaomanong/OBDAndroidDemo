package com.example.obdandroid.config;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;

import java.io.Serializable;

import static com.example.obdandroid.config.OBDConfig.ABS_LOAD;
import static com.example.obdandroid.config.OBDConfig.ABS_THROTTLE_POS_B;
import static com.example.obdandroid.config.OBDConfig.ABS_THROTTLE_POS_C;
import static com.example.obdandroid.config.OBDConfig.ACC_PEDAL_POS_D;
import static com.example.obdandroid.config.OBDConfig.ACC_PEDAL_POS_E;
import static com.example.obdandroid.config.OBDConfig.ACC_PEDAL_POS_F;
import static com.example.obdandroid.config.OBDConfig.AIR_INTAKE_TEMPERATURE;
import static com.example.obdandroid.config.OBDConfig.AMBIENT_AIR_TEMP;
import static com.example.obdandroid.config.OBDConfig.BAROMETRIC_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.CONTROL_MODULE_VOLTAGE;
import static com.example.obdandroid.config.OBDConfig.Catalyst_Temperature_Bank_1_Sensor_1;
import static com.example.obdandroid.config.OBDConfig.Catalyst_Temperature_Bank_1_Sensor_2;
import static com.example.obdandroid.config.OBDConfig.Catalyst_Temperature_Bank_2_Sensor_1;
import static com.example.obdandroid.config.OBDConfig.Catalyst_Temperature_Bank_2_Sensor_2;
import static com.example.obdandroid.config.OBDConfig.DISTANCE_TRAVELED_MIL_ON;
import static com.example.obdandroid.config.OBDConfig.DTC_NUMBER;
import static com.example.obdandroid.config.OBDConfig.ENGINE_COOLANT_TEMP;
import static com.example.obdandroid.config.OBDConfig.ENGINE_OIL_TEMP;
import static com.example.obdandroid.config.OBDConfig.ENGINE_RPM;
import static com.example.obdandroid.config.OBDConfig.FUEL_LEVEL;
import static com.example.obdandroid.config.OBDConfig.FUEL_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.FUEL_RAIL_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.FUEL_RAIL_PRESSURE_manifold;
import static com.example.obdandroid.config.OBDConfig.INTAKE_MANIFOLD_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.LONG_A_BANK1_B_BANK3;
import static com.example.obdandroid.config.OBDConfig.LONG_A_BANK2_B_BANK4;
import static com.example.obdandroid.config.OBDConfig.LONG_TERM_BANK_1;
import static com.example.obdandroid.config.OBDConfig.LONG_TERM_BANK_2;
import static com.example.obdandroid.config.OBDConfig.MAF;
import static com.example.obdandroid.config.OBDConfig.REL_THROTTLE_POS;
import static com.example.obdandroid.config.OBDConfig.SHORT_A_BANK1_B_BANK3;
import static com.example.obdandroid.config.OBDConfig.SHORT_A_BANK2_B_BANK4;
import static com.example.obdandroid.config.OBDConfig.SHORT_TERM_BANK_1;
import static com.example.obdandroid.config.OBDConfig.SHORT_TERM_BANK_2;
import static com.example.obdandroid.config.OBDConfig.SYSTEM_VAPOR_PRESSURE;
import static com.example.obdandroid.config.OBDConfig.THROTTLE_ACTUATOR;
import static com.example.obdandroid.config.OBDConfig.THROTTLE_POS;
import static com.example.obdandroid.config.OBDConfig.TIME_TRAVELED_MIL_ON;
import static com.example.obdandroid.config.OBDConfig.VEHICLE_SPEED;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_1;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_2;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_3;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_4;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_5;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_6;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_7;
import static com.example.obdandroid.config.OBDConfig.WIDE_BAND_AIR_FUEL_RATIO_8;

/**
 * 作者：Jealous
 * 日期：2021/9/14 0014
 * 描述：OBD 模式二数据解析
 */
public class CheckRecordTwo implements Serializable {
    private static CheckRecordTwo sInstance;
    private int engineRpmMax = 0;
    private String engineRpm = "";
    private int speed = -1;
    private int speedMax = 0;

    private String mShortA_BANK1_B_BANK3;
    private String mLong_A_BANK1_B_BANK3;
    private String mShort_A_BANK2_B_BANK4;
    private String mLong_A_BANK2_B_BANK4;
    private float mMassAirFlow;
    private String mFuelLevel;
    private float mIntakeAirTemp = 0.0f;
    private float mIntakePressure = 0.0f;
    private String mAmbientAirTemp;

    private String mWideBandAirFuelRatioOne;
    private String mWideBandAirFuelRatioTwo;
    private String mWideBandAirFuelRatioThree;
    private String mWideBandAirFuelRatioFour;
    private String mWideBandAirFuelRatioFive;
    private String mWideBandAirFuelRatioSix;
    private String mWideBandAirFuelRatioSeven;
    private String mWideBandAirFuelRatioEight;

    private String mCatalystTemperatureBank1Sensor1;
    private String mCatalystTemperatureBank2Sensor1;
    private String mCatalystTemperatureBank1Sensor2;
    private String mCatalystTemperatureBank2Sensor2;

    private String mAbsThrottlePosB;
    private String mAbsThrottlePosC;
    private String mAccPedalPosD;
    private String mAccPedalPosE;
    private String mAccPedalPosF;
    private String mThrottleActuator;
    private String mTimeRunWithMILOn;
    private String mEngineCoolantTemp;
    private String mEngineOilTemp;
    private String mSystemVaporPressure;
    private String mFuelPressure;
    private String mBarometricPressure;
    private String mThrottlePos;
    private String mControlModuleVoltage;
    private String mFuelRailPressure = "";
    private String mFuelRailPressureVacuum = "";
    private String mDistanceTraveledMilOn;
    private String mDtcNumber;
    private String mRelThoPosition;
    private String mAbsLoad;
    private String mShortTermBank1;
    private String mShortTermBank2;
    private String mLongTermBank1;
    private String mLongTermBank2;

    private CheckRecordTwo() {
    }

    public static CheckRecordTwo getRecode() {
        if (sInstance == null)
            sInstance = new CheckRecordTwo();
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
            case MAF://"MAF空气流量速率"
                mMassAirFlow = Float.parseFloat(command.getFormattedResult());
                setMassAirFlow(mMassAirFlow);
                break;
            case FUEL_LEVEL://燃油油位
                mFuelLevel = command.getFormattedResult();
                setFuelLevel(TextUtils.isEmpty(mFuelLevel) ? "N/A" : mFuelLevel);
                break;
            case INTAKE_MANIFOLD_PRESSURE://"进气歧管压力"
                mIntakePressure = Float.parseFloat(command.getCalculatedResult());
                setIntakePressure(mIntakePressure);
                break;
            case AIR_INTAKE_TEMPERATURE://"油箱空气温度"
                mIntakeAirTemp = Float.parseFloat(command.getCalculatedResult()) + 273.15f;
                setIntakeAirTemp(mIntakeAirTemp);
                break;
            case AMBIENT_AIR_TEMP://"环境空气温度"
                mAmbientAirTemp = command.getFormattedResult();
                setAmbientAirTemp(TextUtils.isEmpty(mAmbientAirTemp) ? "N/A" : mAmbientAirTemp + " ℃");
                break;
            case ENGINE_COOLANT_TEMP://"引擎冷媒温度"
                mEngineCoolantTemp = command.getFormattedResult();
                setEngineCoolantTemp(TextUtils.isEmpty(mEngineCoolantTemp) ? "N/A" : mEngineCoolantTemp + " ℃");
                break;
            case ENGINE_OIL_TEMP://"引擎油温"
                mEngineOilTemp = command.getFormattedResult();
                setEngineOilTemp(TextUtils.isEmpty(mEngineOilTemp) ? "N/A" : mEngineOilTemp);
                break;
            case FUEL_PRESSURE://"油压"
                mFuelPressure = command.getFormattedResult();
                setFuelPressure(TextUtils.isEmpty(mFuelPressure) ? "N/A" : mFuelPressure);
                break;
            case BAROMETRIC_PRESSURE://"绝对大气压"
                mBarometricPressure = command.getFormattedResult();
                setBarometricPressure(TextUtils.isEmpty(mBarometricPressure) ? "N/A" : mBarometricPressure);
                break;
            case THROTTLE_POS://"油门位置"
                mThrottlePos = command.getFormattedResult();
                setThrottlePos(TextUtils.isEmpty(mThrottlePos) ? "N/A" : mThrottlePos);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_1://"氧气侦测器1 燃油-空气当量比 电流"
                mWideBandAirFuelRatioOne = command.getFormattedResult();
                setWideBandAirFuelRatioOne(TextUtils.isEmpty(mWideBandAirFuelRatioOne) ? "N/A" : mWideBandAirFuelRatioOne);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_2://"氧气侦测器2 燃油-空气当量比 电流"
                mWideBandAirFuelRatioTwo = command.getFormattedResult();
                setWideBandAirFuelRatioTwo(TextUtils.isEmpty(mWideBandAirFuelRatioTwo) ? "N/A" : mWideBandAirFuelRatioTwo);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_3://"氧气侦测器3 燃油-空气当量比 电流"
                mWideBandAirFuelRatioThree = command.getFormattedResult();
                setWideBandAirFuelRatioThree(TextUtils.isEmpty(mWideBandAirFuelRatioThree) ? "N/A" : mWideBandAirFuelRatioThree);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_4://"氧气侦测器4 燃油-空气当量比 电流"
                mWideBandAirFuelRatioFour = command.getFormattedResult();
                setWideBandAirFuelRatioFour(TextUtils.isEmpty(mWideBandAirFuelRatioFour) ? "N/A" : mWideBandAirFuelRatioFour);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_5://"氧气侦测器5 燃油-空气当量比 电流"
                mWideBandAirFuelRatioFive = command.getFormattedResult();
                setWideBandAirFuelRatioFive(TextUtils.isEmpty(mWideBandAirFuelRatioFive) ? "N/A" : mWideBandAirFuelRatioFive);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_6://"氧气侦测器6 燃油-空气当量比 电流"
                mWideBandAirFuelRatioSix = command.getFormattedResult();
                setWideBandAirFuelRatioSix(TextUtils.isEmpty(mWideBandAirFuelRatioSix) ? "N/A" : mWideBandAirFuelRatioSix);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_7://"氧气侦测器7 燃油-空气当量比 电流"
                mWideBandAirFuelRatioSeven = command.getFormattedResult();
                setWideBandAirFuelRatioSeven(TextUtils.isEmpty(mWideBandAirFuelRatioSeven) ? "N/A" : mWideBandAirFuelRatioSeven);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_8://"氧气侦测器8 燃油-空气当量比 电流"
                mWideBandAirFuelRatioEight = command.getFormattedResult();
                setWideBandAirFuelRatioEight(TextUtils.isEmpty(mWideBandAirFuelRatioEight) ? "N/A" : mWideBandAirFuelRatioEight);
                break;
            case Catalyst_Temperature_Bank_1_Sensor_1://"催化剂温度:Bank1,感测器1"
                mCatalystTemperatureBank1Sensor1 = command.getFormattedResult();
                setCatalystTemperatureBank1Sensor1(TextUtils.isEmpty(mCatalystTemperatureBank1Sensor1) ? "N/A" : mCatalystTemperatureBank1Sensor1);
                break;
            case Catalyst_Temperature_Bank_2_Sensor_1://"催化剂温度:Bank2,感测器1"
                mCatalystTemperatureBank2Sensor1 = command.getFormattedResult();
                setCatalystTemperatureBank2Sensor1(TextUtils.isEmpty(mCatalystTemperatureBank2Sensor1) ? "N/A" : mCatalystTemperatureBank2Sensor1);
                break;
            case Catalyst_Temperature_Bank_1_Sensor_2://"催化剂温度:Bank1,感测器2"
                mCatalystTemperatureBank1Sensor2 = command.getFormattedResult();
                setCatalystTemperatureBank1Sensor2(TextUtils.isEmpty(mCatalystTemperatureBank1Sensor2) ? "N/A" : mCatalystTemperatureBank1Sensor2);
                break;
            case Catalyst_Temperature_Bank_2_Sensor_2://"催化剂温度:Bank2,感测器2"
                mCatalystTemperatureBank2Sensor2 = command.getFormattedResult();
                setCatalystTemperatureBank2Sensor2(TextUtils.isEmpty(mCatalystTemperatureBank2Sensor2) ? "N/A" : mCatalystTemperatureBank2Sensor2);
                break;
            case ABS_THROTTLE_POS_B://"绝对油门位置B"
                mAbsThrottlePosB = command.getFormattedResult();
                setAbsThrottlePosB(TextUtils.isEmpty(mAbsThrottlePosB) ? "N/A" : mAbsThrottlePosB);
                break;
            case ABS_THROTTLE_POS_C://"绝对油门位置C"
                mAbsThrottlePosC = command.getFormattedResult();
                setAbsThrottlePosC(TextUtils.isEmpty(mAbsThrottlePosC) ? "N/A" : mAbsThrottlePosC);
                break;
            case ACC_PEDAL_POS_D://"加速踏板位置D"
                mAccPedalPosD = command.getFormattedResult();
                setAccPedalPosD(TextUtils.isEmpty(mAccPedalPosD) ? "N/A" : mAccPedalPosD);
                break;
            case ACC_PEDAL_POS_E://"加速踏板位置E"
                mAccPedalPosE = command.getFormattedResult();
                setAccPedalPosE(TextUtils.isEmpty(mAccPedalPosE) ? "N/A" : mAccPedalPosE);
                break;
            case ACC_PEDAL_POS_F://"加速踏板位置F"
                mAccPedalPosF = command.getFormattedResult();
                setAccPedalPosF(TextUtils.isEmpty(mAccPedalPosF) ? "N/A" : mAccPedalPosF);
                break;
            case THROTTLE_ACTUATOR://"油门执行器控制值"
                mThrottleActuator = command.getFormattedResult();
                setThrottleActuator(TextUtils.isEmpty(mThrottleActuator) ? "N/A" : mThrottleActuator);
                break;
            case TIME_TRAVELED_MIL_ON://"MIL灯亮的行驶时间"
                mTimeRunWithMILOn = command.getFormattedResult();
                setTimeRunWithMILOn(TextUtils.isEmpty(mTimeRunWithMILOn) ? "N/A" : mTimeRunWithMILOn);
                break;
            case SHORT_A_BANK1_B_BANK3://"第二侧氧气侦测器短期修正,A:bank 1,B:bank 3"
                mShortA_BANK1_B_BANK3 = command.getFormattedResult();
                setShortA_BANK1_B_BANK3(TextUtils.isEmpty(mShortA_BANK1_B_BANK3) ? "N/A" : mShortA_BANK1_B_BANK3);
                break;
            case LONG_A_BANK1_B_BANK3://"第二侧氧气侦测器长期修正,A:bank 1,B:bank 3"
                mLong_A_BANK1_B_BANK3 = command.getFormattedResult();
                setLong_A_BANK1_B_BANK3(TextUtils.isEmpty(mLong_A_BANK1_B_BANK3) ? "N/A" : mLong_A_BANK1_B_BANK3);
                break;
            case SHORT_A_BANK2_B_BANK4://"第二侧氧气侦测器短期修正,A:bank 2,B:bank 4"
                mShort_A_BANK2_B_BANK4 = command.getFormattedResult();
                setShort_A_BANK2_B_BANK4(TextUtils.isEmpty(mShort_A_BANK2_B_BANK4) ? "N/A" : mShort_A_BANK2_B_BANK4);
                break;
            case LONG_A_BANK2_B_BANK4://"第二侧氧气侦测器长期修正,A:bank 2,B:bank 4"
                mLong_A_BANK2_B_BANK4 = command.getFormattedResult();
                setLong_A_BANK2_B_BANK4(TextUtils.isEmpty(mLong_A_BANK2_B_BANK4) ? "N/A" : mLong_A_BANK2_B_BANK4);
                break;
            case CONTROL_MODULE_VOLTAGE://"控制模组电压"
                mControlModuleVoltage = command.getFormattedResult();
                setControlModuleVoltage(TextUtils.isEmpty(mControlModuleVoltage) ? "N/A" : mControlModuleVoltage);
                break;
            case FUEL_RAIL_PRESSURE://"油轨压力(柴油或汽油直喷)"
                mFuelRailPressure = command.getFormattedResult();
                setFuelRailPressure(TextUtils.isEmpty(mFuelRailPressure) ? "N/A" : mFuelRailPressure);
                break;
            case FUEL_RAIL_PRESSURE_manifold://"油轨压力(相对进气歧管真空度)"
                mFuelRailPressureVacuum = command.getFormattedResult();
                setFuelRailPressureVacuum(TextUtils.isEmpty(mFuelRailPressureVacuum) ? "N/A" : mFuelRailPressureVacuum);
                break;
            case DISTANCE_TRAVELED_MIL_ON://"故障指示灯(MIL)亮时行驶的距离"
                mDistanceTraveledMilOn = command.getFormattedResult();
                setDistanceTraveledMilOn(TextUtils.isEmpty(mDistanceTraveledMilOn) ? "N/A" : mDistanceTraveledMilOn);
                break;
            case DTC_NUMBER://"自从DTC清除后的监控状态"
                mDtcNumber = command.getFormattedResult();
                setDtcNumber(TextUtils.isEmpty(mDtcNumber) ? "N/A" : mDtcNumber);
                break;
            case SYSTEM_VAPOR_PRESSURE://"系统蒸汽压力"
                mSystemVaporPressure = command.getFormattedResult();
                setSystemVaporPressure(TextUtils.isEmpty(mSystemVaporPressure) ? "N/A" : mSystemVaporPressure);
                break;
            case REL_THROTTLE_POS://"相对油门位置"
                mRelThoPosition = command.getFormattedResult();
                setRelThoPos(TextUtils.isEmpty(mRelThoPosition) ? "N/A" : mRelThoPosition);
                break;
            case ABS_LOAD://"绝对负荷"
                mAbsLoad = command.getFormattedResult();
                setAbsLoad(TextUtils.isEmpty(mAbsLoad) ? "N/A" : mAbsLoad);
                break;
            case SHORT_TERM_BANK_1://"短期燃油调节库1"
                mShortTermBank1 = command.getFormattedResult();
                setShortTermBank1(TextUtils.isEmpty(mShortTermBank1) ? "N/A" : mShortTermBank1);
                break;
            case SHORT_TERM_BANK_2://"短期燃油调节库2"
                mShortTermBank2 = command.getFormattedResult();
                setShortTermBank2(TextUtils.isEmpty(mShortTermBank2) ? "N/A" : mShortTermBank2);
                break;
            case LONG_TERM_BANK_1://"长期燃油调节库1"
                mLongTermBank1 = command.getFormattedResult();
                setLongTermBank1(TextUtils.isEmpty(mLongTermBank1) ? "N/A" : mLongTermBank1);
                break;
            case LONG_TERM_BANK_2://"长期燃油调节库2"
                mLongTermBank2 = command.getFormattedResult();
                setLongTermBank2(TextUtils.isEmpty(mLongTermBank2) ? "N/A" : mLongTermBank2);
                break;
        }
    }


    public void setSpeed(int currentSpeed) {
        speed = currentSpeed;
        if (speedMax < currentSpeed)
            speedMax = currentSpeed;
    }

    public int getSpeed() {
        if (speed == -1)
            return 0;
        return speed;
    }

    public void setEngineRpm(String currentRPM) {
        engineRpm = currentRPM;
        if (currentRPM != null && this.engineRpmMax < Integer.parseInt(currentRPM)) {
            this.engineRpmMax = Integer.parseInt(currentRPM);
        }
    }

    public Integer getEngineRpmMax() {
        return engineRpmMax;
    }

    public String getEngineRpm() {
        return engineRpm;
    }

    public Integer getSpeedMax() {
        return speedMax;
    }

    public String getShortA_BANK1_B_BANK3() {
        return mShortA_BANK1_B_BANK3;
    }

    public void setShortA_BANK1_B_BANK3(String mShortA_BANK1_B_BANK3) {
        this.mShortA_BANK1_B_BANK3 = mShortA_BANK1_B_BANK3;
    }

    public String getLong_A_BANK1_B_BANK3() {
        return mLong_A_BANK1_B_BANK3;
    }

    public void setLong_A_BANK1_B_BANK3(String mLong_A_BANK1_B_BANK3) {
        this.mLong_A_BANK1_B_BANK3 = mLong_A_BANK1_B_BANK3;
    }

    public String getShort_A_BANK2_B_BANK4() {
        return mShort_A_BANK2_B_BANK4;
    }

    public void setShort_A_BANK2_B_BANK4(String mShort_A_BANK2_B_BANK4) {
        this.mShort_A_BANK2_B_BANK4 = mShort_A_BANK2_B_BANK4;
    }

    public String getLong_A_BANK2_B_BANK4() {
        return mLong_A_BANK2_B_BANK4;
    }

    public void setLong_A_BANK2_B_BANK4(String mLong_A_BANK2_B_BANK4) {
        this.mLong_A_BANK2_B_BANK4 = mLong_A_BANK2_B_BANK4;
    }

    public float getMassAirFlow() {
        return mMassAirFlow;
    }

    public void setMassAirFlow(float mMassAirFlow) {
        this.mMassAirFlow = mMassAirFlow;
    }

    public String getFuelLevel() {
        return mFuelLevel;
    }

    public void setFuelLevel(String mFuelLevel) {
        this.mFuelLevel = mFuelLevel;
    }

    public float getIntakeAirTemp() {
        return mIntakeAirTemp;
    }

    public void setIntakeAirTemp(float mIntakeAirTemp) {
        this.mIntakeAirTemp = mIntakeAirTemp;
    }

    public float getIntakePressure() {
        return mIntakePressure;
    }

    public void setIntakePressure(float mIntakePressure) {
        this.mIntakePressure = mIntakePressure;
    }

    public String getAmbientAirTemp() {
        return mAmbientAirTemp;
    }

    public void setAmbientAirTemp(String mAmbientAirTemp) {
        this.mAmbientAirTemp = mAmbientAirTemp;
    }

    public String getWideBandAirFuelRatioOne() {
        return mWideBandAirFuelRatioOne;
    }

    public void setWideBandAirFuelRatioOne(String mWideBandAirFuelRatioOne) {
        this.mWideBandAirFuelRatioOne = mWideBandAirFuelRatioOne;
    }

    public String getWideBandAirFuelRatioTwo() {
        return mWideBandAirFuelRatioTwo;
    }

    public void setWideBandAirFuelRatioTwo(String mWideBandAirFuelRatioTwo) {
        this.mWideBandAirFuelRatioTwo = mWideBandAirFuelRatioTwo;
    }

    public String getWideBandAirFuelRatioThree() {
        return mWideBandAirFuelRatioThree;
    }

    public void setWideBandAirFuelRatioThree(String mWideBandAirFuelRatioThree) {
        this.mWideBandAirFuelRatioThree = mWideBandAirFuelRatioThree;
    }

    public String getWideBandAirFuelRatioFour() {
        return mWideBandAirFuelRatioFour;
    }

    public void setWideBandAirFuelRatioFour(String mWideBandAirFuelRatioFour) {
        this.mWideBandAirFuelRatioFour = mWideBandAirFuelRatioFour;
    }

    public String getWideBandAirFuelRatioFive() {
        return mWideBandAirFuelRatioFive;
    }

    public void setWideBandAirFuelRatioFive(String mWideBandAirFuelRatioFive) {
        this.mWideBandAirFuelRatioFive = mWideBandAirFuelRatioFive;
    }

    public String getWideBandAirFuelRatioSix() {
        return mWideBandAirFuelRatioSix;
    }

    public void setWideBandAirFuelRatioSix(String mWideBandAirFuelRatioSix) {
        this.mWideBandAirFuelRatioSix = mWideBandAirFuelRatioSix;
    }

    public String getWideBandAirFuelRatioSeven() {
        return mWideBandAirFuelRatioSeven;
    }

    public void setWideBandAirFuelRatioSeven(String mWideBandAirFuelRatioSeven) {
        this.mWideBandAirFuelRatioSeven = mWideBandAirFuelRatioSeven;
    }

    public String getWideBandAirFuelRatioEight() {
        return mWideBandAirFuelRatioEight;
    }

    public void setWideBandAirFuelRatioEight(String mWideBandAirFuelRatioEight) {
        this.mWideBandAirFuelRatioEight = mWideBandAirFuelRatioEight;
    }

    public String getCatalystTemperatureBank1Sensor1() {
        return mCatalystTemperatureBank1Sensor1;
    }

    public void setCatalystTemperatureBank1Sensor1(String mCatalystTemperatureBank1Sensor1) {
        this.mCatalystTemperatureBank1Sensor1 = mCatalystTemperatureBank1Sensor1;
    }

    public String getCatalystTemperatureBank2Sensor1() {
        return mCatalystTemperatureBank2Sensor1;
    }

    public void setCatalystTemperatureBank2Sensor1(String mCatalystTemperatureBank2Sensor1) {
        this.mCatalystTemperatureBank2Sensor1 = mCatalystTemperatureBank2Sensor1;
    }

    public String getCatalystTemperatureBank1Sensor2() {
        return mCatalystTemperatureBank1Sensor2;
    }

    public void setCatalystTemperatureBank1Sensor2(String mCatalystTemperatureBank1Sensor2) {
        this.mCatalystTemperatureBank1Sensor2 = mCatalystTemperatureBank1Sensor2;
    }

    public String getCatalystTemperatureBank2Sensor2() {
        return mCatalystTemperatureBank2Sensor2;
    }

    public void setCatalystTemperatureBank2Sensor2(String mCatalystTemperatureBank2Sensor2) {
        this.mCatalystTemperatureBank2Sensor2 = mCatalystTemperatureBank2Sensor2;
    }

    public String getAbsThrottlePosB() {
        return mAbsThrottlePosB;
    }

    public void setAbsThrottlePosB(String mAbsThrottlePosB) {
        this.mAbsThrottlePosB = mAbsThrottlePosB;
    }

    public String getAbsThrottlePosC() {
        return mAbsThrottlePosC;
    }

    public void setAbsThrottlePosC(String mAbsThrottlePosC) {
        this.mAbsThrottlePosC = mAbsThrottlePosC;
    }

    public String getAccPedalPosD() {
        return mAccPedalPosD;
    }

    public void setAccPedalPosD(String mAccPedalPosD) {
        this.mAccPedalPosD = mAccPedalPosD;
    }

    public String getAccPedalPosE() {
        return mAccPedalPosE;
    }

    public void setAccPedalPosE(String mAccPedalPosE) {
        this.mAccPedalPosE = mAccPedalPosE;
    }

    public String getAccPedalPosF() {
        return mAccPedalPosF;
    }

    public void setAccPedalPosF(String mAccPedalPosF) {
        this.mAccPedalPosF = mAccPedalPosF;
    }

    public String getThrottleActuator() {
        return mThrottleActuator;
    }

    public void setThrottleActuator(String mThrottleActuator) {
        this.mThrottleActuator = mThrottleActuator;
    }

    public String getTimeRunWithMILOn() {
        return mTimeRunWithMILOn;
    }

    public void setTimeRunWithMILOn(String mTimeRunWithMILOn) {
        this.mTimeRunWithMILOn = mTimeRunWithMILOn;
    }

    public String getEngineCoolantTemp() {
        return mEngineCoolantTemp;
    }

    public void setEngineCoolantTemp(String mEngineCoolantTemp) {
        this.mEngineCoolantTemp = mEngineCoolantTemp;
    }

    public String getEngineOilTemp() {
        return mEngineOilTemp;
    }

    public void setEngineOilTemp(String mEngineOilTemp) {
        this.mEngineOilTemp = mEngineOilTemp;
    }

    public String getSystemVaporPressure() {
        return mSystemVaporPressure;
    }

    public void setSystemVaporPressure(String mSystemVaporPressure) {
        this.mSystemVaporPressure = mSystemVaporPressure;
    }

    public String getFuelPressure() {
        return mFuelPressure;
    }

    public void setFuelPressure(String mFuelPressure) {
        this.mFuelPressure = mFuelPressure;
    }

    public String getBarometricPressure() {
        return mBarometricPressure;
    }

    public void setBarometricPressure(String mBarometricPressure) {
        this.mBarometricPressure = mBarometricPressure;
    }

    public String getThrottlePos() {
        return mThrottlePos;
    }

    public void setThrottlePos(String mThrottlePos) {
        this.mThrottlePos = mThrottlePos;
    }

    public String getControlModuleVoltage() {
        return mControlModuleVoltage;
    }

    public void setControlModuleVoltage(String mControlModuleVoltage) {
        this.mControlModuleVoltage = mControlModuleVoltage;
    }

    public String getFuelRailPressure() {
        return mFuelRailPressure;
    }

    public void setFuelRailPressure(String mFuelRailPressure) {
        this.mFuelRailPressure = mFuelRailPressure;
    }

    public String getFuelRailPressureVacuum() {
        return mFuelRailPressureVacuum;
    }

    public void setFuelRailPressureVacuum(String mFuelRailPressureVacuum) {
        this.mFuelRailPressureVacuum = mFuelRailPressureVacuum;
    }

    public String getDistanceTraveledMilOn() {
        return mDistanceTraveledMilOn;
    }

    public void setDistanceTraveledMilOn(String mDistanceTraveledMilOn) {
        this.mDistanceTraveledMilOn = mDistanceTraveledMilOn;
    }

    public String getDtcNumber() {
        return mDtcNumber;
    }

    public void setDtcNumber(String mDtcNumber) {
        this.mDtcNumber = mDtcNumber;
    }

    public String getRelThoPos() {
        return mRelThoPosition;
    }

    public void setRelThoPos(String mRelThoPos) {
        this.mRelThoPosition = mRelThoPos;
    }

    public String getAbsLoad() {
        return mAbsLoad;
    }

    public void setAbsLoad(String mAbsLoad) {
        this.mAbsLoad = mAbsLoad;
    }

    public String getShortTermBank1() {
        return mShortTermBank1;
    }

    public void setShortTermBank1(String mShortTermBank1) {
        this.mShortTermBank1 = mShortTermBank1;
    }

    public String getShortTermBank2() {
        return mShortTermBank2;
    }

    public void setShortTermBank2(String mShortTermBank2) {
        this.mShortTermBank2 = mShortTermBank2;
    }

    public String getLongTermBank1() {
        return mLongTermBank1;
    }

    public void setLongTermBank1(String mLongTermBank1) {
        this.mLongTermBank1 = mLongTermBank1;
    }

    public String getLongTermBank2() {
        return mLongTermBank2;
    }

    public void setLongTermBank2(String mLongTermBank2) {
        this.mLongTermBank2 = mLongTermBank2;
    }
}
