package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * <p>ModuleVoltageCommand class.</p>
 *
 */
public class ModuleVoltageCommand extends ObdCommand {

    // Equivalent ratio (V)
    private double voltage = 0.00;

    /**
     * Default ctor.
     */
    public ModuleVoltageCommand(String mode) {
        super(mode+" 42");
    }

    /**
     * Copy ctor.
     *
     */
    public ModuleVoltageCommand(ModuleVoltageCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size()!=0) {
            int a = buffer.get(2);
            int b = buffer.get(3);
            voltage = (a * 256 + b) / 1000;
            isHaveData = true;
        }else {
            isHaveData=false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            return String.format("%.1f%s", voltage, getResultUnit());
        }else {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "V";
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return String.valueOf(voltage);
        }else {
            return "";
        }
    }

    /**
     * <p>Getter for the field <code>voltage</code>.</p>
     *
     * @return a double.
     */
    public double getVoltage() {
        if (isHaveData) {
            return voltage;
        }else {
            return 0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.CONTROL_MODULE_VOLTAGE.getValue();
    }

}
