package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SystemOfUnits;

/**
 * Distance traveled since codes cleared-up.
 *
 */
public class DistanceSinceCCCommand extends ObdCommand
        implements SystemOfUnits {

    private int km = 0;

    /**
     * Default ctor.
     */
    public DistanceSinceCCCommand(String mode) {
        super(mode+" 31");
    }

    /**
     * Copy ctor.
     *
     */
    public DistanceSinceCCCommand(DistanceSinceCCCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 31] of the response
        if (buffer.size()!=0) {
            km = buffer.get(2) * 256 + buffer.get(3);
            isHaveData=true;
        }else {
            isHaveData=false;
        }
    }

    /**
     * <p>getFormattedResult.</p>
     *
     * @return a {@link String} object.
     */
    public String getFormattedResult() {
        if (isHaveData) {
            return useImperialUnits ? String.format("%.2f%s", getImperialUnit(), getResultUnit())
                    : String.format("%d%s", km, getResultUnit());
        }else {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return useImperialUnits ? String.valueOf(getImperialUnit()) : String.valueOf(km);
        }else {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return useImperialUnits ? "m" : "km";
    }

    /** {@inheritDoc} */
    @Override
    public float getImperialUnit() {
        return km * 0.621371192F;
    }

    /**
     * <p>Getter for the field <code>km</code>.</p>
     *
     * @return a int.
     */
    public int getKm() {
        return km;
    }

    /**
     * <p>Setter for the field <code>km</code>.</p>
     *
     * @param km a int.
     */
    public void setKm(int km) {
        this.km = km;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.DISTANCE_TRAVELED_AFTER_CODES_CLEARED.getValue();
    }

}
