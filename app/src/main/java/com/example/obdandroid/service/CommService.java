/*
 * (C) Copyright 2015 by fr3ts0n <erwin.scheuch-heilig@gmx.at>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 *
 */

package com.example.obdandroid.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.example.obdandroid.R;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.obdandroid.config.Constant.DEVICE_ADDRESS;
import static com.example.obdandroid.config.Constant.DEVICE_NAME;
import static com.example.obdandroid.config.Constant.MESSAGE_DEVICE_NAME;
import static com.example.obdandroid.config.Constant.MESSAGE_STATE_CHANGE;
import static com.example.obdandroid.config.Constant.MESSAGE_TOAST;
import static com.example.obdandroid.config.Constant.TOAST;

/**
 * Abstract communication service
 */
public abstract class CommService {
    /**
     * communication media
     */
    public enum MEDIUM {
        BLUETOOTH   ///< Bluetooth device
    }

    /**
     * media type selection
     */
    static MEDIUM medium = MEDIUM.BLUETOOTH;

    /**
     * Constants that indicate the current connection state
     */
    public enum STATE {
        NONE,           ///< we're doing nothing
        LISTEN,         ///< listening for incoming connections
        CONNECTING,     ///< initiating an outgoing connection
        CONNECTED,      ///< connected to a remote device
        OFFLINE         ///< we are offline
    }

    // Debugging
    private static final String TAG = "CommService";
    static final Logger log = Logger.getLogger(TAG);

    Context mContext;
    private Handler mHandler = null;
    STATE mState;

    /**
     * Constructor. Prepares a new Communication session.
     */
    CommService() {
        super();
        mState = STATE.NONE;
    }

    /**
     * Constructor. Prepares a new Communication session.
     *
     * @param handler A Handler to send messages back to the UI Activity
     */
    private CommService(Handler handler) {
        this();
        mHandler = handler;
    }

    /**
     * Constructor. Prepares a new Bluetooth Communication session.
     *
     * @param context The UI Activity Context
     * @param handler A Handler to send messages back to the UI Activity
     */
    CommService(Context context, Handler handler) {
        this(handler);
        mContext = context;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    synchronized void setState(STATE state) {
        log.log(Level.FINE, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(MESSAGE_STATE_CHANGE, state).sendToTarget();
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a session
     * in listening (server) mode. Called by the Activity onResume()
     */
    protected abstract void start();

    /**
     * Stop all threads
     */
    public abstract void stop();

    /**
     * Write to the output device in an un-synchronized manner
     *
     * @param out The bytes to write
     */
    public abstract void write(byte[] out);

    /**
     * start connection to specified device
     *
     * @param device The device to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public abstract void connect(Object device, boolean secure);

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    void connectionFailed() {
        stop();
        // set new state offline
        setState(STATE.OFFLINE);
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, mContext.getString(R.string.unabletoconnect));
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * 指示连接已丢失并通知UI活动。
     */
    void connectionLost() {
        stop();
        // set new state offline
        setState(STATE.OFFLINE);
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, mContext.getString(R.string.connectionlost));
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * 指示已建立连接并通知UI活动。
     */
    void connectionEstablished(String deviceName,String deviceAddress) {
        // 将connectionEstablished设备的名称发送回UI活动
        Message msg = mHandler.obtainMessage(MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_NAME, deviceName);
        bundle.putString(DEVICE_ADDRESS, deviceAddress);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // set state to connected
        setState(STATE.CONNECTED);
    }
}
