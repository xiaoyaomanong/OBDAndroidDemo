package com.sohrab.obd.reader.trip;

import java.io.Serializable;

/**
 * 作者：Jealous
 * 日期：2021/1/20 0020
 * 描述：
 */
public class OBDJsonTripEntity implements Serializable {
    private String Speed;//时速
    private String EngineRpm;//发动机转速
    private String IdlingDuration;//空闲时间
    private String DrivingDuration;//行驶时间
    private String SpeedMax;//最高时速
    private String EngineRpmMax;//最大转速
    private String RapidDeclTimes;//降速次数
    private String RapidAccTimes;//加速次数
    private String IdleMaf;//怠速空气流量
    private String DrivingMaf;//驱动空气流量
    private String InsFuelConsumption;//瞬时油耗
    private String DrivingFuelConsumption;//行驶油耗
    private String IdlingFuelConsumption;//怠速油耗
    private String IgnitionMonitor;//点火监视器
    private String DescribeProtocolNumber;//协议编号
    private String DescribeProtocol;//协议
    private String WideBandAirFuelRatio;//宽带空燃比
    private String AirFuelRatio;//燃油-空气命令等效比
    private String EngineOilTemp;//引擎油温
    private String RmCommandedEGR;//废气循环
    private String EgrError;//废气循环错误
    private String AbsLoad;//绝对负荷
    private String DistanceTraveledMilOn;//故障指示灯(MIL)亮时行驶的距离
    private String VehicleIdentificationNumber;//车辆识别号(VIN)
    private String FuelRailPressurevacuum;//油轨压力(相对进气歧管真空度)
    private String FuelRailPressure;//油轨压力(柴油或汽油直喷)
    private String ControlModuleVoltage;//控制模组电压
    private String DistanceTraveledAfterCodesCleared;//故障码清除后行驶里程
    private String EquivRatio;//指令当量比
    private String DtcNumber;//自从DTC清除后的监控状态
    private String TimingAdvance;//点火提前值
    private String FuelConsumptionRate;//燃油消耗率
    private String FuelSystemStatus;//燃油系统状态
    private String FuelTypeValue;//燃料类型
    private String FuelLevel;//燃油油位
    private String PermanentTroubleCode;//永久性故障代码
    private String PendingTroubleCode;//未决故障代码
    private String FaultCodes;//故障代码
    private String RelThottlePos;//相对油门位置
    private String ThottlePos;//油门位置
    private String MassAirFlow;//MAF空气流量速率
    private String EngineRuntime;//引擎启动后的运行时间
    private String EngineLoad;//引擎载荷
    private String IntakePressure;//进气歧管压力
    private String FuelPressure;//油压
    private String BarometricPressure;//绝对大气压
    private String EngineCoolantTemp;//引擎冷媒温度
    private String EngineFuelRate;//引擎油量消耗速率
    private String AmbientAirTemp;//环境空气温度
    private String IntakeAirTemp;//油箱空气温度
    private String ShortTermBank1;//短期燃油调节库1
    private String ShortTermBank2;//短期燃油调节库2
    private String LongTermBank1;//长期燃油调节库1
    private String LongTermBank2;//长期燃油调节库2
    private String EvaporaivePurge;//蒸发净化
    private String WarmUpSinceCodesCleared;//代码清除后的预热
    private String SystemVaporPressure;//系统蒸汽压力
    private String WideBandAirFuelRatioOne;//氧气侦测器1 燃油-空气当量比 电流
    private String WideBandAirFuelRatioTwo;//氧气侦测器2 燃油-空气当量比 电流
    private String WideBandAirFuelRatioThree;//氧气侦测器3 燃油-空气当量比 电流
    private String WideBandAirFuelRatioFour;//氧气侦测器4 燃油-空气当量比 电流
    private String WideBandAirFuelRatioFive;//氧气侦测器5 燃油-空气当量比 电流
    private String WideBandAirFuelRatioSix;//氧气侦测器6 燃油-空气当量比 电流
    private String WideBandAirFuelRatioSeven;//氧气侦测器7 燃油-空气当量比 电流
    private String WideBandAirFuelRatioEight;//氧气侦测器8 燃油-空气当量比 电流
    private String CatalystTemperatureBank1Sensor1;//催化剂温度:Bank1,感测器1
    private String CatalystTemperatureBank2Sensor1;//催化剂温度:Bank2,感测器1
    private String CatalystTemperatureBank1Sensor2;//催化剂温度:Bank1,感测器2
    private String CatalystTemperatureBank2Sensor2;//催化剂温度:Bank2,感测器2
    private String AbsThrottlePosb;//绝对油门位置B
    private String AbsThrottlePosc;//绝对油门位置C
    private String AccPedalPosd;//加速踏板位置D
    private String AccPedalPose;//加速踏板位置E
    private String AccPedalPosf;//加速踏板位置F
    private String ThrottleActuator;//油门执行器控制值
    private String TimeRunWithMILOn;//MIL灯亮的行驶时间
    private String TimeSinceTcClear;//故障代码清除后的时间
    private String MaxAirFlowMassRate;//质量空气流量计的最大空气流率
    private String EthanolFuelRate;//乙醇燃料百分比
    private String AbsEvapSystemVaporPressure;//蒸发系统绝对蒸气压力
    private String EvapSystemVaporPressure;//蒸发系统(相对)蒸气压力
    private String ShortA_BANK1_B_BANK3;//第二侧氧气侦测器短期修正,A:bank 1,B:bank 3
    private String Long_A_BANK1_B_BANK3;//第二侧氧气侦测器长期修正,A:bank 1,B:bank 3
    private String Short_A_BANK2_B_BANK4;//第二侧氧气侦测器短期修正,A:bank 2,B:bank 4
    private String Long_A_BANK2_B_BANK4;//第二侧氧气侦测器长期修正,A:bank 2,B:bank 4
    private String FuelRailAbsPressure;//高压共轨绝对压力
    private String RelAccPedalPos;//加速踏板相对位置
    private String HyBatteryPackLife;//油电混合电池组剩下寿命
    private String ActualEngineTorque;//实际引擎-扭矩百分比
    private String DPFTemp;//柴油粒子过滤器(DPF)温度
    private String EngineFrictionPercentTorque;//引擎摩擦力-扭矩百分比
    private String EngineReferenceTorque;//引擎参考扭矩

