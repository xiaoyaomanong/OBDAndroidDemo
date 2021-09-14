package com.example.obdandroid.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;

import java.io.Serializable;

/**
 * 作者：Jealous
 * 日期：2021/9/14 0014
 * 描述：
 */
public class CheckRecordTwo implements Serializable {
    @SuppressLint("StaticFieldLeak")
    private final static String ENGINE_RPM = "Engine RPM";
    private final static String VEHICLE_SPEED = "Vehicle Speed";
    private final static String MAF = "Mass Air Flow";
    private final static String FUEL_LEVEL = "Fuel Level";
    private final static String INTAKE_MANIFOLD_PRESSURE = "Intake Manifold Pressure";
    private final static String AIR_INTAKE_TEMPERATURE = "Air Intake Temperature";
    private final static String AMBIENT_AIR_TEMP = "Ambient Air Temperature";
    private final static String ENGINE_COOLANT_TEMP = "Engine Coolant Temperature";
    private final static String BAROMETRIC_PRESSURE = "Barometric Pressure";
    private final static String FUEL_PRESSURE = "Fuel Pressure";
    private final static String THROTTLE_POS = "Throttle Position";
    private final static String SYSTEM_VAPOR_PRESSURE = "System Vapor Pressure";
    private final static String CONTROL_MODULE_VOLTAGE = "Control Module Power Supply";
    private final static String FUEL_RAIL_PRESSURE = "Fuel Rail Pressure";
    private final static String FUEL_RAIL_PRESSURE_manifold = "Fuel Rail Pressure relative to manifold vacuum";
    private final static String DISTANCE_TRAVELED_MIL_ON = "Distance traveled with MIL on";
    private final static String DTC_NUMBER = "Diagnostic Trouble Codes";
    private final static String REL_THROTTLE_POS = "Relative throttle position";
    private final static String ABS_LOAD = "Absolute load";
    private final static String ENGINE_OIL_TEMP = "Engine oil temperature";
    private final static String SHORT_TERM_BANK_1 = "Short Term Fuel Trim Bank 1";
    private final static String SHORT_TERM_BANK_2 = "Short Term Fuel Trim Bank 2";
    private final static String LONG_TERM_BANK_1 = "Long Term Fuel Trim Bank 1";
    private final static String LONG_TERM_BANK_2 = "Long Term Fuel Trim Bank 2";

    private final static String WIDE_BAND_AIR_FUEL_RATIO_1 = "Wide band Air/Fuel Ratio Oxygen Sensor 1";
    private final static String WIDE_BAND_AIR_FUEL_RATIO_2 = "Wide band Air/Fuel Ratio Oxygen Sensor 2";
    private final static String WIDE_BAND_AIR_FUEL_RATIO_3 = "Wide band Air/Fuel Ratio Oxygen Sensor 3";
    private final static String WIDE_BAND_AIR_FUEL_RATIO_4 = "Wide band Air/Fuel Ratio Oxygen Sensor 4";
    private final static String WIDE_BAND_AIR_FUEL_RATIO_5 = "Wide band Air/Fuel Ratio Oxygen Sensor 5";
    private final static String WIDE_BAND_AIR_FUEL_RATIO_6 = "Wide band Air/Fuel Ratio Oxygen Sensor 6";
    private final static String WIDE_BAND_AIR_FUEL_RATIO_7 = "Wide band Air/Fuel Ratio Oxygen Sensor 7";
    private final static String WIDE_BAND_AIR_FUEL_RATIO_8 = "Wide band Air/Fuel Ratio Oxygen Sensor 8";

    private final static String Catalyst_Temperature_Bank_1_Sensor_1 = "Catalyst Temperature: Bank 1, Sensor 1";
    private final static String Catalyst_Temperature_Bank_2_Sensor_1 = "Catalyst Temperature: Bank 2, Sensor 1";
    private final static String Catalyst_Temperature_Bank_1_Sensor_2 = "Catalyst Temperature: Bank 1, Sensor 2";
    private final static String Catalyst_Temperature_Bank_2_Sensor_2 = "Catalyst Temperature: Bank 2, Sensor 2";


