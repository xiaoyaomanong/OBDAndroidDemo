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

package com.example.obdandroid.ui.obd2.command;

import com.example.obdandroid.ui.obd2.Command;
import com.example.obdandroid.ui.obd2.Response;
import com.example.obdandroid.ui.obd2.command.livedata.FreezeDiagnosticTroubleCode;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

/**
 *
 * @author MacFJA
 */
public class FrozeCommand implements Command {
    private Command service01Command;

    /**
     * @param service01Command The Service 01 command
     * @throws IllegalArgumentException If the command is not a Service 01 command or if the command is "01 01"
     */
    public FrozeCommand(Command service01Command) throws IllegalArgumentException {
        if (!service01Command.getRequest().startsWith("01")) {
            throw new IllegalArgumentException("The command must be a live data (Service 01) command");
        }
        if (service01Command.getRequest().endsWith("01")) {
            throw new IllegalArgumentException("The command '01 01' is not available in frozen context (Service 02)");
        }
        this.service01Command = service01Command;
    }

    /**
     * Get the list of all command that are in or compatible with Service 02
     *
     * @return List of commands
     */
    public static Map<String, FrozeCommand> getService02Commands() {
        Map<String, Command> commands = LiveCommand.getService01Commands();
        commands.remove("01"); // Service 01 only
        commands.put("02", new FreezeDiagnosticTroubleCode()); // Service 02 only

        Map<String, FrozeCommand> frozeCommands = new HashMap<>();

        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            frozeCommands.put(entry.getKey(), new FrozeCommand(entry.getValue()));
        }

        return frozeCommands;
    }

    /**
     * Get the list of all command that are in or compatible with Mode 02
     *
     * @deprecated Since 1.1.0, replaced by {@link #getService02Commands()}
     * @return List of commands
     */
    @Deprecated
    public static Map<String, FrozeCommand> getMode02Commands() {
        return getService02Commands();
    }

    @Override
    public String getRequest() {
        return "02" + service01Command.getRequest().substring(2);
    }

    @Override
    public Response getResponse(byte[] rawResult) throws ScriptException {
        return service01Command.getResponse(rawResult);
    }

    @Override
    public String toString() {
        return service01Command.toString();
    }
}
