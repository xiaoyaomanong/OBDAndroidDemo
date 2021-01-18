package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.FuelType;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FindFuelTypeCommand;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：排气再循环（EGR）指令
 */
public class CommandedEGRCommand extends ObdCommand {
    private int CommandedEGR = 0;

    public CommandedEGRCommand() {
        super("01 2C");
    }

    /**
     * Copy ctor
     */
    public CommandedEGRCommand(CommandedEGRCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        CommandedEGR = (buffer.get(2) * 100) / 255;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return CommandedEGR + "%";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return CommandedEGR + "%";
    }

    @Override
    public String getName() {
        return AvailableCommandNames.Commanded_EGR.getValue();
    }
}