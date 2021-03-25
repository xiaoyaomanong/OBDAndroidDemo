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

import android.util.Log;

import com.example.obdandroid.config.TAG;
import com.example.obdandroid.ui.obd2.TroubleCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>This class is the response of a {@link com.example.obdandroid.ui.obd2.command.PendingDTCsCommand} command
 * or a {@link com.example.obdandroid.ui.obd2.command.DTCsCommand} command.</p>
 *
 * @author MacFJA
 */
public class MultipleDiagnosticTroubleCodeResponse extends RawResponse {
    public MultipleDiagnosticTroubleCodeResponse(byte[] rawResult) {
        super(rawResult);
    }

    @Override
    public String getFormattedString() {
        return Arrays.toString(getTroubleCodes().toArray());
    }

    public List<TroubleCode> getTroubleCodes() {
        List<TroubleCode> troubleCodes = new ArrayList<>();
        troubleCodes.clear();
        String rawResponse = new String(getRawResult());
        for (int index = 0; index < Math.ceil(rawResponse.length() / 4); index++) {
            String code = rawResponse.substring(index * 4, Math.min((index + 1) * 4, rawResponse.length()));
            Log.e(TAG.TAG_Activity,"code------"+code);
            if (code.contains(":")) {
                code = code.replaceAll(":", "");//xxx43yy{codes}
            }
            if (code.length() >= 4) {
                troubleCodes.add(TroubleCode.createFromHex(code));
            }
            if (!code.equals("0000")) {
                troubleCodes.add(TroubleCode.createFromHex(code));
            }
        }

        return troubleCodes;
    }
}
