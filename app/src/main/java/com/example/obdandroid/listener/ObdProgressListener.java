package com.example.obdandroid.listener;

import com.github.pires.obd.commands.ObdCommand;

public interface ObdProgressListener {

    void stateUpdate(final ObdCommand job);

}