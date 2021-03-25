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

package com.example.obdandroid.ui.obd2.elm327;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.obdandroid.ui.obd2.AbsCommander;
import com.example.obdandroid.ui.obd2.Command;
import com.example.obdandroid.ui.obd2.Response;
import com.example.obdandroid.ui.obd2.elm327.command.BooleanCommand;
import com.example.obdandroid.ui.obd2.elm327.response.NoDataResponse;
import com.example.obdandroid.ui.obd2.elm327.response.ResponseOK;
import com.example.obdandroid.ui.obd2.exception.ExceptionResponse;
import com.example.obdandroid.ui.obd2.exception.UnexpectedResponse;
import com.example.obdandroid.utils.LogUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The commander (send and receive) to work with Elm327.
 *
 * @author MacFJA
 */
public class Commander extends AbsCommander {
    private final Map<String, Boolean> outputInfo = new HashMap<>();
    private String currentRequest;
    private Command currentCommand;
    private String lastRequest;

    public Commander() {
        super();
        initState(Arrays.asList(
                BooleanCommand.EchoOn,
                BooleanCommand.HeaderOff,
                BooleanCommand.LinefeedOff,
                BooleanCommand.MemoryOff,
                BooleanCommand.ResponseOn,
                BooleanCommand.SpaceOn,
                BooleanCommand.DLCVariableOff,
                BooleanCommand.AutomaticFormattingOn,
                BooleanCommand.FlowControlOn
        ));
    }

    /**
     * <p>定义ELM327接口的状态.</p>
     * <p>可用于恢复ELM327接口之间会话的状态</p>
     *
     * @param states 定义如何配置ELM327的选项列表
     */
    protected void initState(List<BooleanCommand> states) {
        for (BooleanCommand command : states) {
            putBooleanCommand(command);
        }
    }

    /**
     * 获取当前ELM327的选项列表（{@link BooleanCommand}）
     *
     * @return List of options
     */
    public List<BooleanCommand> getState() {
        List<BooleanCommand> state = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : outputInfo.entrySet()) {
            state.add(BooleanCommand.getCommand(entry.getKey(), entry.getValue()));
        }

