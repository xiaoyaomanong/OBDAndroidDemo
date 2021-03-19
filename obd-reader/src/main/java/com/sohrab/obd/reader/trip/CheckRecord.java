package com.sohrab.obd.reader.trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.constants.DefineObdReader;
import com.sohrab.obd.reader.enums.FuelType;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import app.com.android_obd_reader.R;

/**
 * 作者：Jealous
 * 日期：2021/3/6
 * 描述：
 */
public class CheckRecord implements DefineObdReader, Serializable {
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
    private final static String SYSTEM_VAPOR_PRESSURE = "System Vapor Pressure";
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
    private final static String EGR = "EGR";
    private final static String EGR_ERROR = "EGR Error";
    private final static String SHORT_TERM_BANK_1 = "Short Term Fuel Trim Bank 1";
    private final static String SHORT_TERM_BANK_2 = "Short Term Fuel Trim Bank 2";
    private final static String LONG_TERM_BANK_1 = "Long Term Fuel Trim Bank 1";
    private final static String LONG_TERM_BANK_2 = "Long Term Fuel Trim Bank 2";
    private final static String EVAPORAIVE_PURGE = "evaporative purge";
    private final static String WarmUpSinceCodesCleared = "Warm Up Since Codes Cleared Command";

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
    private final static String MAX_AIR_FLOW_MASS_RATE = "Maximum value for air flow rate from mass air flow sensor";
    private final static String ETHANOL_FUEL_RATE = "Ethanol fuel %";
    private final static String ABS_EVAP_SYSTEM_VAPOR_PRESSURE = "Absolute Evap system Vapor Pressure";
    private final static String EVAP_SYSTEM_VAPOR_PRESSURE = "Evap system vapor pressure";

    private final static String SHORT_A_BANK1_B_BANK3="Short term secondary oxygen sensor trim, A: bank 1, B: bank 3";

    private final static String LONG_A_BANK1_B_BANK3= "Long term secondary oxygen sensor trim, A: bank 1, B: bank 3";

    private final static String SHORT_A_BANK2_B_BANK4= "Short term secondary oxygen sensor trim, A: bank 2, B: bank 4";

    private final static String LONG_A_BANK2_B_BANK4= "Long term secondary oxygen sensor trim, A: bank 2, B: bank 4";
    private final static String FUEL_RAIL_ABS_PRESSURE= "Fuel rail absolute pressure";
    private final static String REL_ACCELERATOR_PEDAL_POS= "Relative accelerator pedal position";
    private final static String HY_BATTERY_PACK_LIFE= "Hybrid battery pack remaining life";
    private final static String DRIVER_ENGINE_TORQUE= "Driver's demand engine - percent torque";
    private final static String ACTUAL_ENGINE_TORQUE= "Actual engine - percent torque";

    @SuppressLint("StaticFieldLeak")
    private static CheckRecord sInstance;
    private Integer engineRpmMax = 0;
    private String engineRpm;
    private String mActualEngineTorque;
    private String mDriverEngineTorque;
    private String mHyBatteryPackLife;
    private String mRelAccPedalPos;
    private String mFuelRailAbsPressure;
    private String mMaxAirFlowMassRate;
    private String mEthanolFuelRate;
    private String mAbsEvapSystemVaporPressure;
    private String mEvapSystemVaporPressure;
    private  String mShortA_BANK1_B_BANK3;
    private  String mLong_A_BANK1_B_BANK3;
    private  String mShort_A_BANK2_B_BANK4;
    private  String mLong_A_BANK2_B_BANK4;
    private Integer speed = -1;
    private Integer speedMax = 0;
    private String engineRuntime;
    private final long tripStartTime;
    private float idlingDuration;
    private float drivingDuration;
    private float AverageSpeed;
    private long mAddSpeed;
    private long mSpeedCount;
    private float mDistanceTravel;
    private float mMassAirFlow;
    private int mRapidAccTimes = 0;
    private int mRapidDeclTimes = 0;
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

    private String mWarmUpSinceCodesCleared;
    private String mEngineCoolantTemp;
    private String mEngineOilTemp;
    private String mSystemVaporPressure;
    private String mFuelConsumptionRate;
    private String mFuelSystemStatus;
    private String mFuelPressure;
    private String mEGR;
    private String mEGRError;
    private String mEvaporaivePurge;
    private String mEngineLoad;
    private String mBarometricPressure;
    private String mThrottlePos;
    private String mTimingAdvance;
    private String mPermanentTroubleCode;
    private String mPendingTroubleCode;
    private String mEquivRatio;
    private String mDistanceTraveledAfterCodesCleared;
    private String mControlModuleVoltage;
    private String mEngineFuelRate;
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
    private String mShortTermBank1;
    private String mShortTermBank2;
    private String mLongTermBank1;
    private String mLongTermBank2;
    private List<OBDTripEntity> datas = new ArrayList<>();
    private final List<OBDTripEntity> data = new ArrayList<>();
    private OBDJsonTripEntity entity = new OBDJsonTripEntity();

