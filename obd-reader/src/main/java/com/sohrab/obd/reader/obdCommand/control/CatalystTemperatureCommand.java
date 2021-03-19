package com.sohrab.obd.reader.obdCommand.control;

import android.annotation.SuppressLint;

import com.sohrab.obd.reader.enums.CatalystTrim;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/16 0016
 * 描述：
 */
public class CatalystTemperatureCommand extends ObdCommand {

    private float temp = 0;

    private final CatalystTrim bank;

    /**
     * Default ctor.
     * <p>
     * Will read the bank from parameters and construct the command accordingly.
     * Please, see FuelTrim enum for more details.
     */
    public CatalystTemperatureCommand(String mode,final CatalystTrim bank) {
        super(mode+" "+bank.buildObdCommand());
        this.bank = bank;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        float B = buffer.get(3);
        temp = (((A * 256) + B) / 10) - 40;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("DefaultLocale")
    @Override
    public String getFormattedResult() {
        return getCatalystTemperature() + " ℃";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getCatalystTemperature() + " ℃";
    }

    /**
     * <p>getWidebandAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getCatalystTemperature() {
        return temp;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return bank.getBank();
    }
}