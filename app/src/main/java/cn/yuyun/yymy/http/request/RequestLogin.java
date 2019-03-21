package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */

public class RequestLogin {

    @SerializedName("username")
    public String phoneNum;

    @SerializedName("password")
    public String pwd;

    /**2-App端（token有效期1个月*/
    @SerializedName("device_type")
    public int deviceType = 2;

    public RequestLogin(String phoneNum, String pwd){
        this.phoneNum = phoneNum;
        this.pwd = pwd;
    }
}
