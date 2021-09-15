package com.sohrab.obd.reader.obdCommand.obdException;

/**
 * Thrown when there is a "BUS INIT... ERROR" message
 *
 * @author pires
 * @version $Id: $Id
 */
public class BusInitException extends ResponseException {

    /**
     * <p>Constructor for BusInitException.</p>
     */
    public BusInitException() {
        super("总线初始化。。。错误");
    }

}
