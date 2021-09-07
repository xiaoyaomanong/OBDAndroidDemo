package com.example.obdandroid.http;

/**
 * 作者：Jealous
 * 日期：2021/9/4 0004
 * 描述：
 */
public class RspModel <T> {

    public final static int CODE_SUCCESS = 200;//成功
    public final static int CODE_FAIL = 400;//失败
    public final static int CODE_UNAUTHORIZED = 401;//未认证（签名错误）
    public final static int CODE_NOT_FOUND = 404;//接口不存在
    public final static int CODE_INTERNAL_SERVER_ERROR = 500;//服务器内部错误

    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}