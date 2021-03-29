package com.sohrab.obd.reader.obdCommand.protocol;

/**
 * 作者：Jealous
 * 日期：2021/3/29 0029
 * 描述：
 */
public class BaseDataCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for EchoOffCommand.</p>
     */
    public BaseDataCommand() {
        super("BT+RPDTC");
    }

    /**
     * <p>Constructor for EchoOffCommand.</p>
     *
     * @param other a {@link com.github.pires.obd.commands.protocol.BaseDataCommand} object.
     */
    public BaseDataCommand(BaseDataCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "base data";
    }

}

