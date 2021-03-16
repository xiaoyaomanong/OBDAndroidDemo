package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AbsThrottleposTrim;
import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.CatalystTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class AbsoluteThrottlePositionCommand extends ObdCommand {


    private float Position = 0;

    private final AbsThrottleposTrim bank;

    /**
     * Default ctor.
     * <p>
     * Will read the bank from parameters and construct the command accordingly.
     * Please, see FuelTrim enum for more details.
     */
    public AbsoluteThrottlePositionCommand(final AbsThrottleposTrim bank) {
        super(bank.buildObdCommand());
        this.bank = bank;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        Position = (100 * A) / 255;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getThrottlePosition() + " %";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getThrottlePosition() + " %";
    }

    /**
     * <p>getAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getThrottlePosition() {
        return Position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return bank.getBank();
    }
}