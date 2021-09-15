package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class EGRErrorCommand extends ObdCommand {
    private int EGRError = 0;

    public EGRErrorCommand(String mode) {
        super(mode+" 2D");
    }

    /**
     * Copy ctor
     */
    public EGRErrorCommand(EGRErrorCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response

        if (buffer.size()!=0) {
            EGRError = ((buffer.get(2) - 128) * 100) / 128;
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
        if (isHaveData){
            return EGRError + "%";
        }else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        if (isHaveData){
            return EGRError + "%";
        }else {
            return "";
        }
    }

    @Override
    public String getName() {
        return AvailableCommandNames.EGR_ERROR.getValue();
    }
}