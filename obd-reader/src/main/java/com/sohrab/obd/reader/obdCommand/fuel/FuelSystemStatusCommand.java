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
        super(mode + " 03");
    }

    /**
     * <p>Constructor for ConsumptionRateCommand.</p>
     */
    public FuelSystemStatusCommand(FuelSystemStatusCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        if (buffer.size() != 0) {
            fuelSystemStatus = buffer.get(2);
            isHaveData = true;
        } else {
            isHaveData = false;
        }
    }

    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            try {
                return FuelSystemStatus.fromValue(fuelSystemStatus).getDescription();
            } catch (NullPointerException e) {
                return "Ok";
            }
        } else {
            return "";
        }
    }

    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return String.valueOf(fuelSystemStatus);
        } else {
            return "";
        }
    }

    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_SYSTEM_STATUS.getValue();
    }
}