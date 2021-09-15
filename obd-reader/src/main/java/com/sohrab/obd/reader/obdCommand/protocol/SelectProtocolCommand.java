package com.sohrab.obd.reader.obdCommand.protocol;


import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.obdCommand.protocol.ObdProtocolCommand;

import static com.sohrab.obd.reader.obdCommand.Const.NO_DATA;

/**
 * Select the protocol to use.
 *
 * @author pires
 * @version $Id: $Id
 */
public class SelectProtocolCommand extends ObdProtocolCommand {

    private final ObdProtocols protocol;

    /**
     * <p>Constructor for SelectProtocolCommand.</p>
     *
     *
     */
    public SelectProtocolCommand(final ObdProtocols protocol) {
        super("AT SP " + protocol.getValue());
        this.protocol = protocol;
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
        return "Select Protocol " + protocol.name();
    }

}
