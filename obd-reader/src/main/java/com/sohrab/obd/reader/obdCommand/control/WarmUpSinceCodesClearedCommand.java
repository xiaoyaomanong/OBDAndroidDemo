package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class WarmUpSinceCodesClearedCommand extends ObdCommand {
    private int Since = 0;

    public WarmUpSinceCodesClearedCommand(final ModeTrim bank) {
        super(bank.buildObdCommand()+" 30");
    }

    /**
     * Copy ctor
     */
    public WarmUpSinceCodesClearedCommand(WarmUpSinceCodesClearedCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        Since = buffer.get(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return Since + " count";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return Since + " count";
    }

    @Override
    public String getName() {
        return AvailableCommandNames.EGR.getValue();
    }
}