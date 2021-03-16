package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/1/18 0018
 * 描述：排气再循环（EGR）指令
 */
public class EGRCommand extends ObdCommand {
    private int CommandedEGR = 0;

    public EGRCommand() {
        super("01 2C");
    }

    /**
     * Copy ctor
     */
    public EGRCommand(EGRCommand other) {
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
        return AvailableCommandNames.EGR.getValue();
    }
}