    public String getEngineFrictionPercentTorque() {
        return EngineFrictionPercentTorque;
    }

    public void setEngineFrictionPercentTorque(String engineFrictionPercentTorque) {
        EngineFrictionPercentTorque = engineFrictionPercentTorque;
    }

    public String getDPFTemp() {
        return DPFTemp;
    }

    public void setDPFTemp(String DPFTemp) {
        this.DPFTemp = DPFTemp;
    }

    public String getEngineReferenceTorque() {
        return EngineReferenceTorque;
    }

    public void setEngineReferenceTorque(String engineReferenceTorque) {
        EngineReferenceTorque = engineReferenceTorque;
    }

    public String getActualEngineTorque() {
        return ActualEngineTorque;
    }

    public void setActualEngineTorque(String actualEngineTorque) {
        ActualEngineTorque = actualEngineTorque;
    }

    public String getHyBatteryPackLife() {
        return HyBatteryPackLife;
    }

    public void setHyBatteryPackLife(String hyBatteryPackLife) {
        HyBatteryPackLife = hyBatteryPackLife;
    }

    public String getRelAccPedalPos() {
        return RelAccPedalPos;
    }

    public void setRelAccPedalPos(String relAccPedalPos) {
        RelAccPedalPos = relAccPedalPos;
    }

    public String getFuelRailAbsPressure() {
        return FuelRailAbsPressure;
    }

    public void setFuelRailAbsPressure(String fuelRailAbsPressure) {
        FuelRailAbsPressure = fuelRailAbsPressure;
    }

    public String getShortA_BANK1_B_BANK3() {
        return ShortA_BANK1_B_BANK3;
    }

    public void setShortA_BANK1_B_BANK3(String shortA_BANK1_B_BANK3) {
        ShortA_BANK1_B_BANK3 = shortA_BANK1_B_BANK3;
    }

    public String getLong_A_BANK1_B_BANK3() {
        return Long_A_BANK1_B_BANK3;
    }

    public void setLong_A_BANK1_B_BANK3(String long_A_BANK1_B_BANK3) {
        Long_A_BANK1_B_BANK3 = long_A_BANK1_B_BANK3;
    }

    public String getShort_A_BANK2_B_BANK4() {
        return Short_A_BANK2_B_BANK4;
    }

    public void setShort_A_BANK2_B_BANK4(String short_A_BANK2_B_BANK4) {
        Short_A_BANK2_B_BANK4 = short_A_BANK2_B_BANK4;
    }

