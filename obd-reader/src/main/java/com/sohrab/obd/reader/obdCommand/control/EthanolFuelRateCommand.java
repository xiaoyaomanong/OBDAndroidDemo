package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class EthanolFuelRateCommand extends ObdCommand {


    private float rate = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public EthanolFuelRateCommand(String mode) {
        super(mode + " 52");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        if (buffer.size() != 0) {
            float A = buffer.get(2);
            rate = (100 * A) / 255;
            isHaveData = true;
        } else {
            isHaveData = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            return getRate() + " %";
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
            return getRate() + " %";
        } else {
            return "";
        }
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
        return AvailableCommandNames.ETHANOL_FUEL_RATE.getValue();
    }
}
