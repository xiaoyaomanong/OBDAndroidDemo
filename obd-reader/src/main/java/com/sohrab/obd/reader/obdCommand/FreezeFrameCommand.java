package com.sohrab.obd.reader.obdCommand;

import com.sohrab.obd.reader.enums.AvailableCommandNames;

/**
 * 作者：Jealous
 * 日期：2021/3/16
 * 描述：
 */
public class FreezeFrameCommand extends ObdCommand {

    private int codeCount = 0;
    private boolean milOn = false;

    /**
     * Default ctor.
     */
    public FreezeFrameCommand() {
        super("02 02");
    }

    /**
     * Copy ctor.
     */
    public FreezeFrameCommand(FreezeFrameCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
      /*  final int mil = buffer.get(2);
        milOn = (mil & 0x80) == 128;
        codeCount = mil & 0x7F;*/
    }

    /**
     * <p>getFormattedResult.</p>
     *
     * @return a {@link String} object.
     */
    public String getFormattedResult() {
        return buffer + "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(buffer);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.DTC_NUMBER.getValue();
    }
}