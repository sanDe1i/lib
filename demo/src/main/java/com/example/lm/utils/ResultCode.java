package com.example.lm.utils;
public enum ResultCode {
    SUCCESS(20000),
    ERROR(20001);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}