    public String getLong_A_BANK2_B_BANK4() {
        return Long_A_BANK2_B_BANK4;
    }

    public void setLong_A_BANK2_B_BANK4(String long_A_BANK2_B_BANK4) {
        Long_A_BANK2_B_BANK4 = long_A_BANK2_B_BANK4;
    }

    public String getEvapSystemVaporPressure() {
        return EvapSystemVaporPressure;
    }

    public void setEvapSystemVaporPressure(String evapSystemVaporPressure) {
        EvapSystemVaporPressure = evapSystemVaporPressure;
    }

    public String getAbsEvapSystemVaporPressure() {
        return AbsEvapSystemVaporPressure;
    }

    public void setAbsEvapSystemVaporPressure(String absEvapSystemVaporPressure) {
        AbsEvapSystemVaporPressure = absEvapSystemVaporPressure;
    }

    public String getEthanolFuelRate() {
        return EthanolFuelRate;
    }

    public void setEthanolFuelRate(String ethanolFuelRate) {
        EthanolFuelRate = ethanolFuelRate;
    }

    public String getMaxAirFlowMassRate() {
        return MaxAirFlowMassRate;
    }

    public void setMaxAirFlowMassRate(String maxAirFlowMassRate) {
        MaxAirFlowMassRate = maxAirFlowMassRate;
    }

    public String getTimeSinceTcClear() {
        return TimeSinceTcClear;
    }

    public void setTimeSinceTcClear(String mTimeSinceTcClear) {
        this.TimeSinceTcClear = mTimeSinceTcClear;
    }

    public String getTimeRunWithMILOn() {
        return TimeRunWithMILOn;
    }

    public void setTimeRunWithMILOn(String TimeRunWithMILOn) {
        this.TimeRunWithMILOn = TimeRunWithMILOn;
    }

    public String getAbsThrottlePosb() {
        return AbsThrottlePosb;
    }

    public void setAbsThrottlePosb(String absThrottlePosb) {
        AbsThrottlePosb = absThrottlePosb;
    }

    public String getAbsThrottlePosc() {
        return AbsThrottlePosc;
    }

    public void setAbsThrottlePosc(String absThrottlePosc) {
        AbsThrottlePosc = absThrottlePosc;
    }

    public String getAccPedalPosd() {
        return AccPedalPosd;
    }

    public void setAccPedalPosd(String accPedalPosd) {
        AccPedalPosd = accPedalPosd;
    }

    public String getAccPedalPose() {
        return AccPedalPose;
    }

    public void setAccPedalPose(String accPedalPose) {
        AccPedalPose = accPedalPose;
    }

    public String getAccPedalPosf() {
        return AccPedalPosf;
    }

    public void setAccPedalPosf(String accPedalPosf) {
        AccPedalPosf = accPedalPosf;
    }

    public String getThrottleActuator() {
        return ThrottleActuator;
    }

    public void setThrottleActuator(String throttleActuator) {
        ThrottleActuator = throttleActuator;
    }

    public String getThottlePos() {
        return ThottlePos;
    }

    public void setThottlePos(String thottlePos) {
        ThottlePos = thottlePos;
    }

    public String getCatalystTemperatureBank1Sensor1() {
        return CatalystTemperatureBank1Sensor1;
    }

    public void setCatalystTemperatureBank1Sensor1(String catalystTemperatureBank1Sensor1) {
        CatalystTemperatureBank1Sensor1 = catalystTemperatureBank1Sensor1;
    }

    public String getCatalystTemperatureBank2Sensor1() {
        return CatalystTemperatureBank2Sensor1;
    }

    public void setCatalystTemperatureBank2Sensor1(String catalystTemperatureBank2Sensor1) {
        CatalystTemperatureBank2Sensor1 = catalystTemperatureBank2Sensor1;
    }

    public String getCatalystTemperatureBank1Sensor2() {
        return CatalystTemperatureBank1Sensor2;
    }

    public void setCatalystTemperatureBank1Sensor2(String catalystTemperatureBank1Sensor2) {
        CatalystTemperatureBank1Sensor2 = catalystTemperatureBank1Sensor2;
    }

    public String getCatalystTemperatureBank2Sensor2() {
        return CatalystTemperatureBank2Sensor2;
    }

    public void setCatalystTemperatureBank2Sensor2(String catalystTemperatureBank2Sensor2) {
        CatalystTemperatureBank2Sensor2 = catalystTemperatureBank2Sensor2;
    }

