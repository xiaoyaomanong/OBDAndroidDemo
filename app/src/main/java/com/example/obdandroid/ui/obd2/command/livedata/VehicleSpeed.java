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

package com.example.obdandroid.ui.obd2.command.livedata;


import com.example.obdandroid.ui.obd2.Response;
import com.example.obdandroid.ui.obd2.Unit;
import com.example.obdandroid.ui.obd2.command.LiveCommand;
import com.example.obdandroid.ui.obd2.response.CalculatedResponse;

/**
 * <p>This class is the OBD-II command for "01 0D" (Service 01, PID 0x0D).</p>
 * <p>Description: Vehicle speed</p>
 * <p>The response:
 * <table border="1">
 * <tr><th>Size</th>     <td>1 byte</td></tr>
 * <tr><th>Unit</th>     <td>km/h</td></tr>
 * <tr><th>Min value</th><td>0</td></tr>
 * <tr><th>Max value</th><td>255</td></tr>
 * <tr><th>Equation</th> <td><pre>A</pre></td></tr>
 * <tr><th>Class</th>    <td>{@link CalculatedResponse}</td></tr>
 * </table></p>
 *
 * @author MacFJA
 * @see <a href="https://en.wikipedia.org/wiki/OBD-II_PIDs#Service_01">Wikipedia</a>
 */
public class VehicleSpeed extends LiveCommand {
    @Override
    public String getCode() {
        return "0D";
    }

    @Override
    public Response getResponse(byte[] rawResult)  {
        return new CalculatedResponse(rawResult, "A") {
            @Override
            public Unit getUnit() {
                return Unit.KilometrePerHour;
            }
        };
    }
}
