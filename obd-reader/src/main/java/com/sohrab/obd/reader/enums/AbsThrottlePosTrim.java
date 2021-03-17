package com.sohrab.obd.reader.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Jealous
 * 日期：2021/3/17
 * 描述：
 */

public enum AbsThrottlePosTrim {
    ABS_THROTTLE_POS_B("47", "Absolute throttle position B"),
    ABS_THROTTLE_POS_C("48", "Absolute throttle position C"),
    ACC_PEDAL_POS_D("49", "Accelerator pedal position D"),
    ACC_PEDAL_POS_E("4A", "Accelerator pedal position E"),
    ACC_PEDAL_POS_F("4B", "Accelerator pedal position F"),
    THROTTLE_ACTUATOR("4B", "Commanded throttle actuator");
    /**
     * Constant <code>map</code>
     */
    private static final Map<String, AbsThrottlePosTrim> map = new HashMap<>();

    static {
        for (AbsThrottlePosTrim error : AbsThrottlePosTrim.values())
            map.put(error.getValue(), error);
    }

    private final String value;
    private final String bank;

    AbsThrottlePosTrim(final String value, final String bank) {
        this.value = value;
        this.bank = bank;
    }

    /**
     * <p>fromValue.</p>
     *
     * @param value a int.
     */
    public static AbsThrottlePosTrim fromValue(final int value) {
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
        return  value;
    }
}
