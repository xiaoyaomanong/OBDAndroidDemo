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

    public EvaporativePurgeCommand(String mode) {
        super(mode+" 2E");
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
        if(buffer.size()!=0) {
            Purge = (buffer.get(2) * 100) / 255;
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
            return Purge + "%";
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
            return Purge + "%";
        }else {
            return "";
        }
    }

    @Override
    public String getName() {
        return AvailableCommandNames.EVAPORATE_PURGE.getValue();
    }
}