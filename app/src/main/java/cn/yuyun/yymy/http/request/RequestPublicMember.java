package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/3/19
 */

public class RequestPublicMember {

    public int no_supervisor;

    @SerializedName("search_text")
    public String searchText = "";

    @SerializedName("days_till_last_consume_time_start")
    public String days_till_last_consume_time_start;

    @SerializedName("days_till_last_consume_time_end")
    public String days_till_last_consume_time_end;

    @SerializedName("sp_id_list")
    public List<String> spIdList;

    public String level_id;

}
