package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class TimeSinceTroubleCodesClearedCommand extends ObdCommand {


    private float time = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public TimeSinceTroubleCodesClearedCommand(String mode) {
        super(mode+" 4E");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        float B = buffer.get(3);
        time = 256 * A + B;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getTime() + " 分";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getTime()+" 分";
    }

    /**
     * <p>getAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getTime() {
        return time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.TIME_SINCE_TC_CLEARED.getValue();
    }
}
