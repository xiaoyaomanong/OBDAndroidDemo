
package com.sohrab.obd.reader.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Select one of the Fuel Trim percentage banks to access.
 */
public enum FuelTrim {

    SHORT_TERM_BANK_1("06", "Short Term Fuel Trim Bank 1"),
    LONG_TERM_BANK_1("07", "Long Term Fuel Trim Bank 1"),
    SHORT_TERM_BANK_2("08", "Short Term Fuel Trim Bank 2"),
    LONG_TERM_BANK_2("09", "Long Term Fuel Trim Bank 2");

    /**
     * Constant <code>map</code>
     */
    private static Map<String, FuelTrim> map = new HashMap<>();

    static {
        for (FuelTrim error : FuelTrim.values())
            map.put(error.getValue(), error);
    }

    private final String value;
    private final String bank;

    private FuelTrim(final String value, final String bank) {
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
        return value;
    }

}
