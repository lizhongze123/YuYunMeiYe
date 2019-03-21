package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/18
 */

public class RequestPartnerLogin {

    @SerializedName("username")
    public String userName;

    @SerializedName("password")
    public String pwd;

    public RequestPartnerLogin(String phoneNum, String pwd){
        this.userName = phoneNum;
        this.pwd = pwd;
    }
}
