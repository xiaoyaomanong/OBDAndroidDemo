package com.sohrab.obd.reader.obdCommand.obdException;

/**
 * 当响应中没有数字且预期为数字时引发
 *
 * @author pires
 * @version $Id: $Id
 */
public class NonNumericResponseException extends RuntimeException {

    /**
     * <p>Constructor for NonNumericResponseException.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public NonNumericResponseException(String message) {
        super("Error reading response: " + message);
    }

}
