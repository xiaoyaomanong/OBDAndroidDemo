package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/1/26 0026
 * 描述：
 */
public class OdometerCommand extends ObdCommand {
    private double Odometer = 0;

    public OdometerCommand() {
        super("01 A6");
    }

    /**
     * Copy ctor
     */
    public OdometerCommand(OdometerCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        //*Math.pow(a, b)//计算a的b次方
        Odometer = (buffer.get(2) * Math.pow(2, 24) + buffer.get(3) * Math.pow(2, 16) + buffer.get(4) * Math.pow(2, 8) + buffer.get(5)) / 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return Odometer + "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return Odometer + "";
    }

    @Override
    public String getName() {
        return AvailableCommandNames.ODOMETER.getValue();
    }
}