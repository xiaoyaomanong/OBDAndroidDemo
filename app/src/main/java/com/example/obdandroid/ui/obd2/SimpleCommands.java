package com.example.obdandroid.ui.obd2;

import com.example.obdandroid.ui.obd2.response.CalculatedResponse;
import com.example.obdandroid.ui.obd2.response.RawResponse;

import javax.script.ScriptException;

/**
 * <p>Utility class to create simple custom command.</p>
 * <p>Be careful with custom command, especially with command that change the response format.
 * For example, sending a "AT H1" with a custom command will break all later reading.</p>
 *
 * @author MacFJA
 */
public final class SimpleCommands {

    private SimpleCommands() {
    }

    /**
     * Create a simple command that will return a {@link RawResponse}
     *
     * @param request The command request
     * @return A simple command
     */
    public static com.example.obdandroid.ui.obd2.Command create(final String request) {
        return new com.example.obdandroid.ui.obd2.Command() {
            @Override
            public String getRequest() {
                return request;
            }

            @Override
            public com.example.obdandroid.ui.obd2.Response getResponse(byte[] rawResult) {
                return new RawResponse(rawResult) {
                };
            }
        };
    }

    /**
     * Create a simple command that will return a {@link CalculatedResponse}
     *
     * @param request  The command request
     * @param equation The equation for the result
     * @return A simple command
     */
    public static com.example.obdandroid.ui.obd2.Command createCalculated(String request, String equation) {
        return createCalculated(request, equation, com.example.obdandroid.ui.obd2.Unit.NoUnit);
    }

    /**
     * Create a simple command that will return a {@link CalculatedResponse}
     *
     * @param request  The command request
     * @param equation The equation for the result
     * @param unit     The unit of the result
     * @return A simple command
     */
    public static com.example.obdandroid.ui.obd2.Command createCalculated(final String request, final String equation, final com.example.obdandroid.ui.obd2.Unit unit) {
        return new Command() {
            @Override
            public String getRequest() {
                return request;
            }

            @Override
            public Response getResponse(byte[] rawResult) throws ScriptException {
                return new CalculatedResponse(rawResult, equation) {
                    @Override
                    public Unit getUnit() {
                        return unit;
                    }
                };
            }
        };
    }
}
