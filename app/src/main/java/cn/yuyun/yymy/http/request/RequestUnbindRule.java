package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;


/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/6
 */

public class RequestUnbindRule {

    @SerializedName("og_id")
    public int og_id;

    @SerializedName("og_type")
    public int og_type;

    @SerializedName("rule_id")
    public int rule_id;

}
