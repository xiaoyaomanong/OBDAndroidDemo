package com.sohrab.obd.reader.obdCommand.fuel;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;

/**
 * Get fuel level in percentage
 *
 */
public class FuelLevelCommand extends PercentageObdCommand {

    /**
     * <p>Constructor for FuelLevelCommand.</p>
     */
    public FuelLevelCommand(String mode) {
        super(mode+" 2F");
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size()!=0) {
            percentage = 100.0f * buffer.get(2) / 255.0f;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_LEVEL.getValue();
    }

}
