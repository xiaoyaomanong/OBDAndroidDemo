package com.sohrab.obd.reader.obdCommand.control;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * 作者：Jealous
 * 日期：2021/3/17 0017
 * 描述：
 */
public class MaxAirFlowMassRateCommand extends ObdCommand {


    private float MaxAirFlowMass = 0;

    /**
     * <p>Constructor for AirFuelRatioCommand.</p>
     */
    public MaxAirFlowMassRateCommand(final ModeTrim modeTrim) {
        super(modeTrim.buildObdCommand()+" 50");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 44] of the response
        float A = buffer.get(2);
        MaxAirFlowMass = 10 * A ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return getMaxAirFlowMass() + " g/s";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return getMaxAirFlowMass()+" g/s";
    }

    /**
     * <p>getAirFuelRatio.</p>
     *
     * @return a double.
     */
    public double getMaxAirFlowMass() {
        return MaxAirFlowMass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.MAX_AIR_FLOW_MASS_RATE.getValue();
    }
}
