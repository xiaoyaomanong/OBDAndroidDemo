package com.sohrab.obd.reader.obdCommand.fuel;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * Fuel Consumption Rate per hour.
 *
 */
public class ConsumptionRateCommand extends ObdCommand {

    private float fuelRate = -1.0f;

    /**
     * <p>Constructor for ConsumptionRateCommand.</p>
     */
    public ConsumptionRateCommand(String mode) {
        super(mode+" 5E");
    }

    /**
     * <p>Constructor for ConsumptionRateCommand.</p>
     *
     */
    public ConsumptionRateCommand(ConsumptionRateCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size()!=0) {
            fuelRate = (buffer.get(2) * 256 + buffer.get(3)) * 0.05f;
            isHaveData=true;
        }else {
            isHaveData=false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            return String.format("%.1f%s", fuelRate, getResultUnit());
        }else {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return String.valueOf(fuelRate);
        }else {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "L/h";
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_CONSUMPTION_RATE.getValue();
    }
}
