package com.sohrab.obd.reader.obdCommand.fuel;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.FuelSystemStatus;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：
 */
public class FuelSystemStatusCommand extends ObdCommand {
    private int fuelSystemStatus = 0;

    public FuelSystemStatusCommand(String mode) {
        super(mode+" 03");
    }

    /**
     * <p>Constructor for ConsumptionRateCommand.</p>
     */
    public FuelSystemStatusCommand(FuelSystemStatusCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        fuelSystemStatus = buffer.get(2);
    }

    @Override
    public String getFormattedResult() {
        try {
            return FuelSystemStatus.fromValue(fuelSystemStatus).getDescription();
        } catch (NullPointerException e) {
            return "Ok";
        }
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(fuelSystemStatus);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_SYSTEM_STATUS.getValue();
    }
}