package com.sohrab.obd.reader.obdCommand.control;

import android.annotation.SuppressLint;

import com.sohrab.obd.reader.enums.CatalystTrim;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.enums.OxygenSensorTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class OxygenSensorTrimCommand extends ObdCommand {

    private float bankA = 0;
    private float bankB = 0;

    private final OxygenSensorTrim bank;

    /**
     * Default ctor.
     * <p>
     * Will read the bank from parameters and construct the command accordingly.
     * Please, see FuelTrim enum for more details.
     */
    public OxygenSensorTrimCommand(final ModeTrim modeTrim, final OxygenSensorTrim bank) {
        super(modeTrim.buildObdCommand() + " " + bank.buildObdCommand());
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
        bankA = ((100 * A) / 128) - 100;
        bankB = ((100 * B) / 128) - 100;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("DefaultLocale")
    @Override
    public String getFormattedResult() {
        return getBankA() + "% , " + getBankB() + "%";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getBankA() + "% , " + getBankB() + "%";
    }

    public float getBankA() {
        return bankA;
    }

    public float getBankB() {
        return bankB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return bank.getBank();
    }
}