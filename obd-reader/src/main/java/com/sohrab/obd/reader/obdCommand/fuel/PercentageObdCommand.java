/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sohrab.obd.reader.obdCommand.fuel;

import android.annotation.SuppressLint;

import com.sohrab.obd.reader.obdCommand.ObdCommand;

/**
 * Abstract class for percentage commands.
 */
public abstract class PercentageObdCommand extends ObdCommand {

    protected float percentage = 0f;

    /**
     * <p>Constructor for PercentageObdCommand.</p>
     *
     * @param command a {@link String} object.
     */
    public PercentageObdCommand(String command) {
        super(command);
    }

    /**
     * <p>Constructor for PercentageObdCommand.</p>
     */
    public PercentageObdCommand(PercentageObdCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        if (buffer.size() != 0) {
            isHaveData = true;
            percentage = (buffer.get(2) * 100.0f) / 255.0f;
        } else {
            isHaveData = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("DefaultLocale")
    @Override
    public String getFormattedResult() {
        if (isHaveData) {
            return String.format("%.1f%s", percentage, getResultUnit());
        } else {
            return "";
        }
    }

    /**
     * <p>Getter for the field <code>percentage</code>.</p>
     *
     * @return a float.
     */
    public float getPercentage() {
        return percentage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultUnit() {
        return "%";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        if (isHaveData) {
            return String.valueOf(percentage);
        } else {
            return "";
        }
    }
}
