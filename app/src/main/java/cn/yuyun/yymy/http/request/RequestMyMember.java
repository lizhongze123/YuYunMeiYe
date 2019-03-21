package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestMyMember {

    @SerializedName("search_text")
    public String searchText = "";

    public String level_id;
    public String birth_day_tils;

    public List<String> staff_id_list;

}