    public String getWideBandAirFuelRatioOne() {
        return WideBandAirFuelRatioOne;
    }

    public void setWideBandAirFuelRatioOne(String wideBandAirFuelRatioOne) {
        WideBandAirFuelRatioOne = wideBandAirFuelRatioOne;
    }

    public String getWideBandAirFuelRatioTwo() {
        return WideBandAirFuelRatioTwo;
    }

    public void setWideBandAirFuelRatioTwo(String wideBandAirFuelRatioTwo) {
        WideBandAirFuelRatioTwo = wideBandAirFuelRatioTwo;
    }

    public String getWideBandAirFuelRatioThree() {
        return WideBandAirFuelRatioThree;
    }

    public void setWideBandAirFuelRatioThree(String wideBandAirFuelRatioThree) {
        WideBandAirFuelRatioThree = wideBandAirFuelRatioThree;
    }

    public String getWideBandAirFuelRatioFour() {
        return WideBandAirFuelRatioFour;
    }

    public void setWideBandAirFuelRatioFour(String wideBandAirFuelRatioFour) {
        WideBandAirFuelRatioFour = wideBandAirFuelRatioFour;
    }

    public String getWideBandAirFuelRatioFive() {
        return WideBandAirFuelRatioFive;
    }

    public void setWideBandAirFuelRatioFive(String wideBandAirFuelRatioFive) {
        WideBandAirFuelRatioFive = wideBandAirFuelRatioFive;
    }

    public String getWideBandAirFuelRatioSix() {
        return WideBandAirFuelRatioSix;
    }

    public void setWideBandAirFuelRatioSix(String wideBandAirFuelRatioSix) {
        WideBandAirFuelRatioSix = wideBandAirFuelRatioSix;
    }

    public String getWideBandAirFuelRatioSeven() {
        return WideBandAirFuelRatioSeven;
    }

    public void setWideBandAirFuelRatioSeven(String wideBandAirFuelRatioSeven) {
        WideBandAirFuelRatioSeven = wideBandAirFuelRatioSeven;
    }

    public String getWideBandAirFuelRatioEight() {
        return WideBandAirFuelRatioEight;
    }

    public void setWideBandAirFuelRatioEight(String wideBandAirFuelRatioEight) {
        WideBandAirFuelRatioEight = wideBandAirFuelRatioEight;
    }

    public String getSystemVaporPressure() {
        return SystemVaporPressure;
    }

    public void setSystemVaporPressure(String SystemVaporPressure) {
        this.SystemVaporPressure = SystemVaporPressure;
    }

    public String getWarmUpSinceCodesCleared() {
        return WarmUpSinceCodesCleared;
    }

    public void setWarmUpSinceCodesCleared(String WarmUpSinceCodesCleared) {
        this.WarmUpSinceCodesCleared = WarmUpSinceCodesCleared;
    }

    public String getEvaporaivePurge() {
        return EvaporaivePurge;
    }

    public void setEvaporaivePurge(String EvaporaivePurge) {
        this.EvaporaivePurge = EvaporaivePurge;
    }

    public String getEgrError() {
        return EgrError;
    }

    public void setEgrError(String egrError) {
        EgrError = egrError;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }

    public String getEngineRpm() {
        return EngineRpm;
    }

    public void setEngineRpm(String engineRpm) {
        EngineRpm = engineRpm;
    }

    public String getIdlingDuration() {
        return IdlingDuration;
    }

    public void setIdlingDuration(String idlingDuration) {
        IdlingDuration = idlingDuration;
    }

    public String getDrivingDuration() {
        return DrivingDuration;
    }

    public void setDrivingDuration(String drivingDuration) {
        DrivingDuration = drivingDuration;
    }

    public String getSpeedMax() {
        return SpeedMax;
    }

    public void setSpeedMax(String speedMax) {
        SpeedMax = speedMax;
    }

    public String getEngineRpmMax() {
        return EngineRpmMax;
    }

    public void setEngineRpmMax(String engineRpmMax) {
        EngineRpmMax = engineRpmMax;
    }

    public String getRapidDeclTimes() {
        return RapidDeclTimes;
    }

    public void setRapidDeclTimes(String rapidDeclTimes) {
        RapidDeclTimes = rapidDeclTimes;
    }

    public String getRapidAccTimes() {
        return RapidAccTimes;
    }

    public void setRapidAccTimes(String rapidAccTimes) {
        RapidAccTimes = rapidAccTimes;
    }

