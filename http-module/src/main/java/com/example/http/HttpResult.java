package com.example.http;


import com.google.gson.annotations.SerializedName;

public class HttpResult<T> {

    public int code;

    @SerializedName(value = "message", alternate = {"msg"})
    public String msg;
    public T data;

}