    private CheckRecord() {
        tripStartTime = System.currentTimeMillis();
        mTripIdentifier = UUID.randomUUID().toString();
    }

    public static CheckRecord getTriRecode(Context context) {
        sContext = context;
        if (sInstance == null)
            sInstance = new CheckRecord();
        return sInstance;
    }

    public Integer getSpeed() {
        if (speed == -1)
            return 0;
        return speed;
    }

    public String getActualEngineTorque() {
        return mActualEngineTorque;
    }

    public String getDriverEngineTorque() {
        return mDriverEngineTorque;
    }

    public String getHyBatteryPackLife() {
        return mHyBatteryPackLife;
    }

    public String getRelAccPedalPos() {
        return mRelAccPedalPos;
    }

    public String getFuelRailAbsPressure() {
        return mFuelRailAbsPressure;
    }

    public String getmShortA_BANK1_B_BANK3() {
        return mShortA_BANK1_B_BANK3;
    }

    public String getmLong_A_BANK1_B_BANK3() {
        return mLong_A_BANK1_B_BANK3;
    }

    public String getmShort_A_BANK2_B_BANK4() {
        return mShort_A_BANK2_B_BANK4;
    }

    public String getmLong_A_BANK2_B_BANK4() {
        return mLong_A_BANK2_B_BANK4;
    }

    public String getEvapSystemVaporPressure() {
        return mEvapSystemVaporPressure;
    }
    public String getAbsEvapSystemVaporPressure() {
        return mAbsEvapSystemVaporPressure;
    }

    public String getEthanolFuelRate() {
        return mEthanolFuelRate;
    }

    public String getmMaxAirFlowMassRate() {
        return mMaxAirFlowMassRate;
    }

    public String getTimeRunwithMILOn() {
        return mTimeRunwithMILOn;
    }

    public String getAbsThrottlePosb() {
        return mAbsThrottlePosb;
    }

    public String getAbsThrottlePosc() {
        return mAbsThrottlePosc;
    }

    public String getAccPedalPosd() {
        return mAccPedalPosd;
    }

    public String getAccPedalPose() {
        return mAccPedalPose;
    }

    public String getAccPedalPosf() {
        return mAccPedalPosf;
    }

    public String getThrottleActuator() {
        return mThrottleActuator;
    }

    public String getCatalystTemperatureBank1Sensor1() {
        return mCatalystTemperatureBank1Sensor1;
    }

    public String getCatalystTemperatureBank2Sensor1() {
        return mCatalystTemperatureBank2Sensor1;
    }

    public String getCatalystTemperatureBank1Sensor2() {
        return mCatalystTemperatureBank1Sensor2;
    }

    public String getCatalystTemperatureBank2Sensor2() {
        return mCatalystTemperatureBank2Sensor2;
    }

    public String getWideBandAirFuelRatioOne() {
        return mWideBandAirFuelRatioOne;
    }

    public String getWideBandAirFuelRatioTwo() {
        return mWideBandAirFuelRatioTwo;
    }

    public String getWideBandAirFuelRatioThree() {
        return mWideBandAirFuelRatioThree;
    }

    public String getWideBandAirFuelRatioFour() {
        return mWideBandAirFuelRatioFour;
    }

    public String getWideBandAirFuelRatioFive() {
        return mWideBandAirFuelRatioFive;
    }

    public String getWideBandAirFuelRatioSix() {
        return mWideBandAirFuelRatioSix;
    }

    public String getWideBandAirFuelRatioSeven() {
        return mWideBandAirFuelRatioSeven;
    }

    public String getWideBandAirFuelRatioEight() {
        return mWideBandAirFuelRatioEight;
    }

    public String getSystemVaporPressure() {
        return mSystemVaporPressure;
    }

    public String getEvaporaivePurge() {
        return mEvaporaivePurge;
    }

