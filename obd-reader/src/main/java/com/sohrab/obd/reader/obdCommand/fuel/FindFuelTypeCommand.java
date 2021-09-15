package com.sohrab.obd.reader.obdCommand.fuel;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.FuelType;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * This command is intended to determine the vehicle fuel type.
 *
 * @author pires
 * @version $Id: $Id
 */
public class FindFuelTypeCommand extends ObdCommand {

    private int fuelType = 0;

    /**
     * Default ctor.
     */
    public FindFuelTypeCommand(String mode) {
        super(mode+" 51");
    }

    /**
     * Copy ctor
          *
     */
    public FindFuelTypeCommand(FindFuelTypeCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size()!=0) {
            fuelType = buffer.get(2);
            isHaveData = true;
        }else {
            isHaveData=false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            try {
                return FuelType.fromValue(fuelType).getDescription();
            } catch (NullPointerException e) {
                return "-";
            }
        }else {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return String.valueOf(fuelType);
        }else {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_TYPE.getValue();
    }

}
