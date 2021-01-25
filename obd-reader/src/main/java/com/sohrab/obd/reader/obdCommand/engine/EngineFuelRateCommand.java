package com.sohrab.obd.reader.obdCommand.engine;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/1/25
 * 描述：
 */
public class EngineFuelRateCommand extends ObdCommand {
    private float Rate = 0;

    public EngineFuelRateCommand() {
        super("01 5E");
    }

    /**
     * <p>Constructor for ConsumptionRateCommand.</p>
     */
    public EngineFuelRateCommand(EngineFuelRateCommand other) {
        super(other);
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        // ((A*256)+B)*0.05
        Rate = ((buffer.get(2) * 256) + buffer.get(3)) * 0.05f;

    }

    @Override
    public String getFormattedResult() {
        return Rate + "L/h";
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(Rate);
    }

    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_FUEL_RATE.getValue();
    }
}