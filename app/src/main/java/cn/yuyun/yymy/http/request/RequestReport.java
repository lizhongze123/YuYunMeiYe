package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/24
 */
public class RequestReport {

    public long start;
    public long end;

    @SerializedName("sp_id")
    public String spId;

    public String type;
}
