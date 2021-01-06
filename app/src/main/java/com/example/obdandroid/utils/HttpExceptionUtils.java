package com.example.obdandroid.utils;

import com.alibaba.fastjson.JSONException;
import com.zhy.http.okhttp.intercepter.NoNetWorkException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Response;

/**
 * 作者：Jealous
 * 日期：2021/1/6 0006
 * 描述：
 */
public class HttpExceptionUtils {
    public static String validateError(Exception error) {

        if (error != null) {
            if (error instanceof NoNetWorkException) {
                return "无网络，请联网重试";
            } else if (error instanceof SocketTimeoutException) {
                return "网络连接超时，请稍候重试";
            } else if (error instanceof JSONException) {
                return "json转化异常";
            } else if (error instanceof ConnectException) {
                return "服务器网络异常或宕机，请稍候重试";
            }
        }
        return "未知异常，请稍候重试";
    }
}