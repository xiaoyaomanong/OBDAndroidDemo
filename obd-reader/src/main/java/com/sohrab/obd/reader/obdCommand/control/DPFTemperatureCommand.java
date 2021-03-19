package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class DPFTemperatureCommand extends ObdCommand {


    private float temp = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public DPFTemperatureCommand(String mode) {
        super(mode + " 7C");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        float B = buffer.get(3);
        temp = ((256 * A + B) / 10) - 40;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getTemp() + " ℃";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getTemp() + " ℃";
    }

    /**
     * <p>getAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getTemp() {
        return temp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.DPF_TEMP.getValue();
    }
}