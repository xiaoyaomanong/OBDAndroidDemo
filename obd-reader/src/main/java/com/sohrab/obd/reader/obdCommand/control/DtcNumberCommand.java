package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.FuelTrim;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * This command will for now read MIL (check engine light) state and number of
 * diagnostic trouble codes currently flagged in the ECU.
 * <p>
 * Perhaps in the future we'll extend this to read the 3rd, 4th and 5th bytes of
 * the response in order to store information about the availability and
 * completeness of certain on-board tests.
 */
public class DtcNumberCommand extends ObdCommand {

    private int codeCount = 0;
    private boolean milOn = false;

    /**
     * Default ctor.
     */
    public DtcNumberCommand(String mode) {
        super(mode + " 01");
    }

    /**
     * Copy ctor.
     */
    public DtcNumberCommand(DtcNumberCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size() != 0) {
            final int mil = buffer.get(2);
            milOn = (mil & 0x80) == 128;
            codeCount = mil & 0x7F;
            isHaveData = true;
        } else {
            isHaveData = false;
        }
    }

    /**
     * <p>getFormattedResult.</p>
     *
     * @return a {@link String} object.
     */
    public String getFormattedResult() {
        if (isHaveData) {
            final String res = milOn ? "ON" : "OFF";
            return "异常指示灯（MIL）状态: " + res + ";DTC的个数: " + codeCount;
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
            return String.valueOf(codeCount);
        } else {
            return "";
        }
    }

    /**
     * <p>getTotalAvailableCodes.</p>
     *
     * @return the number of trouble codes currently flaggd in the ECU.
     */
    public int getTotalAvailableCodes() {
        return codeCount;
    }

    /**
     * <p>Getter for the field <code>milOn</code>.</p>
     *
     * @return the state of the check engine light state.
     */
    public boolean getMilOn() {
        return milOn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.DTC_NUMBER.getValue();
    }

}
