package com.sohrab.obd.reader.obdCommand.protocol;

import com.sohrab.obd.reader.enums.AvailableCommandNames;

import static com.sohrab.obd.reader.obdCommand.Const.NO_DATA;

/**
 * Describe the current Protocol.
 * If a protocol is chosen and the automatic option is
 * also selected, AT DP will show the word 'AUTO' before
 * the protocol description. Note that the description
 * shows the actual protocol names, not the numbers
 * used by the protocol setting commands.
 *
 * @since 1.0-RC12
 */
public class DescribeProtocolCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for DescribeProtocolCommand.</p>
     */
    public DescribeProtocolCommand() {
        super("AT DP");
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
        return AvailableCommandNames.DESCRIBE_PROTOCOL.getValue();
    }

}
