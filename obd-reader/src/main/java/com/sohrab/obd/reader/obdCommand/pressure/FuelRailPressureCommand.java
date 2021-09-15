package com.sohrab.obd.reader.obdCommand.pressure;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;

/**
 * <p>FuelRailPressureCommand class.</p>
 */
public class FuelRailPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for FuelRailPressureCommand.</p>
     */
    public FuelRailPressureCommand(String mode) {
        super(mode + " 23");
    }

    /**
     * <p>Constructor for FuelRailPressureCommand.</p>
     */
    public FuelRailPressureCommand(FuelRailPressureCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     * <p>
     * TODO describe of why we multiply by 3
     */
    @Override
    protected final int preparePressureValue() {
        if (buffer.size() != 0) {
            int a = buffer.get(2);
            int b = buffer.get(3);
            return ((a * 256) + b) * 10;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_RAIL_PRESSURE.getValue();
    }

}
