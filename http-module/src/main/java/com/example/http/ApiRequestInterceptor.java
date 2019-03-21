package com.example.http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiRequestInterceptor implements Interceptor {
    private static ApiRequestInterceptor apiRequestInterceptor = new ApiRequestInterceptor();
    private static final String CHARSET = "Charset";
    private static final String CHARSET_VALUE = "UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String ACCEPT = "Accept";
    private static final String ACCEPT_VALUE = "application/json";
    private static final String RQS_HEADER = "rqs-header";
    private static final String AUTHORIZATION = "Authorization";
    private String tokenId;
    private HttpHeaderData httpHeaderData;
    private Gson gson;

    public static ApiRequestInterceptor getInstance() {
        return apiRequestInterceptor;
    }
    private ApiRequestInterceptor() {
        if (httpHeaderData == null) {
            httpHeaderData = new HttpHeaderData();
        }
        if (gson == null) {
            gson = new Gson();
        }
    }

    public void setToken(String tokenId) {
        this.tokenId = tokenId;
        if (tokenId != null) {
            httpHeaderData.tokenid = tokenId;
            Log.e("lzz", "设置tooken成功，tooken为--"+ httpHeaderData.tokenid);
        }else{
            Log.e("lzz", "设置tooken失败");
        }
    }

    public String getTokenId() {
        return this.tokenId;
    }


    public void clear() {
        this.tokenId = "";
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (tokenId != null) {
            httpHeaderData.tokenid = tokenId;
        }
        Request original = chain.request();
        Request request = original.newBuilder()
                .header(CHARSET, CHARSET_VALUE)
                .header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                .header(ACCEPT, ACCEPT_VALUE)
                .header(AUTHORIZATION, httpHeaderData.tokenid)
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