    private final static String ABS_THROTTLE_POS_B = "Absolute throttle position B";
    private final static String ABS_THROTTLE_POS_C = "Absolute throttle position C";
    private final static String ACC_PEDAL_POS_D = "Accelerator pedal position D";
    private final static String ACC_PEDAL_POS_E = "Accelerator pedal position E";
    private final static String ACC_PEDAL_POS_F = "Accelerator pedal position F";
    private final static String THROTTLE_ACTUATOR = "Commanded throttle actuator";
    private final static String TIME_TRAVELED_MIL_ON = "Time run with MIL on";
    private final static String SHORT_A_BANK1_B_BANK3 = "Short term secondary oxygen sensor trim, A: bank 1, B: bank 3";
    private final static String LONG_A_BANK1_B_BANK3 = "Long term secondary oxygen sensor trim, A: bank 1, B: bank 3";
    private final static String SHORT_A_BANK2_B_BANK4 = "Short term secondary oxygen sensor trim, A: bank 2, B: bank 4";
    private final static String LONG_A_BANK2_B_BANK4 = "Long term secondary oxygen sensor trim, A: bank 2, B: bank 4";

    @SuppressLint("StaticFieldLeak")
    private static CheckRecordTwo sInstance;
    private Integer engineRpmMax = 0;
    private String engineRpm = "";
    private Integer speed = -1;
    private Integer speedMax = 0;

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

    private String mAbsThrottlePosb;
    private String mAbsThrottlePosc;
    private String mAccPedalPosd;
    private String mAccPedalPose;
    private String mAccPedalPosf;
    private String mThrottleActuator;
    private String mTimeRunwithMILOn;
    private String mEngineCoolantTemp;
    private String mEngineOilTemp;
    private String mSystemVaporPressure;
    private String mFuelPressure;
    private String mBarometricPressure;
    private String mThrottlePos;
    private String mControlModuleVoltage;
    private String mFuelRailPressure = "";
    private String mFuelRailPressurevacuum = "";
    private String mDistanceTraveledMilOn;
    private String mDtcNumber;
    private String mRelThottlePos;
    private String mAbsLoad;
    private String mShortTermBank1;
    private String mShortTermBank2;
    private String mLongTermBank1;
    private String mLongTermBank2;

    private CheckRecordTwo() {
    }

