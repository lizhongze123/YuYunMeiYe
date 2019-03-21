package com.example.http;

/**
 * @author
 * @desc
 * @date
 */
public class ResultResponse {

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        public int result;
    }
}
