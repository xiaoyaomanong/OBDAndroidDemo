package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class DriverDemandEnginePercentTorqueCommand extends ObdCommand {


    private float rate = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public DriverDemandEnginePercentTorqueCommand(String mode) {
        super(mode + " 61");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        rate = A - 125;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getRate() + " %";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getRate() + " %";
    }

    /**
     * <p>getAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getRate() {
        return rate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.DRIVER_ENGINE_TORQUE.getValue();
    }
}