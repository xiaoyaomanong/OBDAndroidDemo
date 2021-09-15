package com.sohrab.obd.reader.obdCommand.protocol;

import static com.sohrab.obd.reader.obdCommand.Const.NO_DATA;

/**
 * Turns off line-feed.
 *
 * @author pires
 * @version $Id: $Id
 */
public class LineFeedOffCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for LineFeedOffCommand.</p>
     */
    public LineFeedOffCommand() {
        super("AT L0");
    }

    /**
     * <p>Constructor for LineFeedOffCommand.</p>
     *
     */
    public LineFeedOffCommand(LineFeedOffCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        if (getResult().equals(NO_DATA)) {
            return NO_DATA;
        } else {
            return getResult();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Line Feed Off";
    }

}
