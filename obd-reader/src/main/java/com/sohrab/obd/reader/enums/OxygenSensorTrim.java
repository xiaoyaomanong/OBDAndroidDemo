package com.sohrab.obd.reader.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public enum OxygenSensorTrim {
    SHORT_A_BANK1_B_BANK3("55", "Short term secondary oxygen sensor trim, A: bank 1, B: bank 3"),
    LONG_A_BANK1_B_BANK3("56", "Long term secondary oxygen sensor trim, A: bank 1, B: bank 3"),
    SHORT_A_BANK2_B_BANK4("57", "Short term secondary oxygen sensor trim, A: bank 2, B: bank 4"),
    LONG_A_BANK2_B_BANK4("58", "Long term secondary oxygen sensor trim, A: bank 2, B: bank 4");

    /**
     * Constant <code>map</code>
     */
    private static final Map<String, OxygenSensorTrim> map = new HashMap<>();

    static {
        for (OxygenSensorTrim error : OxygenSensorTrim.values())
            map.put(error.getValue(), error);
    }

    private final String value;
    private final String bank;

    OxygenSensorTrim(final String value, final String bank) {
        this.value = value;
        this.bank = bank;
    }

    /**
     * <p>fromValue.</p>
     *
     * @param value a int.
     */
    public static OxygenSensorTrim fromValue(final int value) {
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
        return value;
    }
}