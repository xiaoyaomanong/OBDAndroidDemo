package com.sohrab.obd.reader.obdCommand.pressure;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;

/**
 * <p>FuelPressureCommand class.</p>
 *
 */
public class FuelPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for FuelPressureCommand.</p>
     */
    public FuelPressureCommand(String mode) {
        super(mode+" 0A");
    }

    /**
     * <p>Constructor for FuelPressureCommand.</p>
     *
     */
    public FuelPressureCommand(FuelPressureCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     * <p>
     * TODO describe of why we multiply by 3
     */
    @Override
    protected final int preparePressureValue() {
        return buffer.get(2) * 3;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_PRESSURE.getValue();
    }

}
