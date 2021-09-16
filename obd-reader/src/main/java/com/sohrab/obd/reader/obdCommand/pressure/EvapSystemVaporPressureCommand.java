package com.sohrab.obd.reader.obdCommand.pressure;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class EvapSystemVaporPressureCommand extends ObdCommand {


    private float pressure = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public EvapSystemVaporPressureCommand(String mode) {
        super(mode + " 54");
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
            pressure = (256 * A + B) - 32767;
            isHaveData=true;
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
            return getPressure() + " Pa";
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
            return getPressure() + " Pa";
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
        return AvailableCommandNames.EVA_SYSTEM_VAPOR_PRESSURE.getValue();
    }
}
