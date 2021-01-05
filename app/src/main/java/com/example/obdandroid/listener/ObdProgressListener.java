package com.example.obdandroid.listener;

import com.example.obdandroid.service.ObdCommandJob;

public interface ObdProgressListener {

    void stateUpdate(final ObdCommandJob job);

}