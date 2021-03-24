/*
  Copyright (c) 2018 MacFJA

  Permission is hereby granted, free of charge,
  to any person obtaining a copy of this software and associated documentation files (the "Software"),
  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge,
  publish, distribute, sublicense, and/or sell copies of the Software,
  and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
  Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.example.obdandroid.ui.obd2.response;

import com.example.obdandroid.ui.obd2.Unit;

import javax.script.ScriptException;

/**
 * <p>This class is the response of a {@link com.example.obdandroid.ui.obd2.command.livedata.MaximumValues} command.</p>
 *
 * @author MacFJA
 */
public class MaximumValuesResponse extends RawResponse {
    public MaximumValuesResponse(byte[] rawResult) {
        super(rawResult);
    }

    @Override
    public String getFormattedString() {
        try {
            return getFormattedFuelAirEquivalenceRatio() + " / " +
                    getFormattedOxygenSensorVoltage() + " / " +
                    getFormattedOxygenSensorCurrent() + " / " +
                    getFormattedIntakeManifoldAbsolutePressure();
        } catch (ScriptException e) {
            return "ERROR";
        }
    }

    public String getFormattedFuelAirEquivalenceRatio() throws ScriptException {
        return getCalculatedFuelAirEquivalenceRatio() + Unit.NoUnit.getSymbol();
    }

    public Number getCalculatedFuelAirEquivalenceRatio() throws ScriptException {
        return CalculatedResponse.calculateFromEquation(getRawResult(), "A");
    }

    public String getFormattedOxygenSensorVoltage() throws ScriptException {
        return getCalculatedOxygenSensorVoltage() + Unit.Volt.getSymbol();
    }

    public Number getCalculatedOxygenSensorVoltage() throws ScriptException {
        return CalculatedResponse.calculateFromEquation(getRawResult(), "B");
    }

    public String getFormattedOxygenSensorCurrent() throws ScriptException {
        return getCalculatedOxygenSensorCurrent() + Unit.Milliampere.getSymbol();
    }

    public Number getCalculatedOxygenSensorCurrent() throws ScriptException {
        return CalculatedResponse.calculateFromEquation(getRawResult(), "C");
    }

    public String getFormattedIntakeManifoldAbsolutePressure() throws ScriptException {
        return getCalculatedIntakeManifoldAbsolutePressure() + Unit.KiloPascal.getSymbol();
    }

    public Number getCalculatedIntakeManifoldAbsolutePressure() throws ScriptException {
        return CalculatedResponse.calculateFromEquation(getRawResult(), "D * 10");
    }
}