    public static CheckRecordTwo getTriRecode(Context context) {
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
            case MAF://"MAF空气流量速率", massAirFlow + ""
                mMassAirFlow = Float.parseFloat(command.getFormattedResult());
                break;
            case FUEL_LEVEL://燃油油位  "燃油油位",
                mFuelLevel = command.getFormattedResult();
                setFuelLevel(TextUtils.isEmpty(mFuelLevel) ? "N/A" : mFuelLevel);
                break;
            case INTAKE_MANIFOLD_PRESSURE://"进气歧管压力", mIntakePressure + " kpa"
                mIntakePressure = Float.parseFloat(command.getCalculatedResult());
                setIntakePressure(mIntakePressure);
                break;
            case AIR_INTAKE_TEMPERATURE://"油箱空气温度", mIntakeAirTemp + " ℃"
                mIntakeAirTemp = Float.parseFloat(command.getCalculatedResult()) + 273.15f;
                setIntakeAirTemp(mIntakeAirTemp);
                break;
            case AMBIENT_AIR_TEMP://"环境空气温度", TextUtils.isEmpty(mAmbientAirTemp) ? "N/A" : mAmbientAirTemp + " ℃"
                mAmbientAirTemp = command.getFormattedResult();
                setAmbientAirTemp(TextUtils.isEmpty(mAmbientAirTemp) ? "N/A" : mAmbientAirTemp + " ℃");
                break;
            case ENGINE_COOLANT_TEMP://"引擎冷媒温度", TextUtils.isEmpty(mEngineCoolantTemp) ? "N/A" : mEngineCoolantTemp + " ℃"
                mEngineCoolantTemp = command.getFormattedResult();
                setEngineCoolantTemp(TextUtils.isEmpty(mEngineCoolantTemp) ? "N/A" : mEngineCoolantTemp + " ℃");
                break;
            case ENGINE_OIL_TEMP://"引擎油温", TextUtils.isEmpty(mEngineOilTemp) ? "N/A" : mEngineOilTemp
                mEngineOilTemp = command.getFormattedResult();
                setEngineOilTemp(TextUtils.isEmpty(mEngineOilTemp) ? "N/A" : mEngineOilTemp);
                break;
            case FUEL_PRESSURE://"油压", TextUtils.isEmpty(mFuelPressure) ? "N/A" : mFuelPressure
                mFuelPressure = command.getFormattedResult();
                setFuelPressure(TextUtils.isEmpty(mFuelPressure) ? "N/A" : mFuelPressure);
                break;
            case BAROMETRIC_PRESSURE://"绝对大气压", TextUtils.isEmpty(mBarometricPressure) ? "N/A" : mBarometricPressure
                mBarometricPressure = command.getFormattedResult();
                setBarometricPressure(TextUtils.isEmpty(mBarometricPressure) ? "N/A" : mBarometricPressure);
                break;
            case THROTTLE_POS://"油门位置", TextUtils.isEmpty(mThrottlePos) ? "N/A" : mThrottlePos
                mThrottlePos = command.getFormattedResult();
                setThrottlePos(TextUtils.isEmpty(mThrottlePos) ? "N/A" : mThrottlePos);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_1://"氧气侦测器1 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioOne) ? "N/A" : mWideBandAirFuelRatioOne
                mWideBandAirFuelRatioOne = command.getFormattedResult();
                setWideBandAirFuelRatioOne(TextUtils.isEmpty(mWideBandAirFuelRatioOne) ? "N/A" : mWideBandAirFuelRatioOne);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_2://"氧气侦测器2 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioTwo) ? "N/A" : mWideBandAirFuelRatioTwo
                mWideBandAirFuelRatioTwo = command.getFormattedResult();
                setWideBandAirFuelRatioTwo(TextUtils.isEmpty(mWideBandAirFuelRatioTwo) ? "N/A" : mWideBandAirFuelRatioTwo);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_3://"氧气侦测器3 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioThree) ? "N/A" : mWideBandAirFuelRatioThree
                mWideBandAirFuelRatioThree = command.getFormattedResult();
                setWideBandAirFuelRatioThree(TextUtils.isEmpty(mWideBandAirFuelRatioThree) ? "N/A" : mWideBandAirFuelRatioThree);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_4://"氧气侦测器4 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioFour) ? "N/A" : mWideBandAirFuelRatioFour
                mWideBandAirFuelRatioFour = command.getFormattedResult();
                setWideBandAirFuelRatioFour(TextUtils.isEmpty(mWideBandAirFuelRatioFour) ? "N/A" : mWideBandAirFuelRatioFour);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_5://"氧气侦测器5 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioFive) ? "N/A" : mWideBandAirFuelRatioFive
                mWideBandAirFuelRatioFive = command.getFormattedResult();
                setWideBandAirFuelRatioFive(TextUtils.isEmpty(mWideBandAirFuelRatioFive) ? "N/A" : mWideBandAirFuelRatioFive);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_6://"氧气侦测器6 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioSix) ? "N/A" : mWideBandAirFuelRatioSix
                mWideBandAirFuelRatioSix = command.getFormattedResult();
                setWideBandAirFuelRatioSix(TextUtils.isEmpty(mWideBandAirFuelRatioSix) ? "N/A" : mWideBandAirFuelRatioSix);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_7://"氧气侦测器7 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioSeven) ? "N/A" : mWideBandAirFuelRatioSeven
                mWideBandAirFuelRatioSeven = command.getFormattedResult();
                setWideBandAirFuelRatioSeven(TextUtils.isEmpty(mWideBandAirFuelRatioSeven) ? "N/A" : mWideBandAirFuelRatioSeven);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_8://"氧气侦测器8 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioEight) ? "N/A" : mWideBandAirFuelRatioEight
                mWideBandAirFuelRatioEight = command.getFormattedResult();
                setWideBandAirFuelRatioEight(TextUtils.isEmpty(mWideBandAirFuelRatioEight) ? "N/A" : mWideBandAirFuelRatioEight);
                break;
            case Catalyst_Temperature_Bank_1_Sensor_1://"催化剂温度:Bank1,感测器1", TextUtils.isEmpty(mCatalystTemperatureBank1Sensor1) ? "N/A" : mCatalystTemperatureBank1Sensor1
                mCatalystTemperatureBank1Sensor1 = command.getFormattedResult();
                setCatalystTemperatureBank1Sensor1(TextUtils.isEmpty(mCatalystTemperatureBank1Sensor1) ? "N/A" : mCatalystTemperatureBank1Sensor1);
                break;
            case Catalyst_Temperature_Bank_2_Sensor_1://"催化剂温度:Bank2,感测器1", TextUtils.isEmpty(mCatalystTemperatureBank2Sensor1) ? "N/A" : mCatalystTemperatureBank2Sensor1
                mCatalystTemperatureBank2Sensor1 = command.getFormattedResult();
                setCatalystTemperatureBank2Sensor1(TextUtils.isEmpty(mCatalystTemperatureBank2Sensor1) ? "N/A" : mCatalystTemperatureBank2Sensor1);
                break;
            case Catalyst_Temperature_Bank_1_Sensor_2://"催化剂温度:Bank1,感测器2", TextUtils.isEmpty(mCatalystTemperatureBank1Sensor2) ? "N/A" : mCatalystTemperatureBank1Sensor2
                mCatalystTemperatureBank1Sensor2 = command.getFormattedResult();
                setCatalystTemperatureBank1Sensor2(TextUtils.isEmpty(mCatalystTemperatureBank1Sensor2) ? "N/A" : mCatalystTemperatureBank1Sensor2);
                break;
            case Catalyst_Temperature_Bank_2_Sensor_2://"催化剂温度:Bank2,感测器2", TextUtils.isEmpty(mCatalystTemperatureBank2Sensor2) ? "N/A" : mCatalystTemperatureBank2Sensor2
                mCatalystTemperatureBank2Sensor2 = command.getFormattedResult();
                setCatalystTemperatureBank2Sensor2(TextUtils.isEmpty(mCatalystTemperatureBank2Sensor2) ? "N/A" : mCatalystTemperatureBank2Sensor2);
                break;
            case ABS_THROTTLE_POS_B://"绝对油门位置B", TextUtils.isEmpty(mAbsThrottlePosb) ? "N/A" : mAbsThrottlePosb
                mAbsThrottlePosb = command.getFormattedResult();
                setAbsThrottlePosb(TextUtils.isEmpty(mAbsThrottlePosb) ? "N/A" : mAbsThrottlePosb);
                break;
            case ABS_THROTTLE_POS_C://"绝对油门位置C", TextUtils.isEmpty(mAbsThrottlePosc) ? "N/A" : mAbsThrottlePosc
                mAbsThrottlePosc = command.getFormattedResult();
                setAbsThrottlePosc(TextUtils.isEmpty(mAbsThrottlePosc) ? "N/A" : mAbsThrottlePosc);
                break;
            case ACC_PEDAL_POS_D://"加速踏板位置D", TextUtils.isEmpty(mAccPedalPosd) ? "N/A" : mAccPedalPosd
                mAccPedalPosd = command.getFormattedResult();
                setAccPedalPosd(TextUtils.isEmpty(mAccPedalPosd) ? "N/A" : mAccPedalPosd);
                break;
            case ACC_PEDAL_POS_E://"加速踏板位置E", TextUtils.isEmpty(mAccPedalPose) ? "N/A" : mAccPedalPose
                mAccPedalPose = command.getFormattedResult();
                setAccPedalPose(TextUtils.isEmpty(mAccPedalPose) ? "N/A" : mAccPedalPose);
                break;
            case ACC_PEDAL_POS_F://"加速踏板位置F", TextUtils.isEmpty(mAccPedalPosf) ? "N/A" : mAccPedalPosf
                mAccPedalPosf = command.getFormattedResult();
                setAccPedalPosf(TextUtils.isEmpty(mAccPedalPosf) ? "N/A" : mAccPedalPosf);
                break;
            case THROTTLE_ACTUATOR://"油门执行器控制值", TextUtils.isEmpty(mThrottleActuator) ? "N/A" : mThrottleActuator
                mThrottleActuator = command.getFormattedResult();
                setThrottleActuator(TextUtils.isEmpty(mThrottleActuator) ? "N/A" : mThrottleActuator);
                break;
            case TIME_TRAVELED_MIL_ON://"MIL灯亮的行驶时间", TextUtils.isEmpty(mTimeRunwithMILOn) ? "N/A" : mTimeRunwithMILOn
                mTimeRunwithMILOn = command.getFormattedResult();
                setTimeRunwithMILOn(TextUtils.isEmpty(mTimeRunwithMILOn) ? "N/A" : mTimeRunwithMILOn);
                break;
            case SHORT_A_BANK1_B_BANK3://"第二侧氧气侦测器短期修正,A:bank 1,B:bank 3", TextUtils.isEmpty(mShortA_BANK1_B_BANK3) ? "N/A" : mShortA_BANK1_B_BANK3
                mShortA_BANK1_B_BANK3 = command.getFormattedResult();
                setShortA_BANK1_B_BANK3(TextUtils.isEmpty(mShortA_BANK1_B_BANK3) ? "N/A" : mShortA_BANK1_B_BANK3);
                break;
            case LONG_A_BANK1_B_BANK3://"第二侧氧气侦测器长期修正,A:bank 1,B:bank 3", TextUtils.isEmpty(mLong_A_BANK1_B_BANK3) ? "N/A" : mLong_A_BANK1_B_BANK3
                mLong_A_BANK1_B_BANK3 = command.getFormattedResult();
                setLong_A_BANK1_B_BANK3(TextUtils.isEmpty(mLong_A_BANK1_B_BANK3) ? "N/A" : mLong_A_BANK1_B_BANK3);
                break;
            case SHORT_A_BANK2_B_BANK4://"第二侧氧气侦测器短期修正,A:bank 2,B:bank 4", TextUtils.isEmpty(mShort_A_BANK2_B_BANK4) ? "N/A" : mShort_A_BANK2_B_BANK4
                mShort_A_BANK2_B_BANK4 = command.getFormattedResult();
                setShort_A_BANK2_B_BANK4(TextUtils.isEmpty(mShort_A_BANK2_B_BANK4) ? "N/A" : mShort_A_BANK2_B_BANK4);
                break;
            case LONG_A_BANK2_B_BANK4://"第二侧氧气侦测器长期修正,A:bank 2,B:bank 4", TextUtils.isEmpty(mLong_A_BANK2_B_BANK4) ? "N/A" : mLong_A_BANK2_B_BANK4
                mLong_A_BANK2_B_BANK4 = command.getFormattedResult();
                setLong_A_BANK2_B_BANK4(TextUtils.isEmpty(mLong_A_BANK2_B_BANK4) ? "N/A" : mLong_A_BANK2_B_BANK4);
                break;
            case CONTROL_MODULE_VOLTAGE://"控制模组电压", TextUtils.isEmpty(mControlModuleVoltage) ? "N/A" : mControlModuleVoltage
                mControlModuleVoltage = command.getFormattedResult();
                setControlModuleVoltage(TextUtils.isEmpty(mControlModuleVoltage) ? "N/A" : mControlModuleVoltage);
                break;
            case FUEL_RAIL_PRESSURE://"油轨压力(柴油或汽油直喷)", TextUtils.isEmpty(mFuelRailPressure) ? "N/A" : mFuelRailPressure
                mFuelRailPressure = command.getFormattedResult();
                setFuelRailPressure(TextUtils.isEmpty(mFuelRailPressure) ? "N/A" : mFuelRailPressure);
                break;
            case FUEL_RAIL_PRESSURE_manifold://"油轨压力(相对进气歧管真空度)", TextUtils.isEmpty(mFuelRailPressurevacuum) ? "N/A" : mFuelRailPressurevacuum
                mFuelRailPressurevacuum = command.getFormattedResult();
                setFuelRailPressurevacuum(TextUtils.isEmpty(mFuelRailPressurevacuum) ? "N/A" : mFuelRailPressurevacuum);
                break;
            case DISTANCE_TRAVELED_MIL_ON://"故障指示灯(MIL)亮时行驶的距离", TextUtils.isEmpty(mDistanceTraveledMilOn) ? "N/A" : mDistanceTraveledMilOn
                mDistanceTraveledMilOn = command.getFormattedResult();
                setDistanceTraveledMilOn(TextUtils.isEmpty(mDistanceTraveledMilOn) ? "N/A" : mDistanceTraveledMilOn);
                break;
            case DTC_NUMBER://"自从DTC清除后的监控状态", TextUtils.isEmpty(mDtcNumber) ? "N/A" : mDtcNumber
                mDtcNumber = command.getFormattedResult();
                setDtcNumber(TextUtils.isEmpty(mDtcNumber) ? "N/A" : mDtcNumber);
                break;
            case SYSTEM_VAPOR_PRESSURE://"系统蒸汽压力", TextUtils.isEmpty(mSystemVaporPressure) ? "N/A" : mSystemVaporPressure
                mSystemVaporPressure = command.getFormattedResult();
                setSystemVaporPressure(TextUtils.isEmpty(mSystemVaporPressure) ? "N/A" : mSystemVaporPressure);
                break;
            case REL_THROTTLE_POS://"相对油门位置", TextUtils.isEmpty(mRelThottlePos) ? "N/A" : mRelThottlePos
                mRelThottlePos = command.getFormattedResult();
                setRelThottlePos(TextUtils.isEmpty(mRelThottlePos) ? "N/A" : mRelThottlePos);
                break;
            case ABS_LOAD://"绝对负荷", TextUtils.isEmpty(mAbsLoad) ? "N/A" : mAbsLoad
                mAbsLoad = command.getFormattedResult();
                setAbsLoad(TextUtils.isEmpty(mAbsLoad) ? "N/A" : mAbsLoad);
                break;
            case SHORT_TERM_BANK_1://"短期燃油调节库1", TextUtils.isEmpty(mShortTermBank1) ? "N/A" : mShortTermBank1
                mShortTermBank1 = command.getFormattedResult();
                setShortTermBank1(TextUtils.isEmpty(mShortTermBank1) ? "N/A" : mShortTermBank1);
                break;
            case SHORT_TERM_BANK_2://"短期燃油调节库2", TextUtils.isEmpty(mShortTermBank2) ? "N/A" : mShortTermBank2
                mShortTermBank2 = command.getFormattedResult();
                setShortTermBank2(TextUtils.isEmpty(mShortTermBank2) ? "N/A" : mShortTermBank2);
                break;
            case LONG_TERM_BANK_1://"长期燃油调节库1", TextUtils.isEmpty(mLongTermBank1) ? "N/A" : mLongTermBank1
                mLongTermBank1 = command.getFormattedResult();
                setLongTermBank1(TextUtils.isEmpty(mLongTermBank1) ? "N/A" : mLongTermBank1);
                break;
            case LONG_TERM_BANK_2://"长期燃油调节库2", TextUtils.isEmpty(mLongTermBank2) ? "N/A" : mLongTermBank2
                mLongTermBank2 = command.getFormattedResult();
                setLongTermBank2(TextUtils.isEmpty(mLongTermBank2) ? "N/A" : mLongTermBank2);
                break;
        }
    }


    public void setSpeed(Integer currentSpeed) {
        speed = currentSpeed;
        if (speedMax < currentSpeed)
            speedMax = currentSpeed;
    }

    public Integer getSpeed() {
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

    public String getAbsThrottlePosb() {
        return mAbsThrottlePosb;
    }

    public void setAbsThrottlePosb(String mAbsThrottlePosb) {
        this.mAbsThrottlePosb = mAbsThrottlePosb;
    }

    public String getAbsThrottlePosc() {
        return mAbsThrottlePosc;
    }

    public void setAbsThrottlePosc(String mAbsThrottlePosc) {
        this.mAbsThrottlePosc = mAbsThrottlePosc;
    }

    public String getAccPedalPosd() {
        return mAccPedalPosd;
    }

    public void setAccPedalPosd(String mAccPedalPosd) {
        this.mAccPedalPosd = mAccPedalPosd;
    }

    public String getAccPedalPose() {
        return mAccPedalPose;
    }

    public void setAccPedalPose(String mAccPedalPose) {
        this.mAccPedalPose = mAccPedalPose;
    }

    public String getAccPedalPosf() {
        return mAccPedalPosf;
    }

    public void setAccPedalPosf(String mAccPedalPosf) {
        this.mAccPedalPosf = mAccPedalPosf;
    }

    public String getThrottleActuator() {
        return mThrottleActuator;
    }

    public void setThrottleActuator(String mThrottleActuator) {
        this.mThrottleActuator = mThrottleActuator;
    }

    public String getTimeRunwithMILOn() {
        return mTimeRunwithMILOn;
    }

    public void setTimeRunwithMILOn(String mTimeRunwithMILOn) {
        this.mTimeRunwithMILOn = mTimeRunwithMILOn;
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

    public String getFuelRailPressurevacuum() {
        return mFuelRailPressurevacuum;
    }

    public void setFuelRailPressurevacuum(String mFuelRailPressurevacuum) {
        this.mFuelRailPressurevacuum = mFuelRailPressurevacuum;
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

    public String getRelThottlePos() {
        return mRelThottlePos;
    }

    public void setRelThottlePos(String mRelThottlePos) {
        this.mRelThottlePos = mRelThottlePos;
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
