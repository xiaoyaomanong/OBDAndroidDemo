package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class FuelRailAbsPressureCommand extends ObdCommand {


    private float pressure = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public FuelRailAbsPressureCommand(final ModeTrim modeTrim) {
        super(modeTrim.buildObdCommand() + " 59");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        float B = buffer.get(3);
        pressure = 10 * (256 * A + B);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getPressure() + " kPa";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getPressure() + " kPa";
    }

    /**
     * <p>getAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_RAIL_ABS_PRESSURE.getValue();
    }
}
