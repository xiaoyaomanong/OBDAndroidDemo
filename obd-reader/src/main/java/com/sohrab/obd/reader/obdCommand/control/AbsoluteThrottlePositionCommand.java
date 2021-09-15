package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AbsThrottlePosTrim;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class AbsoluteThrottlePositionCommand extends ObdCommand {


    private float Position = 0;

    private final AbsThrottlePosTrim bank;

    /**
     * Default ctor.
     * <p>
     * Will read the bank from parameters and construct the command accordingly.
     * Please, see FuelTrim enum for more details.
     */
    public AbsoluteThrottlePositionCommand(String mode,final AbsThrottlePosTrim bank) {
        super(mode+" "+bank.buildObdCommand());
        this.bank = bank;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        if (buffer.size()!=0) {
            float A = buffer.get(2);
            Position = (100 * A) / 255;
            isHaveData=true;
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
            return getThrottlePosition() + " %";
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
            return getThrottlePosition() + " %";
        }else {
            return "";
        }
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
