package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAmount {

    public List<String> sp_id_list;

    @SerializedName("group_id")
    public String groupId;
}
