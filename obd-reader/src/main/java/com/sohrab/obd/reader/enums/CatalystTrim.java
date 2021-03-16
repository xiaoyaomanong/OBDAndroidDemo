package com.sohrab.obd.reader.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public enum CatalystTrim {
    Catalyst_Temperature_Bank_1_Sensor_1("3C", "Catalyst Temperature: Bank 1, Sensor 1"),
    Catalyst_Temperature_Bank_2_Sensor_1("3D", "Catalyst Temperature: Bank 2, Sensor 1"),
    Catalyst_Temperature_Bank_1_Sensor_2("3E", "Catalyst Temperature: Bank 1, Sensor 2"),
    Catalyst_Temperature_Bank_2_Sensor_2("3F", "Catalyst Temperature: Bank 2, Sensor 2");

    /**
     * Constant <code>map</code>
     */
    private static final Map<Integer, FuelTrim> map = new HashMap<>();

    static {
        for (FuelTrim error : FuelTrim.values())
            map.put(error.getValue(), error);
    }

    private final String value;
    private final String bank;

    CatalystTrim(final String value, final String bank) {
        this.value = value;
        this.bank = bank;
    }

    /**
     * <p>fromValue.</p>
     *
     * @param value a int.
     */
    public static FuelTrim fromValue(final int value) {
        return map.get(value);
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a int.
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Getter for the field <code>bank</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getBank() {
        return bank;
    }

    /**
     * <p>buildObdCommand.</p>
     *
     * @return a {@link String} object.
     */
    public final String buildObdCommand() {
        return "01 " + value;
    }
}