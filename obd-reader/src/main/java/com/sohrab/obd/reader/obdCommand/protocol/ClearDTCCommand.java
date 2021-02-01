package com.sohrab.obd.reader.obdCommand.protocol;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/2/1 0001
 * 描述：
 */
public class ClearDTCCommand extends ObdCommand {

    protected String result = null;


    public ClearDTCCommand() {
        super("0A");
    }

    /**
     * Copy ctor
     */
    public ClearDTCCommand(ClearDTCCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        result = getResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return result;
    }

    @Override
    public String getName() {
        return AvailableCommandNames.CLEAR_TROUBLE_CODE.getValue();
    }
}