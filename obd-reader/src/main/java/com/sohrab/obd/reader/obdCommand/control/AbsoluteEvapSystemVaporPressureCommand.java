package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class AbsoluteEvapSystemVaporPressureCommand extends ObdCommand {


    private float pressure = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public AbsoluteEvapSystemVaporPressureCommand(String mode) {
        super(mode + " 53");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        if (buffer.size()!=0) {
            float A = buffer.get(2);
            float B = buffer.get(3);
            pressure = (256 * A + B) / 200;
            isHaveData = true;
        }else {
            isHaveData=false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            return getPressure() + " kPa";
        }else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return getPressure() + " kPa";
        }else {
            return "";
        }
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
        return AvailableCommandNames.ABS_EVA_SYSTEM_VAPOR_PRESSURE.getValue();
    }
}
