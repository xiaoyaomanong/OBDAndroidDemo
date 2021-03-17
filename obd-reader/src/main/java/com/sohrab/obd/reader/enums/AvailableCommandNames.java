package com.sohrab.obd.reader.enums;

/**
 * Names of all available commands.
 *
 * @author pires
 * @version $Id: $Id
 */
public enum AvailableCommandNames {

    AIR_INTAKE_TEMP("Air Intake Temperature"),
    AMBIENT_AIR_TEMP("Ambient Air Temperature"),
    ENGINE_COOLANT_TEMP("Engine Coolant Temperature"),
    BAROMETRIC_PRESSURE("Barometric Pressure"),
    FUEL_PRESSURE("Fuel Pressure"),
    INTAKE_MANIFOLD_PRESSURE("Intake Manifold Pressure"),
    ENGINE_LOAD("Engine Load"),
    ENGINE_RUNTIME("Engine Runtime"),
    ENGINE_RPM("Engine RPM"),
    SPEED("Vehicle Speed"),
    MAF("Mass Air Flow"),
    THROTTLE_POS("Throttle Position"),
    TROUBLE_CODES("Trouble Codes"),
    PENDING_TROUBLE_CODES("Pending Trouble Codes"),
    PERMANENT_TROUBLE_CODES("Permanent Trouble Codes"),
    FUEL_LEVEL("Fuel Level"),
    FUEL_TYPE("Fuel Type"),
    FUEL_SYSTEM_STATUS("Fuel System Status"),
    FUEL_CONSUMPTION_RATE("Fuel Consumption Rate"),
    TIMING_ADVANCE("Timing Advance"),
    DTC_NUMBER("Diagnostic Trouble Codes"),
    EQUIV_RATIO("Command Equivalence Ratio"),
    DISTANCE_TRAVELED_AFTER_CODES_CLEARED("Distance since codes cleared"),
    CONTROL_MODULE_VOLTAGE("Control Module Power Supply "),
    ENGINE_FUEL_RATE("Engine Fuel Rate"),
    FUEL_RAIL_PRESSURE("Fuel Rail Pressure"),
    FUEL_RAIL_PRESSURE_manifold("Fuel Rail Pressure relative to manifold vacuum"),
    VIN("Vehicle Identification Number (VIN)"),
    DISTANCE_TRAVELED_MIL_ON("Distance traveled with MIL on"),
    OBD_STANDARDS("OBD standards this vehicle conforms to"),
    TIME_TRAVELED_MIL_ON("Time run with MIL on"),
    TIME_SINCE_TC_CLEARED("Time since trouble codes cleared"),
    MAX_AIR_FLOW_MASS_RATE("Maximum value for air flow rate from mass air flow sensor"),
    ABS_EVAP_SYSTEM_VAPOR_PRESSURE("Absolute Evap system Vapor Pressure"),
    FUEL_RAIL_ABS_PRESSURE("Fuel rail absolute pressure"),
    REL_ACCELERATOR_PEDAL_POS("Relative accelerator pedal position"),
    HY_BATTERY_PACK_LIFE("Hybrid battery pack remaining life"),
    DRIVER_ENGINE_TORQUE("Driver's demand engine - percent torque"),
    ACTUAL_ENGINE_TORQUE("Actual engine - percent torque"),
    ENGINE_REFERENCE_TORQUE("Engine reference torque"),
    DPF_TEMP("Diesel Particulate filter (DPF) temperature"),
    ENGINE_FRICTION_PERCENT_TORQUE("Engine Friction - Percent Torque"),
    EVAP_SYSTEM_VAPOR_PRESSURE("Evap system vapor pressure"),
    ETHANOL_FUEL_RATE("Ethanol fuel %"),
    REL_THROTTLE_POS("Relative throttle position"),
    PIDS_01_20("Available PIDs 01-20"),
    PIDS_21_40("Available PIDs 21-40"),
    PIDS_41_60("Available PIDs 41-60"),
    ABS_LOAD("Absolute load"),
    EGR("EGR"),
    EGR_ERROR("EGR Error"),
    EVAPORAIVE_PURGE("evaporative purge"),
    SYSTEM_VAPOR_PRESSURE("System Vapor Pressure"),
    ODOMETER("Odometer"),
    ENGINE_OIL_TEMP("Engine oil temperature"),
    AIR_FUEL_RATIO("Air/Fuel Ratio"),
    WIDEBAND_AIR_FUEL_RATIO_1("Wideband Air/Fuel Ratio Oxygen Sensor 1"),
    WIDEBAND_AIR_FUEL_RATIO_2("Wideband Air/Fuel Ratio Oxygen Sensor 2"),
    WIDEBAND_AIR_FUEL_RATIO_3("Wideband Air/Fuel Ratio Oxygen Sensor 3"),
    WIDEBAND_AIR_FUEL_RATIO_4("Wideband Air/Fuel Ratio Oxygen Sensor 4"),
    WIDEBAND_AIR_FUEL_RATIO_5("Wideband Air/Fuel Ratio Oxygen Sensor 5"),
    WIDEBAND_AIR_FUEL_RATIO_6("Wideband Air/Fuel Ratio Oxygen Sensor 6"),
    WIDEBAND_AIR_FUEL_RATIO_7("Wideband Air/Fuel Ratio Oxygen Sensor 7"),
    WIDEBAND_AIR_FUEL_RATIO_8("Wideband Air/Fuel Ratio Oxygen Sensor 8"),
    DESCRIBE_PROTOCOL("Describe protocol"),
    DESCRIBE_PROTOCOL_NUMBER("Describe protocol number"),
    CLEAR_TROUBLE_CODE("Clear diagnostic error code"),
    IGNITION_MONITOR("Ignition monitor");

    private final String value;

    /**
     * @param value Command description
     */
    AvailableCommandNames(String value) {
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public final String getValue() {
        return value;
    }

}
