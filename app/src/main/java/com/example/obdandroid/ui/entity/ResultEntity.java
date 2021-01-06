package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/1/6 0006
 * 描述：
 */
public class ResultEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : null
     */

    private boolean success;
    private String code;
    private String message;
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}