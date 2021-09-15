package com.sohrab.obd.reader.obdCommand.pressure;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：
 */
public class FuelRailPressureManifoldVacuumCommand extends PressureCommand {

    /**
     * <p>Constructor for FuelRailPressureCommand.</p>
     */
    public FuelRailPressureManifoldVacuumCommand(String mode) {
        super(mode + " 22");
    }

    /**
     * <p>Constructor for FuelRailPressureCommand.</p>
     */
    public FuelRailPressureManifoldVacuumCommand(FuelRailPressureCommand other) {
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
            return (int) (((a * 256) + b) * 0.079);
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_RAIL_PRESSURE_manifold.getValue();
    }

}
