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
    REL_THROTTLE_POS("Relative throttle position"),
    PIDS_01_20("Available PIDs 01-20"),
    PIDS_21_40("Available PIDs 21-40"),
    PIDS_41_60("Available PIDs 41-60"),
    ABS_LOAD("Absolute load"),
    Commanded_EGR("Commanded EGR"),
    ENGINE_OIL_TEMP("Engine oil temperature"),
    AIR_FUEL_RATIO("Air/Fuel Ratio"),
    WIDEBAND_AIR_FUEL_RATIO("Wideband Air/Fuel Ratio"),
    DESCRIBE_PROTOCOL("Describe protocol"),
    DESCRIBE_PROTOCOL_NUMBER("Describe protocol number"),
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
