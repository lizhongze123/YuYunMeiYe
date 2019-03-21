package com.example.http;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;


class ExceptionMgr {

    // 对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    static ApiException handleException(Throwable e) {
        ApiException ex;
        // HTTP错误，表示状态码不为200
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络错误，请稍候重试";
                    break;
            }
            return ex;
        // 与服务器约定返回的错误
        } else if (e instanceof ServerResultError) {
            ServerResultError resultException = (ServerResultError) e;
            ex = new ApiException(resultException, resultException.code);
            ex.message = resultException.message;
            if(resultException.code == ServerResultError.TOKEN_INVALID){
                ex.code =  ServerResultError.TOKEN_INVALID;
            }
            return ex;

        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.message = "数据解析出错";
            return ex;

        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR.NETWORK_ERROR);
            ex.message = "网络连接失败";
            return ex;

        } else {
            ex = new ApiException(e, ERROR.UNKNOWN);
//            ex.message = "未知错误";
            ex.message = "请检查网络";
            return ex;
        }
    }


    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * HTTP协议出错
         */
        public static final int HTTP_ERROR = 1003;
    }
}