        return state;
    }

    /**
     * 注册一个{@link BooleanCommand}，以反映它对命令和响应的操作
     *
     * @param command The command to save
     */
    protected void putBooleanCommand(BooleanCommand command) {
        outputInfo.put(command.getPrefix(), command.isOnVersion());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response sendCommand(Command command) throws IOException {
        if (command instanceof BooleanCommand && !((BooleanCommand) command).getPrefix().equals("E")) {
            putBooleanCommand((BooleanCommand) command);
        }
        currentRequest = command.getRequest();
        currentCommand = command;
        Response response;
        try {
            response = super.sendCommand(command);
        } catch (ExceptionResponse exceptionResponse) {
            LogUtils.w("The response is not a normal response: {}" + exceptionResponse.getLocalizedMessage());
            response = exceptionResponse;
        }

        if (command instanceof BooleanCommand && ((BooleanCommand) command).getPrefix().equals("E")) {
            putBooleanCommand((BooleanCommand) command);
        }

        lastRequest = currentRequest;

        if (response instanceof ResponseOK && ((ResponseOK) response).isError()) {
            LogUtils.w("The command did not succeed: {}" + Arrays.toString(response.getRawResult()));
            response = new UnexpectedResponse(response.getRawResult());
        }

        if (new String(response.getRawResult()).equals("NO DATA") || new String(response.getRawResult()).equals("NODATA")) {
            LogUtils.w("The ELM327 return a 'NO DATA'");
            response = (Response) new NoDataResponse();
        }

        if (response instanceof ExceptionResponse) {
            LogUtils.w("The command wasn't successful, remove it from the persistent storage");
            unpersistCommand(command.getRequest());
        }

        return response;
    }

    protected boolean isCurrent(BooleanCommand command) {
        if (!outputInfo.containsKey(command.getPrefix())) {
            return false;
        }
        return outputInfo.get(command.getPrefix()).equals(command.isOnVersion());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected byte[] read() throws IOException, ExceptionResponse {
        byte[] raw = super.read();
        boolean dontFilter = currentCommand.getClass().getAnnotation(DontFilterResponse.class) != null;

        if (isCurrent(BooleanCommand.ResponseOff)) {
            return new byte[0];
        }

        String rawString = new String(raw);

        if (rawString.contains("NO DATA\r")) {
            throw new NoDataResponse();
        }

        if (isCurrent(BooleanCommand.HeaderOn)) {
            // Each line have the following format :
            // PP RR TT DD DD DD DD DD DD DD CC
            // With "PP"=Priority, "RR"=Receiver ID, "TT"=Transmitter ID, "DD"=Data and "CC"=Checksum
            Pattern pattern = Pattern.compile("^(?<message>(?<header>(?<priority>[a-f0-9]{2}) ?(?<receiver>[a-f0-9]{2}) ?(?<transmitter>[a-f0-9]{2})) ?(?<data>[a-f0-9 ]{2,})) ?(?<checksum>[a-f0-9]{2})$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(rawString);

            StringBuilder newRawString = new StringBuilder();

            while (matcher.find()) {
                long data = Long.parseLong(matcher.group("message").replace(" ", ""), 16);
                long checksum = getBinaryChecksum(data, 8);
                if (checksum != Long.parseLong(matcher.group("checksum"))) {
                    throw new UnexpectedResponse(rawString.getBytes());
                }
                newRawString.append(matcher.group("data")).append("\r");
            }

            if (newRawString.length() == 0) {
                throw new UnexpectedResponse(rawString.getBytes());
            }

            rawString = newRawString.toString();
        }

        if (isCurrent(BooleanCommand.EchoOn)) {
            rawString = rawString.replaceFirst(Pattern.quote(currentRequest), "");
            rawString = rawString.replaceAll("^\\s+", "");
        }

        String responseHeader = "0000";
        if (currentRequest.substring(0, 1).matches("[0-9]")) {
            responseHeader = Integer.toHexString(4 + Integer.parseInt(currentRequest.substring(0, 1)))
                    + currentRequest.substring(1);
        }

        if (rawString.startsWith(responseHeader)) {
            rawString = rawString.substring(responseHeader.length());
        }

        if (isCurrent(BooleanCommand.SpaceOn) && !dontFilter) {
            Pattern space = Pattern.compile("([^a-z0-9\\n\\r])", Pattern.CASE_INSENSITIVE);
            rawString = space.matcher(rawString).replaceAll("");
        }
        if (isCurrent(BooleanCommand.LinefeedOn)) {
            rawString = rawString.replaceAll("\\r\\n", "\r");
        }

        rawString = rawString.replaceAll("\\r+", "\r");
        rawString = rawString.replaceAll("\\r$", "");

        if (rawString.equals("NODATA")) {
            throw new NoDataResponse();
        }

        LogUtils.i("Clean response of the command: {}" + rawString);
        return rawString.getBytes();
    }

    @Override
    protected ReadCharResult actionForChar(char read, StringBuilder context) {
        if (read == '>') {
            LogUtils.d("Receive char >, stop reading");
            return ReadCharResult.IgnoreAndStop;
        }
        return super.actionForChar(read, context);
    }

    @Override
    protected String dataToSend(Command command) {
        if (command.getRequest().equals(lastRequest)) {
            return "\r";
        }
        String data = super.dataToSend(command) + "\r";

        if (isCurrent(BooleanCommand.SpaceOff)) {
            return data.replaceAll(" ", "");
        }

        return data;
    }

    /**
     * 通过关闭多个选项减少发送回的数据量
     *
     * @throws IOException 如果在与OBD接口通信期间发生错误
     */
    public void reduceCommunicationSize() throws IOException {
        LogUtils.i("Reducing communication by turning off 'Echo', 'Line Feed', 'Space' and 'Header'");
        sendCommand(BooleanCommand.EchoOff);
        sendCommand(BooleanCommand.LinefeedOff);
        sendCommand(BooleanCommand.SpaceOff);
        sendCommand(BooleanCommand.HeaderOff);
    }
}
