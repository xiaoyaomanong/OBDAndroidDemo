package com.sohrab.obd.reader.obdCommand;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：Jealous
 * 日期：2021/3/16
 * 描述：
 */
public class FreezeFrameCommand extends ObdCommand {

    /**
     * Constant <code>dtcLetters={'P', 'C', 'B', 'U'}</code>
     */
    protected final static char[] dtcLetters = {'P', 'C', 'B', 'U'};
    /**
     * Constant <code>hexArray="0123456789ABCDEF".toCharArray()</code>
     */
    protected final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    protected StringBuilder codes;

    /**
     * <p>Constructor for TroubleCodesCommand.</p>
     */
    public FreezeFrameCommand(ModeTrim modeTrim) {
        super(modeTrim.build() + " 02");
        codes = new StringBuilder();
    }

    /**
     * Copy ctor.
     */
    public FreezeFrameCommand(FreezeFrameCommand other) {
        super(other);
        codes = new StringBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillBuffer() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        final String result = getResult().replace("SEARCHING...", "").replace("NODATA", "");
        LogUtils.i("result :: " + result);
        String workingData = null;
        int startIndex = 0;//标题大小
       // String canOneFrame = result.replaceAll("[\r\n]", "");
        int canOneFrameLength = result.length();
        if (canOneFrameLength <= 16 && canOneFrameLength % 4 == 0) {//CAN（ISO-15765）协议一帧。
            workingData = result;//43yy{codes}
            startIndex = 4;//Header is 43yy, yy 显示数据项的数量。
        }

        codes.delete(0, codes.length());
        for (int begin = startIndex; begin < workingData.length(); begin += 4) {
            String dtc = "";
            byte b1 = hexStringToByteArray(workingData.charAt(begin));
            int ch1 = ((b1 & 0xC0) >> 6);
            int ch2 = ((b1 & 0x30) >> 4);
            dtc += dtcLetters[ch1];
            dtc += hexArray[ch2];
            dtc += workingData.substring(begin + 1, begin + 4);
            if (dtc.equals("P0000")) {
                return;
            }
            codes.append(dtc);
            codes.append('\n');
        }
    }

    private byte hexStringToByteArray(char s) {
        return (byte) ((Character.digit(s, 16) << 4));
    }

    /**
     * <p>formatResult.</p>
     *
     * @return the formatted result of this command in string representation.
     * @deprecated use #getCalculatedResult instead
     */
    public String formatResult() {
        return codes.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(codes);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void readRawData(InputStream in) throws IOException {
        byte b;
        StringBuilder res = new StringBuilder();

        // read until '>' arrives OR end of stream reached (and skip ' ')
        char c;
        while (true) {
            b = (byte) in.read();
            LogUtils.i("result0000000 :: " + b);
            if (b == -1) // -1 if the end of the stream is reached
            {
                break;
            }
            c = (char) b;
            if (c == '>') // read until '>' arrives
            {
                break;
            }
            if (c != ' ') // skip ' '
            {
                res.append(c);
            }
        }

        rawData = res.toString().trim();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        return codes.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.TROUBLE_CODES.getValue();
    }
}