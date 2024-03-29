package com.sohrab.obd.reader.obdCommand.control;

import android.annotation.SuppressLint;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SystemOfUnits;

/**
 * <p>DistanceMILOnCommand class.</p>
 *
 */
public class DistanceMILOnCommand extends ObdCommand
        implements SystemOfUnits {

    private int km = 0;

    /**
     * Default ctor.
     */
    public DistanceMILOnCommand(String mode) {
        super(mode+" 21");
    }

    /**
     * Copy ctor.
     *
     */
    public DistanceMILOnCommand(
            DistanceMILOnCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 31] of the response
        if (buffer.size()!=0) {
            km = buffer.get(2) * 256 + buffer.get(3);
            isHaveData=true;
        }else{
            isHaveData=false;
        }
    }

    /**
     * <p>getFormattedResult.</p>
     *
     * @return a {@link String} object.
     */
    @SuppressLint("DefaultLocale")
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

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.DISTANCE_TRAVELED_MIL_ON.getValue();
    }
}
