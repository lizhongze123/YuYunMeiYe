package com.example.http;

/**
 * 应API文档中的错误返回值, 指与服务器约定返回的错误
 */
public class ServerResultError extends RuntimeException {

    public static final int TOKEN_INVALID = -1;
    public static final int ALREADY_APPOINTMENT = 20073;

    public int code;
    public String message;

    public ServerResultError(int code, String message) {
        this.code = code;
        this.message = message == null ? "" : message;
    }
}
