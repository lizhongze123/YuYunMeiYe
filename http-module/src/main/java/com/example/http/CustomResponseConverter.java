package com.example.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;


/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/23
 */

final class CustomResponseConverter<T> implements Converter<ResponseBody, T>{

    private Gson gson;
    private TypeAdapter<T> adapter;
    private Type mType;

    CustomResponseConverter(Gson gson, TypeAdapter<T> mAdapter, Type mType) {
        this.gson = gson;
        this.adapter = mAdapter;
        this.mType = mType;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        /*HttpResult result = new HttpResult();
        String body = null;
        try {
            body = value.string();
            JSONObject json = new JSONObject(body);
        } catch (Exception e) {
            if(e instanceof JSONException){
                BaseJsonResponse rawResult = gson.fromJson(body, BaseJsonResponse.class);
                result.data = rawResult.body.data;
                result.code = rawResult.body.code;
                result.msg = rawResult.body.message;
                return (T) result;
            }
        }
        return gson.fromJson(body, mType);*/

        String response = value.string();
        try {
            //ResultResponse 只解析result字段
            ResultResponse resultResponse = gson.fromJson(response, ResultResponse.class);


            if(null != resultResponse.data){
                if (resultResponse.data.result == 1){
                    //result==1表示成功返回，继续用本来的Model类解析
                    return gson.fromJson(response, mType);
                } else {
                    //ErrResponse 将msg解析为异常消息文本
                    ErrResponse errResponse = gson.fromJson(response, ErrResponse.class);
                    if(resultResponse.data.result == 0){
                        throw new ServerResultError(resultResponse.data.result, errResponse.data.data);
                    } else{
                        throw new ServerResultError(resultResponse.data.result, errResponse.data.msg);
                    }
                }
            }else{
                if(resultResponse.code == ServerResultError.TOKEN_INVALID){
                    throw new ServerResultError(resultResponse.code, "账号信息已过期，请重新登录");
                }else{
                    throw new ServerResultError(resultResponse.code, resultResponse.msg);
                }
            }

        } finally {

        }

    }
}
