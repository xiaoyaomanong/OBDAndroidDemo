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

package com.example.obdandroid.ui.obd2.elm327.command;

import com.example.obdandroid.ui.obd2.Response;
import com.example.obdandroid.ui.obd2.elm327.command.ATCommand;
import com.example.obdandroid.ui.obd2.exception.UnexpectedResponse;
import com.example.obdandroid.ui.obd2.response.RawResponse;

/**
 * <p>This class is the ELM327 command for "AT IGN".</p>
 * <p>Description: Read the IgnMon input level</p>
 * <p>ELM327 version: {@code 1.4}</p>
 * <p>Response: {@code ON} or {@code OFF}</p>
 *
 * @author MacFJA
 * @see <a href="https://www.elmelectronics.com/wp-content/uploads/2017/01/ELM327DS.pdf">ELM327 PDF</a>
 */
public class Ignition extends ATCommand {
    @Override
    protected String getCode() {
        return "IGN";
    }

    @Override
    public Response getResponse(byte[] rawResult) {
        String result = new String(rawResult);

        if (!result.equals("ON") && !result.equals("OFF")) {
            return new UnexpectedResponse(rawResult);
        }

        return new RawResponse(rawResult) {
        };
    }

    @Override
    public String minElmVersion() {
        return "1.4";
    }
}
