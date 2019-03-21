package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/21
 */
public class RequestStoreMember {


    @SerializedName("search_text")
    public String searchText = "";

    @SerializedName("days_till_last_consume_time")
    public String daysTillLastConsumeTime;

    @SerializedName("level_id")
    public String levelId;

    @SerializedName("sp_id")
    public String spId;
}
