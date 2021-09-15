package com.sohrab.obd.reader.obdCommand.engine;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.fuel.PercentageObdCommand;

/**
 * <p>AbsoluteLoadCommand class.</p>
 *
 */
public class AbsoluteLoadCommand extends PercentageObdCommand {

    /**
     * Default ctor.
     */
    public AbsoluteLoadCommand(String mode) {
        super(mode+" 43");
    }

    /**
     * Copy ctor.
     *
     */
    public AbsoluteLoadCommand(AbsoluteLoadCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        if (buffer.size()!=0) {
            int a = buffer.get(2);
            int b = buffer.get(3);
            percentage = (a * 256 + b) * 100 / 255;
            isHaveData=true;
        }else {
            isHaveData=false;
        }
    }

    /**
     * <p>getRatio.</p>
     *
     * @return a double.
     */
    public double getRatio() {
        if (isHaveData) {
            return percentage;
        }else {
            return 0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ABS_LOAD.getValue();
    }

}
