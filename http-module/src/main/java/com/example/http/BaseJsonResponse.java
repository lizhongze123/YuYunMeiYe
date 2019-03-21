package com.example.http;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/23
 */

public class BaseJsonResponse {


    /**
     * headers : {}
     * body : {"code":-3,"result":false,"message":"AUTH TOKEN 验证失败!","data":"获取 Authorization 失败!"}
     * statusCode : OK
     * statusCodeValue : 200
     */

    public HeadersBean headers;
    public BodyBean body;
    public String statusCode;
    public int statusCodeValue;

    public static class HeadersBean {
    }

    public static class BodyBean {
        /**
         * code : -3
         * result : false
         * message : AUTH TOKEN 验证失败!
         * data : 获取 Authorization 失败!
         */

        public int code;
        public boolean result;
        public String message;
        public String data;
    }


    public BaseJsonResponse setResult(Object obj){

        return this;
    }
}
