package com.sohrab.obd.reader.obdCommand.protocol;

import static com.sohrab.obd.reader.obdCommand.Const.NO_DATA;

/**
 * Turn-off spaces.
 */
public class SpacesOffCommand extends ObdProtocolCommand {

    public SpacesOffCommand() {
        super("ATS0");
    }

    /**
     * <p>Constructor for SpacesOffCommand.</p>
     *
     * @param other a {@link SpacesOffCommand} object.
     */
    public SpacesOffCommand(SpacesOffCommand other) {
        super(other);
    }

    @Override
    public String getFormattedResult() {
        if (getResult().equals(NO_DATA)) {
            return NO_DATA;
        } else {
            return getResult();
        }
    }

    @Override
    public String getName() {
        return "Spaces Off";
    }
}
