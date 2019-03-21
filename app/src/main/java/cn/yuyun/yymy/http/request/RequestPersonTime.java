package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */

public class RequestPersonTime {

    @SerializedName("start_date")
    public long start;

    @SerializedName("end_date")
    public long end;

    @SerializedName("staff_id")
    public String staffId;

    public String group_id;

}
