package com.sohrab.obd.reader.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public enum ModeTrim {

    MODE_01("01", "Show current data"),
    MODE_02("02", "Show freeze frame data"),
    MODE_03("03", "Show stored Diagnostic Trouble Codes"),
    MODE_04("04", "Clear Diagnostic Trouble Codes and stored values"),
    MODE_05("05", "Test results, oxygen sensor monitoring (non CAN only)"),
    MODE_06("06", "Test results, other component/system monitoring (Test results, oxygen sensor monitoring for CAN only"),
    MODE_07("07", "Show pending Diagnostic Trouble Codes (detected during current or last driving cycle)"),
    MODE_08("08", "Control operation of on-board component/system"),
    MODE_09("09", "Request vehicle information"),
    MODE_0A("0A", "Permanent Diagnostic Trouble Codes (DTCs) (Cleared DTCs)");

    /**
     * Constant <code>map</code>
     */
    private static final Map<String, ModeTrim> map = new HashMap<>();

    static {
        for (ModeTrim error : ModeTrim.values())
            map.put(error.getValue(), error);
    }

    private final String value;
    private final String bank;

    ModeTrim(final String value, final String bank) {
        this.value = value;
        this.bank = bank;
    }

    /**
     * <p>fromValue.</p>
     *
     * @param value a int.
     */
    public static ModeTrim fromValue(final int value) {
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
    public final String build() {
        return value;
    }
}