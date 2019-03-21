package com.example.http;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/8/23
 */
public class ErrResponse {
    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        public int result;
        public String data;
        public String msg;
    }
}
