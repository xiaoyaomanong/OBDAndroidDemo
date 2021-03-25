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


import com.example.obdandroid.ui.obd2.Response;
import com.sohrab.obd.reader.obdCommand.obdException.NonNumericResponseException;
import com.sohrab.obd.reader.utils.LogUtils;

import java.util.ArrayList;


/**
 * @author MacFJA
 */
public abstract class CalculatedResponse implements Response {
    private byte[] raw;
    private Number calculated;
    private static ArrayList<Integer> buffer = null;

    public CalculatedResponse(byte[] raw, Number calculated) {
        this.raw = raw;
        this.calculated = calculated;
        this.buffer = new ArrayList<>();
    }

    public CalculatedResponse(byte[] raw, String equation) {
        this(raw, calculateFromEquation(raw, equation));
    }

    public static int getGroupCount(byte[] rawResult) {
        String hexValue = new String(rawResult);
        return hexValue.length() / 2;
    }

    public static int getIntValue(byte[] rawResult, int ofGroup) {
        String hexValue = new String(rawResult);
        return Integer.parseInt(hexValue.substring(ofGroup * 2, (ofGroup + 1) * 2), 16);
    }

    public static int getIntValue(byte[] rawResult, char ofGroup) {
        return getIntValue(rawResult, ofGroup - 'A');
    }

    public static Number calculateFromEquation(byte[] raw, String equation) {
      /*  ScriptEngine mathSolver = new ScriptEngineManager().getEngineByName("JavaScript");
        Map<String, Object> vars = new HashMap<>();
        //vars.put("SignedA", getSignedA2(raw));
        for (int index = 0; index < CalculatedResponse.getGroupCount(raw); index++) {
            vars.put(Character.toString((char) ('A' + index)), CalculatedResponse.getIntValue(raw, index));
            vars.put("Signed" + (char) ('A' + index), CalculatedResponse.getSigned(raw, index));
        }
        try {
            return (Number) mathSolver.eval(equation, new SimpleBindings(vars));
        } catch (ScriptException e) {
            e.printStackTrace();
        }*/

        fillBuffer(raw);
        float result = 0f;
        switch (equation) {
            case "100/255 * (256 * A + B)":
                result = (buffer.get(2) * 256 + buffer.get(3)) * 100 / 255;
                break;
            case "A - 125":
                result = buffer.get(2) - 125;
                break;
            case "(A - 128) * 100/128":
                result = ((buffer.get(2) - 128) * 100) / 128;
                break;
            case "(256 * A + B) / 100":
                result = (buffer.get(2) * 256 + buffer.get(3)) / 100;
                break;
            case "(256 * A + B) / 1000":
                result = (buffer.get(2) * 256 + buffer.get(3)) / 1000;
                break;
            case "256 * A + B":
                result = buffer.get(2) * 256 + buffer.get(3);
                break;
            case "(256 * A + B) / 20":
                result = (buffer.get(2) * 256 + buffer.get(3)) / 20;
                break;
            case "(256 * A + B) /4":
                result = (buffer.get(2) * 256 + buffer.get(3)) / 4;
                break;
            case "((256 * A + B) / 128) - 210":
                result = ((buffer.get(2) * 256 + buffer.get(3)) / 128) - 210;
                break;
            case "A * 10":
                result = buffer.get(2) * 10;
                break;
            case "A / 2 - 64":
                result = (buffer.get(2) / 2) - 64;
                break;
            case "A * 0.005":
                result = (float) (buffer.get(2) / 0.005);
                break;
            case "A * 0.655":
                result = (float) (buffer.get(2) / 0.655);
                break;
            case "A":
                result = buffer.get(2);
                break;
            case "B":
                result = buffer.get(3);
                break;
            case "C":
                result = buffer.get(4);
                break;
            case "D * 10":
                result = buffer.get(5) * 10;
                break;
            case "(256 * C + D) / 256 - 128":
                result = (256 * buffer.get(4) + buffer.get(5)) / 256 - 128;
                break;
            case "(2 / 65536) * (256 * A + B)":
                result = (2 / 65536) * (256 * buffer.get(2) + buffer.get(3));
                break;
            case "(8/65536)*(256 * C + D)":
                result = (8 / 65536) * (256 * buffer.get(4) + buffer.get(5));
                break;
        }
        return (Number) result;
    }

    private static Number getSigned(byte[] raw, int index) {
        int value = getIntValue(raw, index);
        if ((value & (1 << (Byte.SIZE - 1))) != 0) {
            value = value - (1 << Byte.SIZE);
        }
        return value;
    }

    protected int getByte(char group) {
        return getByte(group - 'A');
    }

    protected int getByte(int group) {
        return getIntValue(raw, group);
    }

    @Override
    public byte[] getRawResult() {
        return raw;
    }

    public Number getCalculated() {
        return calculated;
    }

    @Override
    public String getFormattedString() {
        return getCalculated() + getUnit().getSymbol();
    }

    /**
     * <p>fillBuffer.</p>
     */
    public static void fillBuffer(byte[] rawResult) {
        String rawData = new String(rawResult);
        rawData = rawData.replaceAll("\\s", ""); //removes all [ \t\n\x0B\f\r]
        rawData = rawData.replaceAll("(BUS INIT)|(BUSINIT)|(\\.)", "");

        // L.i("Cmd :: " + cmd + " rawData :: " + rawData);
        if (!rawData.matches("([0-9A-F])+")) {
            com.sohrab.obd.reader.utils.LogUtils.i("NonNumericResponseException :: " + rawData);
            throw new NonNumericResponseException(rawData);
        }

        // read string each two chars
        buffer.clear();
        int begin = 0;
        int end = 2;
        while (end <= rawData.length()) {
            buffer.add(Integer.decode("0x" + rawData.substring(begin, end)));
            begin = end;
            end += 2;
        }

        LogUtils.i("buffer :: " + buffer);
    }
}