    public String getWarmUpSinceCodesCleared() {
        return mWarmUpSinceCodesCleared;
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

    public float getAverageSpeed() {
        return AverageSpeed;
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

    public static Context getsContext() {
        return sContext;
    }

    public String getmShortTermBank1() {
        return mShortTermBank1;
    }

    public String getmShortTermBank2() {
        return mShortTermBank2;
    }

    public String getmLongTermBank1() {
        return mLongTermBank1;
    }

    public String getmLongTermBank2() {
        return mLongTermBank2;
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
        data.add(new OBDTripEntity("最大转速", engineRpmMax + " ", R.drawable.icon_max_engine_zs));
        entity.setEngineRpmMax(String.valueOf(engineRpmMax));
    }

    public void findIdleAndDrivingFuelConsumtion(float currentMaf) {
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

    public String getmEGRError() {
        return mEGRError;
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
                findInsFualConsumption(mMassAirFlow);
                data.add(new OBDTripEntity("MAF空气流量速率", mMassAirFlow + "", R.drawable.icon_kqzlll));
                entity.setMassAirFlow(mMassAirFlow + "");
                setmIsMAFSupported(true);
                break;
            case ENGINE_RUNTIME:
                engineRuntime = command.getFormattedResult();
                data.add(new OBDTripEntity("发动机启动后的运行时间", TextUtils.isEmpty(engineRuntime) ? "" : engineRuntime, R.drawable.icon_engine_time));
                entity.setEngineRuntime(TextUtils.isEmpty(engineRuntime) ? "" : engineRuntime);
                break;

            case FUEL_LEVEL:
                mFuelLevel = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油油位", TextUtils.isEmpty(mFuelLevel) ? "" : mFuelLevel, R.drawable.icon_fuel_level));
                entity.setFuelLevel(TextUtils.isEmpty(mFuelLevel) ? "" : mFuelLevel);
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
                data.add(new OBDTripEntity("邮箱空气温度", mIntakeAirTemp + " ℃", R.drawable.icon_intake_air_temp));
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
                data.add(new OBDTripEntity("发动机冷媒温度", TextUtils.isEmpty(mEngineCoolantTemp) ? "" : mEngineCoolantTemp + " ℃", R.drawable.icon_fdjlqywd));
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
                data.add(new OBDTripEntity("油压", TextUtils.isEmpty(mFuelPressure) ? "" : mFuelPressure, R.drawable.icon_ryyl));
                entity.setFuelPressure(TextUtils.isEmpty(mFuelPressure) ? "" : mFuelPressure);
                break;
            case ENGINE_LOAD:
                mEngineLoad = command.getFormattedResult();
                data.add(new OBDTripEntity("发动机载荷", TextUtils.isEmpty(mEngineLoad) ? "" : mEngineLoad, R.drawable.icon_fdjfh));
                entity.setEngineLoad(TextUtils.isEmpty(mEngineLoad) ? "" : mEngineLoad);
                break;
            case EGR:
                mEGR = command.getFormattedResult();
                data.add(new OBDTripEntity("废气循环", TextUtils.isEmpty(mEGR) ? "" : mEGR, R.drawable.icon_zlegr));
                entity.setRmCommandedEGR(TextUtils.isEmpty(mEGR) ? "" : mEGR);
                break;
            case EGR_ERROR:
                mEGRError = command.getFormattedResult();
                data.add(new OBDTripEntity("废气循环错误", TextUtils.isEmpty(mEGRError) ? "" : mEGRError, R.drawable.icon_zlegr));
                entity.setEgrError(TextUtils.isEmpty(mEGRError) ? "" : mEGRError);
                break;
            case EVAPORAIVE_PURGE:
                mEvaporaivePurge = command.getFormattedResult();
                data.add(new OBDTripEntity("蒸发净化", TextUtils.isEmpty(mEvaporaivePurge) ? "" : mEvaporaivePurge, R.drawable.icon_zlegr));
                entity.setEvaporaivePurge(TextUtils.isEmpty(mEvaporaivePurge) ? "" : mEvaporaivePurge);
                break;
            case BAROMETRIC_PRESSURE:
                mBarometricPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对大气压", TextUtils.isEmpty(mBarometricPressure) ? "" : mBarometricPressure, R.drawable.icon_dqy));
                entity.setBarometricPressure(TextUtils.isEmpty(mBarometricPressure) ? "" : mBarometricPressure);
                break;
            case THROTTLE_POS:
                mThrottlePos = command.getFormattedResult();
                data.add(new OBDTripEntity("节气门位置", TextUtils.isEmpty(mThrottlePos) ? "" : mThrottlePos, R.drawable.icon_jqmwz));
                entity.setThottlePos(TextUtils.isEmpty(mThrottlePos) ? "" : mThrottlePos);
                break;
            case TIMING_ADVANCE:
                mTimingAdvance = command.getFormattedResult();
                data.add(new OBDTripEntity("点火提前值", TextUtils.isEmpty(mTimingAdvance) ? "" : mTimingAdvance, R.drawable.icon_timing_advance));
                entity.setTimingAdvance(TextUtils.isEmpty(mTimingAdvance) ? "" : mTimingAdvance);
                break;
            case WarmUpSinceCodesCleared:
                mWarmUpSinceCodesCleared = command.getFormattedResult();
                data.add(new OBDTripEntity("代码清除后的预热", TextUtils.isEmpty(mWarmUpSinceCodesCleared) ? "" : mWarmUpSinceCodesCleared, R.drawable.icon_timing_advance));
                entity.setWarmUpSinceCodesCleared(TextUtils.isEmpty(mWarmUpSinceCodesCleared) ? "" : mWarmUpSinceCodesCleared);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_1:
                mWideBandAirFuelRatioOne = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器1 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioOne) ? "" : mWideBandAirFuelRatioOne, R.drawable.icon_timing_advance));
                entity.setWideBandAirFuelRatioOne(TextUtils.isEmpty(mWideBandAirFuelRatioOne) ? "" : mWideBandAirFuelRatioOne);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_2:
                mWideBandAirFuelRatioTwo = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器2 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioTwo) ? "" : mWideBandAirFuelRatioTwo, R.drawable.icon_timing_advance));
                entity.setWideBandAirFuelRatioTwo(TextUtils.isEmpty(mWideBandAirFuelRatioTwo) ? "" : mWideBandAirFuelRatioTwo);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_3:
                mWideBandAirFuelRatioThree = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器3 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioThree) ? "" : mWideBandAirFuelRatioThree, R.drawable.icon_timing_advance));
                entity.setWideBandAirFuelRatioThree(TextUtils.isEmpty(mWideBandAirFuelRatioThree) ? "" : mWideBandAirFuelRatioThree);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_4:
                mWideBandAirFuelRatioFour = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器4 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioFour) ? "" : mWideBandAirFuelRatioFour, R.drawable.icon_timing_advance));
                entity.setWideBandAirFuelRatioFour(TextUtils.isEmpty(mWideBandAirFuelRatioFour) ? "" : mWideBandAirFuelRatioFour);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_5:
                mWideBandAirFuelRatioFive = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器5 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioFive) ? "" : mWideBandAirFuelRatioFive, R.drawable.icon_timing_advance));
                entity.setWideBandAirFuelRatioFive(TextUtils.isEmpty(mWideBandAirFuelRatioFive) ? "" : mWideBandAirFuelRatioFive);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_6:
                mWideBandAirFuelRatioSix = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器6 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioSix) ? "" : mWideBandAirFuelRatioSix, R.drawable.icon_timing_advance));
                entity.setWideBandAirFuelRatioSix(TextUtils.isEmpty(mWideBandAirFuelRatioSix) ? "" : mWideBandAirFuelRatioSix);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_7:
                mWideBandAirFuelRatioSeven = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器7 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioSeven) ? "" : mWideBandAirFuelRatioSeven, R.drawable.icon_timing_advance));
                entity.setWideBandAirFuelRatioSeven(TextUtils.isEmpty(mWideBandAirFuelRatioSeven) ? "" : mWideBandAirFuelRatioSeven);
                break;
            case WIDE_BAND_AIR_FUEL_RATIO_8:
                mWideBandAirFuelRatioEight = command.getFormattedResult();
                data.add(new OBDTripEntity("氧气侦测器8 燃油-空气当量比 电流", TextUtils.isEmpty(mWideBandAirFuelRatioEight) ? "" : mWideBandAirFuelRatioEight, R.drawable.icon_timing_advance));
                entity.setWideBandAirFuelRatioEight(TextUtils.isEmpty(mWideBandAirFuelRatioEight) ? "" : mWideBandAirFuelRatioEight);
                break;
            case Catalyst_Temperature_Bank_1_Sensor_1:
                mCatalystTemperatureBank1Sensor1 = command.getFormattedResult();
                data.add(new OBDTripEntity("催化剂温度:Bank1,感测器1", TextUtils.isEmpty(mCatalystTemperatureBank1Sensor1) ? "" : mCatalystTemperatureBank1Sensor1, R.drawable.icon_timing_advance));
                entity.setCatalystTemperatureBank1Sensor1(TextUtils.isEmpty(mCatalystTemperatureBank1Sensor1) ? "" : mCatalystTemperatureBank1Sensor1);
                break;
            case Catalyst_Temperature_Bank_2_Sensor_1:
                mCatalystTemperatureBank2Sensor1 = command.getFormattedResult();
                data.add(new OBDTripEntity("催化剂温度:Bank2,感测器1", TextUtils.isEmpty(mCatalystTemperatureBank2Sensor1) ? "" : mCatalystTemperatureBank2Sensor1, R.drawable.icon_timing_advance));
                entity.setCatalystTemperatureBank2Sensor1(TextUtils.isEmpty(mCatalystTemperatureBank2Sensor1) ? "" : mCatalystTemperatureBank2Sensor1);
                break;
            case Catalyst_Temperature_Bank_1_Sensor_2:
                mCatalystTemperatureBank1Sensor2 = command.getFormattedResult();
                data.add(new OBDTripEntity("催化剂温度:Bank1,感测器2", TextUtils.isEmpty(mCatalystTemperatureBank1Sensor2) ? "" : mCatalystTemperatureBank1Sensor2, R.drawable.icon_timing_advance));
                entity.setCatalystTemperatureBank1Sensor2(TextUtils.isEmpty(mCatalystTemperatureBank1Sensor2) ? "" : mCatalystTemperatureBank1Sensor2);
                break;
            case Catalyst_Temperature_Bank_2_Sensor_2:
                mCatalystTemperatureBank2Sensor2 = command.getFormattedResult();
                data.add(new OBDTripEntity("催化剂温度:Bank2,感测器2", TextUtils.isEmpty(mCatalystTemperatureBank2Sensor2) ? "" : mCatalystTemperatureBank2Sensor2, R.drawable.icon_timing_advance));
                entity.setCatalystTemperatureBank2Sensor2(TextUtils.isEmpty(mCatalystTemperatureBank2Sensor2) ? "" : mCatalystTemperatureBank2Sensor2);
                break;
            case ABS_THROTTLE_POS_B:
                mAbsThrottlePosb = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对油门位置B", TextUtils.isEmpty(mAbsThrottlePosb) ? "" : mAbsThrottlePosb, R.drawable.icon_timing_advance));
                entity.setAbsThrottlePosb(TextUtils.isEmpty(mAbsThrottlePosb) ? "" : mAbsThrottlePosb);
                break;
            case ABS_THROTTLE_POS_C:
                mAbsThrottlePosc = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对油门位置C", TextUtils.isEmpty(mAbsThrottlePosc) ? "" : mAbsThrottlePosc, R.drawable.icon_timing_advance));
                entity.setAbsThrottlePosc(TextUtils.isEmpty(mAbsThrottlePosc) ? "" : mAbsThrottlePosc);
                break;
            case ACC_PEDAL_POS_D:
                mAccPedalPosd = command.getFormattedResult();
                data.add(new OBDTripEntity("加速踏板位置D", TextUtils.isEmpty(mAccPedalPosd) ? "" : mAccPedalPosd, R.drawable.icon_timing_advance));
                entity.setAccPedalPosd(TextUtils.isEmpty(mAccPedalPosd) ? "" : mAccPedalPosd);
                break;
            case ACC_PEDAL_POS_E:
                mAccPedalPose = command.getFormattedResult();
                data.add(new OBDTripEntity("加速踏板位置E", TextUtils.isEmpty(mAccPedalPose) ? "" : mAccPedalPose, R.drawable.icon_timing_advance));
                entity.setAccPedalPose(TextUtils.isEmpty(mAccPedalPose) ? "" : mAccPedalPose);
                break;
            case ACC_PEDAL_POS_F:
                mAccPedalPosf = command.getFormattedResult();
                data.add(new OBDTripEntity("加速踏板位置F", TextUtils.isEmpty(mAccPedalPosf) ? "" : mAccPedalPosf, R.drawable.icon_timing_advance));
                entity.setAccPedalPosf(TextUtils.isEmpty(mAccPedalPosf) ? "" : mAccPedalPosf);
                break;
            case THROTTLE_ACTUATOR:
                mThrottleActuator = command.getFormattedResult();
                data.add(new OBDTripEntity("油门执行器控制值", TextUtils.isEmpty(mThrottleActuator) ? "" : mThrottleActuator, R.drawable.icon_timing_advance));
                entity.setThrottleActuator(TextUtils.isEmpty(mThrottleActuator) ? "" : mThrottleActuator);
                break;
            case TIME_TRAVELED_MIL_ON:
                mTimeRunwithMILOn = command.getFormattedResult();
                data.add(new OBDTripEntity("MIL灯亮的行驶时间", TextUtils.isEmpty(mTimeRunwithMILOn) ? "" : mTimeRunwithMILOn, R.drawable.icon_timing_advance));
                entity.setTimeRunWithMILOn(TextUtils.isEmpty(mTimeRunwithMILOn) ? "" : mTimeRunwithMILOn);
                break;
            case MAX_AIR_FLOW_MASS_RATE:
                mMaxAirFlowMassRate = command.getFormattedResult();
                data.add(new OBDTripEntity("质量空气流量计的最大空气流率", TextUtils.isEmpty(mMaxAirFlowMassRate) ? "" : mMaxAirFlowMassRate, R.drawable.icon_timing_advance));
                entity.setMaxAirFlowMassRate(TextUtils.isEmpty(mMaxAirFlowMassRate) ? "" : mMaxAirFlowMassRate);
                break;
            case ETHANOL_FUEL_RATE:
                mEthanolFuelRate = command.getFormattedResult();
                data.add(new OBDTripEntity("乙醇燃料百分比", TextUtils.isEmpty(mEthanolFuelRate) ? "" : mEthanolFuelRate, R.drawable.icon_timing_advance));
                entity.setEthanolFuelRate(TextUtils.isEmpty(mEthanolFuelRate) ? "" : mEthanolFuelRate);
                break;
            case ABS_EVAP_SYSTEM_VAPOR_PRESSURE:
                mAbsEvapSystemVaporPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("蒸发系统绝对蒸气压力", TextUtils.isEmpty(mAbsEvapSystemVaporPressure) ? "" : mAbsEvapSystemVaporPressure, R.drawable.icon_timing_advance));
                entity.setAbsEvapSystemVaporPressure(TextUtils.isEmpty(mAbsEvapSystemVaporPressure) ? "" : mAbsEvapSystemVaporPressure);
                break;
            case EVAP_SYSTEM_VAPOR_PRESSURE:
                mEvapSystemVaporPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("蒸发系统(相对)蒸气压力", TextUtils.isEmpty(mEvapSystemVaporPressure) ? "" : mEvapSystemVaporPressure, R.drawable.icon_timing_advance));
                entity.setEvapSystemVaporPressure(TextUtils.isEmpty(mEvapSystemVaporPressure) ? "" : mEvapSystemVaporPressure);
                break;
            case SHORT_A_BANK1_B_BANK3:
                mShortA_BANK1_B_BANK3 = command.getFormattedResult();
                data.add(new OBDTripEntity("第二侧氧气侦测器短期修正,A:bank 1,B:bank 3", TextUtils.isEmpty(mShortA_BANK1_B_BANK3) ? "" : mShortA_BANK1_B_BANK3, R.drawable.icon_timing_advance));
                entity.setShortA_BANK1_B_BANK3(TextUtils.isEmpty(mShortA_BANK1_B_BANK3) ? "" : mShortA_BANK1_B_BANK3);
                break;
            case LONG_A_BANK1_B_BANK3:
                mLong_A_BANK1_B_BANK3 = command.getFormattedResult();
                data.add(new OBDTripEntity("第二侧氧气侦测器长期修正,A:bank 1,B:bank 3", TextUtils.isEmpty(mLong_A_BANK1_B_BANK3) ? "" : mLong_A_BANK1_B_BANK3, R.drawable.icon_timing_advance));
                entity.setLong_A_BANK1_B_BANK3(TextUtils.isEmpty(mLong_A_BANK1_B_BANK3) ? "" : mLong_A_BANK1_B_BANK3);
                break;
            case SHORT_A_BANK2_B_BANK4:
                mShort_A_BANK2_B_BANK4 = command.getFormattedResult();
                data.add(new OBDTripEntity("第二侧氧气侦测器短期修正,A:bank 2,B:bank 4", TextUtils.isEmpty(mShort_A_BANK2_B_BANK4) ? "" : mShort_A_BANK2_B_BANK4, R.drawable.icon_timing_advance));
                entity.setShort_A_BANK2_B_BANK4(TextUtils.isEmpty(mShort_A_BANK2_B_BANK4) ? "" : mShort_A_BANK2_B_BANK4);
                break;
            case LONG_A_BANK2_B_BANK4:
                mLong_A_BANK2_B_BANK4 = command.getFormattedResult();
                data.add(new OBDTripEntity("第二侧氧气侦测器长期修正,A:bank 2,B:bank 4", TextUtils.isEmpty(mLong_A_BANK2_B_BANK4) ? "" : mLong_A_BANK2_B_BANK4, R.drawable.icon_timing_advance));
                entity.setLong_A_BANK2_B_BANK4(TextUtils.isEmpty(mLong_A_BANK2_B_BANK4) ? "" : mLong_A_BANK2_B_BANK4);
                break;
            case FUEL_RAIL_ABS_PRESSURE:
                mFuelRailAbsPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("高压共轨绝对压力", TextUtils.isEmpty(mFuelRailAbsPressure) ? "" : mFuelRailAbsPressure, R.drawable.icon_timing_advance));
                entity.setFuelRailAbsPressure(TextUtils.isEmpty(mFuelRailAbsPressure) ? "" : mFuelRailAbsPressure);
                break;
            case REL_ACCELERATOR_PEDAL_POS:
                mRelAccPedalPos = command.getFormattedResult();
                data.add(new OBDTripEntity("加速踏板相对位置", TextUtils.isEmpty(mRelAccPedalPos) ? "" : mRelAccPedalPos, R.drawable.icon_timing_advance));
                entity.setRelAccPedalPos(TextUtils.isEmpty(mRelAccPedalPos) ? "" : mRelAccPedalPos);
                break;
            case HY_BATTERY_PACK_LIFE:
                mHyBatteryPackLife = command.getFormattedResult();
                data.add(new OBDTripEntity("油电混合电池组剩下寿命", TextUtils.isEmpty(mHyBatteryPackLife) ? "" : mHyBatteryPackLife, R.drawable.icon_timing_advance));
                entity.setHyBatteryPackLife(TextUtils.isEmpty(mHyBatteryPackLife) ? "" : mHyBatteryPackLife);
                break;
            case DRIVER_ENGINE_TORQUE:
                mDriverEngineTorque = command.getFormattedResult();
                data.add(new OBDTripEntity("驾驶的发动机命令-力矩百分比", TextUtils.isEmpty(mDriverEngineTorque) ? "" : mDriverEngineTorque, R.drawable.icon_timing_advance));
                entity.setHyBatteryPackLife(TextUtils.isEmpty(mDriverEngineTorque) ? "" : mDriverEngineTorque);
                break;
            case ACTUAL_ENGINE_TORQUE:
                mActualEngineTorque = command.getFormattedResult();
                data.add(new OBDTripEntity("实际发动机-力矩百分比", TextUtils.isEmpty(mActualEngineTorque) ? "" : mActualEngineTorque, R.drawable.icon_timing_advance));
                entity.setActualEngineTorque(TextUtils.isEmpty(mActualEngineTorque) ? "" : mActualEngineTorque);
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
                data.add(new OBDTripEntity("故障码清除后行驶里程", TextUtils.isEmpty(mDistanceTraveledAfterCodesCleared) ? "" : mDistanceTraveledAfterCodesCleared, R.drawable.icon_distance_code_clear));
                entity.setDistanceTraveledAfterCodesCleared(mDistanceTraveledAfterCodesCleared);
                break;
            case CONTROL_MODULE_VOLTAGE:
                mControlModuleVoltage = command.getFormattedResult();
                data.add(new OBDTripEntity("控制模组电压", TextUtils.isEmpty(mControlModuleVoltage) ? "" : mControlModuleVoltage, R.drawable.icon_control_model_voltage));
                entity.setControlModuleVoltage(TextUtils.isEmpty(mControlModuleVoltage) ? "" : mControlModuleVoltage);
                break;
            case ENGINE_FUEL_RATE:
                mEngineFuelRate = command.getFormattedResult();
                data.add(new OBDTripEntity("引擎油量消耗速率", TextUtils.isEmpty(mEngineFuelRate) ? "" : mEngineFuelRate, R.drawable.icon_engine_fuel_rate));
                entity.setEngineFuelRate(TextUtils.isEmpty(mEngineFuelRate) ? "" : mEngineFuelRate);
                break;
            case FUEL_RAIL_PRESSURE:
                mFuelRailPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("油轨压力(柴油或汽油直喷)", TextUtils.isEmpty(mFuelRailPressure) ? "" : mFuelRailPressure, R.drawable.icon_fule_rail_pressure));
                entity.setFuelRailPressure(TextUtils.isEmpty(mFuelRailPressure) ? "" : mFuelRailPressure);
                break;
            case FUEL_RAIL_PRESSURE_manifold:
                mFuelRailPressurevacuum = command.getFormattedResult();
                data.add(new OBDTripEntity("油轨压力(相对进气歧管真空度)", TextUtils.isEmpty(mFuelRailPressurevacuum) ? "" : mFuelRailPressurevacuum, R.drawable.icon_fyg_yl));
                entity.setFuelRailPressurevacuum(TextUtils.isEmpty(mFuelRailPressurevacuum) ? "" : mFuelRailPressurevacuum);
                break;
            case VIN:
                mVehicleIdentificationNumber = command.getFormattedResult();
                data.add(new OBDTripEntity("车辆识别号（VIN）", TextUtils.isEmpty(mVehicleIdentificationNumber) ? "" : mVehicleIdentificationNumber, R.drawable.icon_vin));
                entity.setVehicleIdentificationNumber(TextUtils.isEmpty(mVehicleIdentificationNumber) ? "" : mVehicleIdentificationNumber);
                break;

            case DISTANCE_TRAVELED_MIL_ON:
                mDistanceTraveledMilOn = command.getFormattedResult();
                data.add(new OBDTripEntity("故障指示灯（MIL）亮时行驶的距离", TextUtils.isEmpty(mDistanceTraveledMilOn) ? "" : mDistanceTraveledMilOn, R.drawable.icon_mil_jl));
                entity.setDistanceTraveledMilOn(mDistanceTraveledMilOn);
                break;
            case DTC_NUMBER:
                mDtcNumber = command.getFormattedResult();
                data.add(new OBDTripEntity("自从DTC清除后的监控状态", TextUtils.isEmpty(mDtcNumber) ? "" : mDtcNumber, R.drawable.icon_dtc_number));
                entity.setDtcNumber(TextUtils.isEmpty(mDtcNumber) ? "" : mDtcNumber);
                break;
            case SYSTEM_VAPOR_PRESSURE:
                mSystemVaporPressure = command.getFormattedResult();
                data.add(new OBDTripEntity("系统蒸汽压力", TextUtils.isEmpty(mSystemVaporPressure) ? "" : mSystemVaporPressure, R.drawable.icon_dtc_number));
                entity.setSystemVaporPressure(TextUtils.isEmpty(mSystemVaporPressure) ? "" : mSystemVaporPressure);
                break;
            case TIME_SINCE_TC_CLEARED:
                mTimeSinceTcClear = command.getFormattedResult();
                data.add(new OBDTripEntity("故障代码清除后的时间", TextUtils.isEmpty(mTimeSinceTcClear) ? "" : mTimeSinceTcClear, R.drawable.icon_abs));
                entity.setTimeSinceTcClear(TextUtils.isEmpty(mTimeSinceTcClear) ? "" : mTimeSinceTcClear);
                break;
            case REL_THROTTLE_POS:
                mRelThottlePos = command.getFormattedResult();
                data.add(new OBDTripEntity("相对油门位置", TextUtils.isEmpty(mRelThottlePos) ? "" : mRelThottlePos, R.drawable.icon_abs));
                entity.setRelThottlePos(TextUtils.isEmpty(mRelThottlePos) ? "" : mRelThottlePos);
                break;
            case ABS_LOAD:
                mAbsLoad = command.getFormattedResult();
                data.add(new OBDTripEntity("绝对负荷", TextUtils.isEmpty(mAbsLoad) ? "" : mAbsLoad, R.drawable.icon_abs));
                entity.setAbsLoad(TextUtils.isEmpty(mAbsLoad) ? "" : mAbsLoad);
                break;

            case AIR_FUEL_RATIO:
                mAirFuelRatio = command.getFormattedResult();
                data.add(new OBDTripEntity("燃油-空气命令等效比", TextUtils.isEmpty(mAirFuelRatio) ? "" : mAirFuelRatio, R.drawable.icon_krb));
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
            case SHORT_TERM_BANK_1:
                mShortTermBank1 = command.getFormattedResult();
                data.add(new OBDTripEntity("短期燃油调节库1", TextUtils.isEmpty(mShortTermBank1) ? "" : mShortTermBank1, R.drawable.icon_short_one));
                entity.setShortTermBank1(TextUtils.isEmpty(mShortTermBank1) ? "" : mShortTermBank1);
                break;
            case SHORT_TERM_BANK_2:
                mShortTermBank2 = command.getFormattedResult();
                data.add(new OBDTripEntity("短期燃油调节库2", TextUtils.isEmpty(mShortTermBank2) ? "" : mShortTermBank2, R.drawable.icon_short_two));
                entity.setShortTermBank2(TextUtils.isEmpty(mShortTermBank2) ? "" : mShortTermBank2);
                break;
            case LONG_TERM_BANK_1:
                mLongTermBank1 = command.getFormattedResult();
                data.add(new OBDTripEntity("长期燃油调节库1", TextUtils.isEmpty(mLongTermBank1) ? "" : mLongTermBank1, R.drawable.icon_long_one));
                entity.setLongTermBank1(TextUtils.isEmpty(mLongTermBank1) ? "" : mLongTermBank1);
                break;
            case LONG_TERM_BANK_2:
                mLongTermBank2 = command.getFormattedResult();
                data.add(new OBDTripEntity("长期燃油调节库2", TextUtils.isEmpty(mLongTermBank2) ? "" : mLongTermBank2, R.drawable.icon_long_two));
                entity.setLongTermBank2(TextUtils.isEmpty(mLongTermBank2) ? "" : mLongTermBank2);
                break;
        }
        datas.addAll(data);
        setTripMap(datas);
        setOBDJson(entity);
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
        if (drivingDuration <= 0) {
            AverageSpeed = 0f;
        } else {
            AverageSpeed = mDistanceTravel / (drivingDuration / (60 * 60 * 1000));
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
        data.add(new OBDTripEntity("加速次数", mRapidAccTimes + "", R.drawable.icon_add_times));
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
    public String getmFuelLevel() {
        return mFuelLevel;
    }

    public float getmMassAirFlow() {
        return mMassAirFlow;
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

    public String getmEGR() {
        return mEGR;
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
        return removeDuplicate(data);
    }

    private List<OBDTripEntity> removeDuplicate(List<OBDTripEntity> list) {
        HashSet<OBDTripEntity> set = new HashSet<>(list.size());
        List<OBDTripEntity> result = new ArrayList<>(list.size());
        for (OBDTripEntity str : list) {
            if (set.add(str)) {
                result.add(str);
            }
        }
        list.clear();
        list.addAll(result);
        return list;
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
