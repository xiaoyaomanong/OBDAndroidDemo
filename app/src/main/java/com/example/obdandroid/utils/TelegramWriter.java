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
 */

package com.example.obdandroid.utils;

import java.util.EventListener;

/**
 * 电报作家
 * 处理传出协议电报的接口
 */
public interface TelegramWriter extends EventListener {
    /**
     * 处理传出协议电报
     *
     * @param buffer - telegram buffer
     * @return number of bytes sent
     */
    int writeTelegram(char[] buffer);

    /**
     * 处理传出协议电报
     *
     * @param buffer - telegram buffer
     * @param type   telegram type (numeric ID)
     * @param id     unique telegram ID (Sequence number) may be null to generate automatic
     * @return number of bytes sent
     */
    int writeTelegram(char[] buffer, int type, Object id);

}
