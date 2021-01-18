package com.sohrab.obd.reader.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述： 燃油系统状态
 */
public enum FuelSystemStatus {
    motor_OFF(0x00, "马达关了"),
    insufficient_engine_temperature(0x01, "因为发动机温度不足而开路"),
    fuel_mix(0x02, "闭回路，利用氧气侦测器回授来决定fuel mix"),
    OR_fue(0x04, "因为发动机负载或是因减速燃料减少而开路"),
    system_failure(0x08, "因为系统失效而开路"),
    feedback_system(0x10, "闭回路，利用至少一个氧气侦测器，但回授系统出现故障");
    /** Constant <code>map</code> */
    private static Map<Integer, FuelSystemStatus> map = new HashMap<>();

    static {
        for (FuelSystemStatus error : FuelSystemStatus.values())
            map.put(error.getValue(), error);
    }

    private final int value;
    private final String description;

    private FuelSystemStatus(final int value, final String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * <p>fromValue.</p>
     *
     * @param value a int.     *
     */
    public static FuelSystemStatus fromValue(final int value) {
        return map.get(value);
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a int.
     */
    public int getValue() {
        return value;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }
}