package com.sohrab.obd.reader.obdCommand.protocol;

import static com.sohrab.obd.reader.obdCommand.Const.NO_DATA;

/**
 * Reset the OBD connection.
 *
 * @author pires
 * @version $Id: $Id
 */
public class ObdResetCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for ObdResetCommand.</p>
     */
    public ObdResetCommand() {
        super("AT Z");
    }

    /**
     * <p>Constructor for ObdResetCommand.</p>
     */
    public ObdResetCommand(ObdResetCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        if (getResult().equals(NO_DATA)) {
            return NO_DATA;
        } else {
            return getResult();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Reset OBD";
    }

}
