package com.sohrab.obd.reader.obdCommand.temperature;


import android.annotation.SuppressLint;

import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SystemOfUnits;

/**
 * Abstract temperature command.
 *
 * @author pires
 * @version $Id: $Id
 */
public abstract class TemperatureCommand extends ObdCommand implements
        SystemOfUnits {

    private float temperature = 0.0f;

    /**
     * Default ctor.
     *
     * @param cmd a {@link String} object.
     */
    public TemperatureCommand(String cmd) {
        super(cmd);
    }

    /**
     * Copy ctor.
     */
    public TemperatureCommand(TemperatureCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size() != 0) {
            temperature = buffer.get(2) - 40;
            isHaveData = true;
        } else {
            isHaveData = false;
        }
    }


    /**
     * {@inheritDoc}
     * <p>
     * Get values from 'buff', since we can't rely on char/string for
     * calculations.
     */
    @SuppressLint("DefaultLocale")
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            return useImperialUnits ? String.format("%.1f%s", getImperialUnit(), getResultUnit())
                    : String.format("%.0f%s", temperature, getResultUnit());
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return useImperialUnits ? String.valueOf(getImperialUnit()) : String.valueOf(temperature);
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultUnit() {
        return useImperialUnits ? "F" : "℃";
    }

    /**
     * <p>Getter for the field <code>temperature</code>.</p>
     *
     * @return the temperature in Celsius.
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * <p>getImperialUnit.</p>
     *
     * @return the temperature in Fahrenheit.
     */
    public float getImperialUnit() {
        return temperature * 1.8f + 32;
    }

    /**
     * <p>getKelvin.</p>
     *
     * @return the temperature in Kelvin.
     */
    public float getKelvin() {
        return temperature + 273.15f;
    }

    /**
     * <p>getName.</p>
     *
     * @return the OBD command name.
     */
    public abstract String getName();

}
