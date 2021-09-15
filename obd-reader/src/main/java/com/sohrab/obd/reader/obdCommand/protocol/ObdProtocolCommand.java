package com.sohrab.obd.reader.obdCommand.protocol;


import com.sohrab.obd.reader.obdCommand.ObdCommand;

import static com.sohrab.obd.reader.obdCommand.Const.NO_DATA;

/**
 * <p>Abstract ObdProtocolCommand class.</p>
 */
public abstract class ObdProtocolCommand extends ObdCommand {
    /**
     * Default ctor to use
     *
     * @param command the command to send
     */
    public ObdProtocolCommand(String command) {
        super(command);
    }

    /**
     * Copy ctor.
     *
     * @param other the ObdCommand to copy.
     */
    public ObdProtocolCommand(ObdProtocolCommand other) {
        this(other.cmd);
    }

    /**
     * <p>performCalculations.</p>
     */
    protected void performCalculations() {
        // ignore
    }

    /**
     * <p>fillBuffer.</p>
     */
    protected void fillBuffer() {
        // settings commands don't return a value appropriate to place into the
        // buffer, so do nothing
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        if (getResult().equals(NO_DATA)) {
            return NO_DATA;
        } else {
            return String.valueOf(getResult());
        }
    }
}
