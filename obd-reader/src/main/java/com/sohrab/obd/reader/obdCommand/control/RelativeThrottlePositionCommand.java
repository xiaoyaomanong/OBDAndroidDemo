package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class RelativeThrottlePositionCommand extends ObdCommand {


    private float RelativeThrottlePosition = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public RelativeThrottlePositionCommand(final ModeTrim modeTrim) {
        super(modeTrim.buildObdCommand()+" 45");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        float B = buffer.get(3);
        RelativeThrottlePosition = (100 * A) / 255;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getThrottlePosition() + " %";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getThrottlePosition() + " %";
    }

    /**
     * <p>getAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getThrottlePosition() {
        return RelativeThrottlePosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.REL_THROTTLE_POS.getValue();
    }
}
