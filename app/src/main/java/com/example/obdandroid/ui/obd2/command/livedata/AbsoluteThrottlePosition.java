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

import com.example.obdandroid.ui.obd2.Command;
import com.example.obdandroid.ui.obd2.Response;
import com.example.obdandroid.ui.obd2.response.PercentResponse;

/**
 * <p>This class is the OBD-II command for multiple commands: "01 47" to "01 4B".</p>
 * <p>Description: <ul>
 * <li>Absolute throttle position B</li>
 * <li>Absolute throttle position C</li>
 * <li>Absolute throttle position D</li>
 * <li>Absolute throttle position E</li>
 * <li>Absolute throttle position F</li>
 * </ul></p>
 * <p>The response:
 * <table border="1">
 * <tr><th>Size</th>     <td>1 byte</td></tr>
 * <tr><th>Unit</th>     <td>%</td></tr>
 * <tr><th>Min value</th><td>0</td></tr>
 * <tr><th>Max value</th><td>100</td></tr>
 * <tr><th>Equation</th> <td><pre>
 * 100
 * --- * A
 * 255
 *     </pre></td></tr>
 * <tr><th>Class</th>    <td>{@link PercentResponse}</td></tr>
 * </table></p>
 *
 * @author MacFJA
 * @see <a href="https://en.wikipedia.org/wiki/OBD-II_PIDs#Service_01">Wikipedia</a>
 */
public enum AbsoluteThrottlePosition implements Command {
    ThrottleB("47"),
    ThrottleC("48"),
    ThrottleD("49"),
    ThrottleE("4A"),
    ThrottleF("4B");

    private final String code;

    AbsoluteThrottlePosition(String code) {
        this.code = code;
    }

    @Override
    public String getRequest() {
        return "01 " + code;
    }

    @Override
    public Response getResponse(byte[] rawResult) {
        return new PercentResponse(rawResult);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "." + super.toString();
    }
}