    public String getIdleMaf() {
        return IdleMaf;
    }

    public void setIdleMaf(String idleMaf) {
        IdleMaf = idleMaf;
    }

    public String getDrivingMaf() {
        return DrivingMaf;
    }

    public void setDrivingMaf(String drivingMaf) {
        DrivingMaf = drivingMaf;
    }

    public String getInsFuelConsumption() {
        return InsFuelConsumption;
    }

    public void setInsFuelConsumption(String insFuelConsumption) {
        InsFuelConsumption = insFuelConsumption;
    }

    public String getDrivingFuelConsumption() {
        return DrivingFuelConsumption;
    }

    public void setDrivingFuelConsumption(String drivingFuelConsumption) {
        DrivingFuelConsumption = drivingFuelConsumption;
    }

    public String getIdlingFuelConsumption() {
        return IdlingFuelConsumption;
    }

    public void setIdlingFuelConsumption(String idlingFuelConsumption) {
        IdlingFuelConsumption = idlingFuelConsumption;
    }

    public String getIgnitionMonitor() {
        return IgnitionMonitor;
    }

    public void setIgnitionMonitor(String ignitionMonitor) {
        IgnitionMonitor = ignitionMonitor;
    }

    public String getDescribeProtocolNumber() {
        return DescribeProtocolNumber;
    }

    public void setDescribeProtocolNumber(String describeProtocolNumber) {
        DescribeProtocolNumber = describeProtocolNumber;
    }

    public String getDescribeProtocol() {
        return DescribeProtocol;
    }

    public void setDescribeProtocol(String describeProtocol) {
        DescribeProtocol = describeProtocol;
    }

    public String getWideBandAirFuelRatio() {
        return WideBandAirFuelRatio;
    }

    public void setWideBandAirFuelRatio(String wideBandAirFuelRatio) {
        WideBandAirFuelRatio = wideBandAirFuelRatio;
    }

    public String getAirFuelRatio() {
        return AirFuelRatio;
    }

    public void setAirFuelRatio(String airFuelRatio) {
        AirFuelRatio = airFuelRatio;
    }

    public String getEngineOilTemp() {
        return EngineOilTemp;
    }

    public void setEngineOilTemp(String engineOilTemp) {
        EngineOilTemp = engineOilTemp;
    }

    public String getRmCommandedEGR() {
        return RmCommandedEGR;
    }

    public void setRmCommandedEGR(String rmCommandedEGR) {
        RmCommandedEGR = rmCommandedEGR;
    }

    public String getAbsLoad() {
        return AbsLoad;
    }

    public void setAbsLoad(String absLoad) {
        AbsLoad = absLoad;
    }

    public String getDistanceTraveledMilOn() {
        return DistanceTraveledMilOn;
    }

    public void setDistanceTraveledMilOn(String distanceTraveledMilOn) {
        DistanceTraveledMilOn = distanceTraveledMilOn;
    }

    public String getVehicleIdentificationNumber() {
        return VehicleIdentificationNumber;
    }

    public void setVehicleIdentificationNumber(String vehicleIdentificationNumber) {
        VehicleIdentificationNumber = vehicleIdentificationNumber;
    }

    public String getFuelRailPressurevacuum() {
        return FuelRailPressurevacuum;
    }

    public void setFuelRailPressurevacuum(String fuelRailPressurevacuum) {
        FuelRailPressurevacuum = fuelRailPressurevacuum;
    }

    public String getFuelRailPressure() {
        return FuelRailPressure;
    }

    public void setFuelRailPressure(String fuelRailPressure) {
        FuelRailPressure = fuelRailPressure;
    }

    public String getControlModuleVoltage() {
        return ControlModuleVoltage;
    }

    public void setControlModuleVoltage(String controlModuleVoltage) {
        ControlModuleVoltage = controlModuleVoltage;
    }

    public String getDistanceTraveledAfterCodesCleared() {
        return DistanceTraveledAfterCodesCleared;
    }

    public void setDistanceTraveledAfterCodesCleared(String distanceTraveledAfterCodesCleared) {
        DistanceTraveledAfterCodesCleared = distanceTraveledAfterCodesCleared;
    }

    public String getEquivRatio() {
        return EquivRatio;
    }

    public void setEquivRatio(String equivRatio) {
        EquivRatio = equivRatio;
    }

    public String getDtcNumber() {
        return DtcNumber;
    }

