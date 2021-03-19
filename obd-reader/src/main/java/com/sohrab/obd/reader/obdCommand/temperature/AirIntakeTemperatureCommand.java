package com.sohrab.obd.reader.obdCommand.temperature;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;

/**
 * Temperature of intake air.
 *
 * @author pires
 * @version $Id: $Id
 */
public class AirIntakeTemperatureCommand extends TemperatureCommand {

    /**
     * <p>Constructor for AirIntakeTemperatureCommand.</p>
     */
    public AirIntakeTemperatureCommand(String mode) {
        super(mode+" 0F");
    }

    /**
     * <p>Constructor for AirIntakeTemperatureCommand.</p>
     *     *
     */
    public AirIntakeTemperatureCommand(AirIntakeTemperatureCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.AIR_INTAKE_TEMP.getValue();
    }


}
