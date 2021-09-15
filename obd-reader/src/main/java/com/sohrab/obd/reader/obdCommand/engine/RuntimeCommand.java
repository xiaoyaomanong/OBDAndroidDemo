package com.sohrab.obd.reader.obdCommand.engine;


import android.util.Log;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

import java.util.Locale;

/**
 * Engine runtime.
 *
 * @author pires
 * @version $Id: $Id
 */
public class RuntimeCommand extends ObdCommand {

    private int value = 0;

    /**
     * Default ctor.
     */
    public RuntimeCommand(String mode) {
        super(mode + " 1F");
    }

    /**
     * Copy ctor.
     */
    public RuntimeCommand(RuntimeCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 0C] of the response
        if (buffer.size() != 0) {
            value = buffer.get(2) * 256 + buffer.get(3);
            isHaveData = true;
        } else {
            isHaveData = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        // determine time
        if (isHaveData) {
            Log.d("RUNTIME VALUE", String.valueOf(value));
            final String hh = String.format(Locale.ENGLISH, "%02d", value / 3600);
            final String mm = String.format(Locale.ENGLISH, "%02d", (value % 3600) / 60);
            final String ss = String.format(Locale.ENGLISH, "%02d", value % 60);
            return String.format(Locale.ENGLISH, "%s:%s:%s", hh, mm, ss);
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return String.valueOf(value);
        }else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultUnit() {
        return "s";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RUNTIME.getValue();
    }
}

