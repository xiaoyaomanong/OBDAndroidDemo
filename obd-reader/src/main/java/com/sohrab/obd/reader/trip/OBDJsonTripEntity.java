package com.sohrab.obd.reader.trip;

import java.io.Serializable;

/**
 * 作者：Jealous
 * 日期：2021/1/20 0020
 * 描述：
 */
public class OBDJsonTripEntity implements Serializable {
    private String Speed;
    private String EngineRpm;
    private String IdlingDuration;
    private String DrivingDuration;
    private String SpeedMax;
    private String EngineRpmMax;
    private String RapidDeclTimes;
    private String RapidAccTimes;
    private String IdleMaf;
    private String DrivingMaf;
    private String InsFuelConsumption;
    private String DrivingFuelConsumption;
    private String IdlingFuelConsumption;
    private String IgnitionMonitor;
    private String DescribeProtocolNumber;
    private String DescribeProtocol;
    private String WideBandAirFuelRatio;
    private String AirFuelRatio;
    private String EngineOilTemp;
    private String RmCommandedEGR;
    private String EgrError;
    private String AbsLoad;
    private String DistanceTraveledMilOn;
    private String VehicleIdentificationNumber;
    private String FuelRailPressurevacuum;
    private String FuelRailPressure;
    private String ControlModuleVoltage;
    private String DistanceTraveledAfterCodesCleared;
    private String EquivRatio;
    private String DtcNumber;
    private String TimingAdvance;
    private String FuelConsumptionRate;
    private String FuelSystemStatus;
    private String FuelTypeValue;
    private String FuelLevel;
    private String PermanentTroubleCode;
    private String PendingTroubleCode;
    private String FaultCodes;
    private String RelThottlePos;
    private String ThottlePos;
    private String MassAirFlow;
    private String EngineRuntime;
    private String EngineLoad;
    private String IntakePressure;
    private String FuelPressure;
    private String BarometricPressure;
    private String EngineCoolantTemp;
    private String EngineFuelRate;
    private String AmbientAirTemp;
    private String IntakeAirTemp;
    private String ShortTermBank1;
    private String ShortTermBank2;
    private String LongTermBank1;
    private String LongTermBank2;
    private String EvaporaivePurge;
    private String WarmUpSinceCodesCleared;
    private String SystemVaporPressure;
    private String WideBandAirFuelRatioOne;
    private String WideBandAirFuelRatioTwo;
    private String WideBandAirFuelRatioThree;
    private String WideBandAirFuelRatioFour;
    private String WideBandAirFuelRatioFive;
    private String WideBandAirFuelRatioSix;
    private String WideBandAirFuelRatioSeven;
    private String WideBandAirFuelRatioEight;
    private String CatalystTemperatureBank1Sensor1;
    private String CatalystTemperatureBank2Sensor1;
    private String CatalystTemperatureBank1Sensor2;
    private String CatalystTemperatureBank2Sensor2;
    private String AbsThrottlePosb;
    private String AbsThrottlePosc;
    private String AccPedalPosd;
    private String AccPedalPose;
    private String AccPedalPosf;
    private String ThrottleActuator;
    private String TimeRunWithMILOn;
    private String TimeSinceTcClear;
    private String MaxAirFlowMassRate;
    private String EthanolFuelRate;
    private String AbsEvapSystemVaporPressure;
    private String EvapSystemVaporPressure;
    private String ShortA_BANK1_B_BANK3;
    private String Long_A_BANK1_B_BANK3;
    private String Short_A_BANK2_B_BANK4;
    private String Long_A_BANK2_B_BANK4;
    private String FuelRailAbsPressure;
    private String RelAccPedalPos;
    private String HyBatteryPackLife;
    private String ActualEngineTorque;
    private String DPFTemp;
    private String EngineFrictionPercentTorque;

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

    private String EngineReferenceTorque;

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