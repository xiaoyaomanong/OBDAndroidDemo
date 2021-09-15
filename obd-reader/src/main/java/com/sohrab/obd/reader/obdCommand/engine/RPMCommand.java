package com.sohrab.obd.reader.obdCommand.engine;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

import java.util.Locale;

/**
 * Displays the current engine revolutions per minute (RPM).
 *
 * @author pires
 * @version $Id: $Id
 */
public class RPMCommand extends ObdCommand {

    private int rpm = -1;

    /**
     * Default ctor.
     */
    public RPMCommand(String mode) {
        super(mode + " 0C");
    }

    /**
     * Copy ctor.
     */
    public RPMCommand(RPMCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [41 0C] of the response((A*256)+B)/4
        if (buffer.size() != 0) {
            rpm = (buffer.get(2) * 256 + buffer.get(3)) / 4;
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
        if (isHaveData) {
            return String.format(Locale.ENGLISH, "%d%s", rpm, getResultUnit());
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
            return String.valueOf(rpm);
        }else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultUnit() {
        return "RPM";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RPM.getValue();
    }
}

