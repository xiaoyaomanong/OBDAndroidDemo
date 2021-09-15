package com.sohrab.obd.reader.obdCommand.fuel;

import android.annotation.SuppressLint;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * Created by "Sohrab" on 1/8/2018.
 */

public class AirFuelRatioCommand extends ObdCommand {


    private float afr = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public AirFuelRatioCommand(String mode) {
        super(mode+" 44");
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
            afr = (((A * 256) + B) / 32768) * 14.7f;//((A*256)+B)/32768
            isHaveData = true;
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
            return String.format("%.2f", getAirFuelRatio()) + ":1 AFR";
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
            return String.valueOf(getAirFuelRatio());
        }else {
            return "";
        }
    }

    /**
     * <p>getAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getAirFuelRatio() {
        return afr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.AIR_FUEL_RATIO.getValue();
    }
}
