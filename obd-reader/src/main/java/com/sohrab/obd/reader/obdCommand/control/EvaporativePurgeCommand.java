package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class EvaporativePurgeCommand extends ObdCommand {
    private int Purge = 0;

    public EvaporativePurgeCommand() {
        super("01 2E");
    }

    /**
     * Copy ctor
     */
    public EvaporativePurgeCommand(EvaporativePurgeCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        Purge = (buffer.get(2) * 100) / 255;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return Purge + "%";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return Purge + "%";
    }

    @Override
    public String getName() {
        return AvailableCommandNames.EVAPORAIVE_PURGE.getValue();
    }
}