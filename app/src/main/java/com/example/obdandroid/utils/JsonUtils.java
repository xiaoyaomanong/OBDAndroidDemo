package com.example.obdandroid.utils;

import com.alibaba.fastjson.JSON;

/**
 * 作者：Jealous
 * 日期：2021/2/22 0022
 * 描述：
 */
public class JsonUtils {
    public static boolean isGoodJson(String str) {
        boolean result = false;
        try {
            Object obj = JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}