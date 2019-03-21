package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestMemberFilter {

    @SerializedName("sp_id_list")
    public List<String> spIdList;

    public String level_id;

    /** 1正常  -1回收站 */
    public int status = 1;

    public String search_text;

    /*@SerializedName("days_till_last_consume_time")
    public String daysTillLastConsumeTime;*/

    @SerializedName("days_till_last_consume_time_start")
    public String dayStart;

    @SerializedName("days_till_last_consume_time_end")
    public String dayEnd;
}
