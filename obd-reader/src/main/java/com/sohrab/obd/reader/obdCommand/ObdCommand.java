package com.sohrab.obd.reader.obdCommand;

import android.util.Log;

import com.sohrab.obd.reader.obdCommand.obdException.BusInitException;
import com.sohrab.obd.reader.obdCommand.obdException.MisunderstoodCommandException;
import com.sohrab.obd.reader.obdCommand.obdException.NoDataException;
import com.sohrab.obd.reader.obdCommand.obdException.NonNumericResponseException;
import com.sohrab.obd.reader.obdCommand.obdException.ResponseException;
import com.sohrab.obd.reader.obdCommand.obdException.StoppedException;
import com.sohrab.obd.reader.obdCommand.obdException.UnableToConnectException;
import com.sohrab.obd.reader.obdCommand.obdException.UnknownErrorException;
import com.sohrab.obd.reader.obdCommand.obdException.UnsupportedCommandException;
import com.sohrab.obd.reader.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by EMP203 on 6/13/2017.
 */

/**
 * Base OBD command.
 *
 * @author pires
 * @version $Id: $Id
 */
public abstract class ObdCommand {
    private final Class[] ERROR_CLASSES = {
            UnableToConnectException.class,
            BusInitException.class,
            MisunderstoodCommandException.class,
            NoDataException.class,
            StoppedException.class,
            UnknownErrorException.class,
            UnsupportedCommandException.class
    };
    protected ArrayList<Integer> buffer = null;
    protected String cmd = null;
    protected boolean useImperialUnits = false;
    protected String rawData = null;
    protected boolean isHaveData = true;

    /**
     * Default ctor to use
     *
     * @param command the command to send
     */
    public ObdCommand(String command) {
        this.cmd = command;
        this.buffer = new ArrayList<>();
    }

    /**
     * Prevent empty instantiation
     */
    private ObdCommand() {
    }

    /**
     * Copy ctor.
     *
     * @param other the ObdCommand to copy.
     */
    public ObdCommand(ObdCommand other) {
        this(other.cmd);
    }

    /**
     * Sends the OBD-II request and deals with the response.
     * <p>
     * This method CAN be overriden in fake commands.
     *
     * @param in  a {@link InputStream} object.
     * @param out a {@link OutputStream} object.
     * @throws IOException          if any.
     * @throws InterruptedException if any.
     */
    public void run(InputStream in, OutputStream out) throws Exception {
        sendCommand(out);
        readResult(in);
    }

    /**
     * 发送OBD-II请求。
     * <p>
     *此方法可以在子类中重写，例如ObMultiCommand或
     *
     * @param out The output stream.
     * @throws IOException          if any.
     * @throws InterruptedException if any.
     */
    protected void sendCommand(OutputStream out) throws Exception {
        out.write((cmd + "\r").getBytes());
        out.flush();
    }

    /**
     * 重新发送这个命令。
     *
     * @param out a {@link OutputStream} object.
     * @throws IOException if any.
     */
    protected void resendCommand(OutputStream out) throws IOException {
        out.write("\r".getBytes());
        out.flush();
    }

    /**
     * Reads the OBD-II response.
     * <p>
     * 这个方法可以在子类中被重写，比如ObdMultiCommand。
     *
     * @param in a {@link InputStream} object.
     * @throws IOException if any.
     */
    protected void readResult(InputStream in) throws Exception {
        readRawData(in);
        checkForErrors();
        fillBuffer();
        performCalculations();
    }

    /**
     * 计算
     */
    protected abstract void performCalculations();

    /**
     * 填充数据
     */
    protected void fillBuffer() {
        rawData = rawData.replaceAll("\\s", ""); //removes all [ \t\n\x0B\f\r]
        rawData = rawData.replaceAll("(BUS INIT)|(BUSINIT)|(\\.)", "");
        Log.e("BaseActivity", "Cmd :: " + cmd + " rawData :: " + rawData);
        if (!rawData.matches("([0-9A-F])+")) {
            rawData = "N/A";
        }
        if (!rawData.equals("N/A")) {
            //每两个字符读取字符串
            buffer.clear();
            int begin = 0;
            int end = 2;
            while (end <= rawData.length()) {
                buffer.add(Integer.decode("0x" + rawData.substring(begin, end)));
                begin = end;
                end += 2;
            }
        }
        Log.e("BaseActivity", "buffer:" + buffer);
    }

    /**
     * 读取数据
     *
     * @param in a {@link InputStream} object.
     * @throws IOException if any.
     */
    protected void readRawData(InputStream in) throws Exception {
        byte b;
        StringBuilder res = new StringBuilder();
        char c;
        while (((b = (byte) in.read()) > -1)) {
            c = (char) b;
            if (c == '>') {// read until '>' arrives
                break;
            }
            res.append(c);
        }
        Log.e("BaseActivity", "Cmd :: " + cmd + " res :: " + res.toString());
        rawData = res.toString().replaceAll("SEARCHING", "");
        rawData = rawData.replaceAll("\\s", "");//removes all [ \t\n\x0B\f\r]
    }

    @SuppressWarnings("unchecked")
    void checkForErrors() {
        for (Class<? extends ResponseException> errorClass : ERROR_CLASSES) {
            ResponseException messageError;
            try {
                messageError = errorClass.newInstance();
                messageError.setCommand(this.cmd);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (messageError.isError(rawData)) {
                //throw messageError;
                rawData = "N/A";
            }
        }
    }

    /**
     * @return 以字符串表示的原始命令响应。
     */
    public String getResult() {
        return rawData;
    }

    /**
     * @return 字符串表示形式的格式化命令响应。
     */
    public abstract String getFormattedResult();

    /**
     *
     * @return 命令响应以字符串表示，不带格式。
     */
    public abstract String getCalculatedResult();

    ;

    /**
     * @return 表示单位或“”的字符串，从不为空
     */
    public String getResultUnit() {
        return "";
    }

    /**
     * @return OBD命令名。
     */
    public abstract String getName();

    /**
     * @return PID
     */
    public final String getCommandPID() {
        return cmd.substring(3);
    }

}

