package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class SystemVaporPressureCommand extends ObdCommand {
    private int SystemVaporPressure = 0;

    public SystemVaporPressureCommand(String mode) {
        super(mode+" 32");
    }

    /**
     * Copy ctor
     */
    public SystemVaporPressureCommand(SystemVaporPressureCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size()!=0) {
            int a = buffer.get(2);
            int b = buffer.get(3);
            SystemVaporPressure = (a * 256 + b) / 4;
            isHaveData=false;
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
            return SystemVaporPressure + "Pa";
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
            return SystemVaporPressure + "Pa";
        }else {
            return "";
        }
    }

    @Override
    public String getName() {
        return AvailableCommandNames.SYSTEM_VAPOR_PRESSURE.getValue();
    }
}