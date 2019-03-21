package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestStorehouse {

    @SerializedName("group_id")
    public String groupId;

    @SerializedName("sp_id_list")
    public List<String> spIdList;


}