    public void setDtcNumber(String dtcNumber) {
        DtcNumber = dtcNumber;
    }

    public String getTimingAdvance() {
        return TimingAdvance;
    }

    public void setTimingAdvance(String timingAdvance) {
        TimingAdvance = timingAdvance;
    }

    public String getFuelConsumptionRate() {
        return FuelConsumptionRate;
    }

    public void setFuelConsumptionRate(String fuelConsumptionRate) {
        FuelConsumptionRate = fuelConsumptionRate;
    }

    public String getFuelSystemStatus() {
        return FuelSystemStatus;
    }

    public void setFuelSystemStatus(String fuelSystemStatus) {
        FuelSystemStatus = fuelSystemStatus;
    }

    public String getFuelTypeValue() {
        return FuelTypeValue;
    }

    public void setFuelTypeValue(String fuelTypeValue) {
        FuelTypeValue = fuelTypeValue;
    }

    public String getFuelLevel() {
        return FuelLevel;
    }

    public void setFuelLevel(String fuelLevel) {
        FuelLevel = fuelLevel;
    }

    public String getPermanentTroubleCode() {
        return PermanentTroubleCode;
    }

    public void setPermanentTroubleCode(String permanentTroubleCode) {
        PermanentTroubleCode = permanentTroubleCode;
    }

    public String getPendingTroubleCode() {
        return PendingTroubleCode;
    }

    public void setPendingTroubleCode(String pendingTroubleCode) {
        PendingTroubleCode = pendingTroubleCode;
    }

    public String getFaultCodes() {
        return FaultCodes;
    }

    public void setFaultCodes(String faultCodes) {
        FaultCodes = faultCodes;
    }

    public String getRelThottlePos() {
        return RelThottlePos;
    }

    public void setRelThottlePos(String relThottlePos) {
        RelThottlePos = relThottlePos;
    }

    public String getMassAirFlow() {
        return MassAirFlow;
    }

    public void setMassAirFlow(String massAirFlow) {
        MassAirFlow = massAirFlow;
    }

    public String getEngineRuntime() {
        return EngineRuntime;
    }

    public void setEngineRuntime(String engineRuntime) {
        EngineRuntime = engineRuntime;
    }

    public String getEngineLoad() {
        return EngineLoad;
    }

    public void setEngineLoad(String engineLoad) {
        EngineLoad = engineLoad;
    }

    public String getIntakePressure() {
        return IntakePressure;
    }

    public void setIntakePressure(String intakePressure) {
        IntakePressure = intakePressure;
    }

    public String getFuelPressure() {
        return FuelPressure;
    }

    public void setFuelPressure(String fuelPressure) {
        FuelPressure = fuelPressure;
    }

    public String getBarometricPressure() {
        return BarometricPressure;
    }

    public void setBarometricPressure(String barometricPressure) {
        BarometricPressure = barometricPressure;
    }

    public String getEngineCoolantTemp() {
        return EngineCoolantTemp;
    }

    public void setEngineCoolantTemp(String engineCoolantTemp) {
        EngineCoolantTemp = engineCoolantTemp;
    }

    public String getAmbientAirTemp() {
        return AmbientAirTemp;
    }

    public void setAmbientAirTemp(String ambientAirTemp) {
        AmbientAirTemp = ambientAirTemp;
    }

    public String getIntakeAirTemp() {
        return IntakeAirTemp;
    }

    public void setIntakeAirTemp(String intakeAirTemp) {
        IntakeAirTemp = intakeAirTemp;
    }

    public String getShortTermBank1() {
        return ShortTermBank1;
    }

    public void setShortTermBank1(String shortTermBank1) {
        ShortTermBank1 = shortTermBank1;
    }

    public String getShortTermBank2() {
        return ShortTermBank2;
    }

    public void setShortTermBank2(String shortTermBank2) {
        ShortTermBank2 = shortTermBank2;
    }

    public String getLongTermBank1() {
        return LongTermBank1;
    }

    public void setLongTermBank1(String longTermBank1) {
        LongTermBank1 = longTermBank1;
    }

    public String getLongTermBank2() {
        return LongTermBank2;
    }

    public void setLongTermBank2(String longTermBank2) {
        LongTermBank2 = longTermBank2;
    }

    public String getEngineFuelRate() {
        return EngineFuelRate;
    }

    public void setEngineFuelRate(String engineFuelRate) {
        EngineFuelRate = engineFuelRate;
    }
}