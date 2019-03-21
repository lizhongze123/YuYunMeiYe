package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import butterknife.BindView;

/**
 * @author
 * @desc
 * @date
 */
public class RequestHandmakeList {

    @SerializedName("start_date")
    public long start;
    @SerializedName("end_date")
    public long end;
    @SerializedName("staff_id")
    public String staffId;
    @SerializedName("group_id")
    public String groupId;

}
