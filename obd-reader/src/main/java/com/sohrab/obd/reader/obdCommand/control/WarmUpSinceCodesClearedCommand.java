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

    public WarmUpSinceCodesClearedCommand(String mode) {
        super(mode+" 30");
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
        if (buffer.size()!=0) {
            Since = buffer.get(2);
            isHaveData = true;
        }else {
            isHaveData=false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            return Since + " count";
        }else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return Since + " count";
        }else {
            return "";
        }
    }

    @Override
    public String getName() {
        return AvailableCommandNames.WarmUpSinceCodesClearedCommand.getValue();
    }
}