package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lzz
 * @desc
 * @date 2018/1/19
 */

public class RequestPersonNumber {

    @SerializedName("start_date")
    public long start;

    @SerializedName("end_date")
    public long end;

    @SerializedName("staff_id")
    public String staffId;

    @SerializedName("group_id")
    public String groupId;

    @SerializedName("sp_id_list")
    public List<String> spIdList;

}
