package com.sohrab.obd.reader.obdCommand.fuel;

import android.annotation.SuppressLint;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class WidebandAirFuelRatioEightCommand extends ObdCommand {

    private float wafr = 0;
    private float current = 0;

    /**
     * <p>Constructor for WidebandAirFuelRatioCommand.</p>
     */
    public WidebandAirFuelRatioEightCommand(String mode) {
        super(mode+" 3B");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        if (buffer.size()!=0){
            float A = buffer.get(2);
            float B = buffer.get(3);
            float C = buffer.get(4);
            float D = buffer.get(5);
            current = ((256 * C + D) / 256) - 128;//(256*C+D)/256-128
            wafr = (((A * 256) + B) / 32768) * 14.7f;//((A*256)+B)/32768
            isHaveData=true;
        }else {
            isHaveData=false;
        }

    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("DefaultLocale")
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            return String.format("%.2f", getWidebandAirFuelRatio()) + ":1 AFR" + "," + getElectricCurrent() + " mA";
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
            return getWidebandAirFuelRatio() + "," + getElectricCurrent();
        }else {
            return "";
        }
    }

    /**
     * <p>getWidebandAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getWidebandAirFuelRatio() {
        return wafr;
    }

    public double getElectricCurrent() {
        return current;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.WIDE_BAND_AIR_FUEL_RATIO_8.getValue();
    }
}
