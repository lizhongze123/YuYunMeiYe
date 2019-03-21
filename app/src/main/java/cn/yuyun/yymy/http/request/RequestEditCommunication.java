package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestEditCommunication {

    public int id;

    @SerializedName("group_id")
    public String groupId;

    public String staffId;

    public String content;

    @SerializedName("communication_time")
    public long comTime;

    public List<String> pictures;

}
