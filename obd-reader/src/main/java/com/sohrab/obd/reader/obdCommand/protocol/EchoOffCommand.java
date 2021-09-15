package com.sohrab.obd.reader.obdCommand.protocol;


import static com.sohrab.obd.reader.obdCommand.Const.NO_DATA;

/**
 * Turn-off echo.
 *
 * @author pires
 * @version $Id: $Id
 */
public class EchoOffCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for EchoOffCommand.</p>
     */
    public EchoOffCommand() {
        super("AT E0");
    }

    /**
     * <p>Constructor for EchoOffCommand.</p>
     *
     */
    public EchoOffCommand(EchoOffCommand other) {
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
        return "Echo Off";
    }

}

