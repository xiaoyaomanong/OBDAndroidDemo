package com.sohrab.obd.reader.obdCommand.engine;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

import java.util.Locale;

/**
 * Mass Air Flow (MAF)
 *
 * @author pires
 * @version $Id: $Id
 */
public class MassAirFlowCommand extends ObdCommand {

    private float maf = -1.0f;

    /**
     * Default ctor.
     */
    public MassAirFlowCommand(String mode) {
        super(mode+" 10");
    }

    /**
     * Copy ctor.
     */
    public MassAirFlowCommand(MassAirFlowCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size()!=0) {
            maf = (buffer.get(2) * 256 + buffer.get(3)) / 100.0f;
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
        if (isHaveData) {
            return String.format(Locale.ENGLISH, "%.2f", maf);
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
            return String.valueOf(maf);
        }else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultUnit() {
        return "g/s";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.MAF.getValue();
    }

}

