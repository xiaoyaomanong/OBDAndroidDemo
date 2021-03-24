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


/**
 * <p>This class is the response of a {@link io.github.macfja.obd2.command.oxygen_monitor.SupportedOxygenSensorMonitorCommand} command.</p>
 *
 * @author MacFJA
 */
public class AvailableOxygenSensorMonitorResponse extends BinaryResponse {
    public AvailableOxygenSensorMonitorResponse(byte[] rawResult) {
        super(rawResult);
    }

    public boolean isRichToLeanSupported(int bank, int sensor) {
        if (bank < 1 || bank > 4 || sensor < 1 || sensor > 4) {
            throw new IndexOutOfBoundsException();
        }

        int position = (bank - 1) * 4 + sensor - 1;

        return isOn(position);
    }

    public boolean isLeanToRichSupported(int bank, int sensor) {
        if (bank < 1 || bank > 4 || sensor < 1 || sensor > 4) {
            throw new IndexOutOfBoundsException();
        }

        int position = (bank - 1) * 4 + sensor - 1 + 16;

        return isOn(position);
    }
}
