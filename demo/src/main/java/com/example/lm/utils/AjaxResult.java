package com.example.lm.utils;

import java.io.Serializable;

public class AjaxResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private Object data;

    public AjaxResult() {
    }

    public AjaxResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public AjaxResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static AjaxResult success(int code, String message) {
        return new AjaxResult(code, message);
    }

    public static AjaxResult success(int code, String message, Object data) {
        return new AjaxResult(code, message, data);
    }

    public static AjaxResult fail(int code, String message) {
        return new AjaxResult(code, message);
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

