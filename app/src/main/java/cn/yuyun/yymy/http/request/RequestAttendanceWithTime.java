package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;


/**
 * @author
 * @desc
 * @date
 */

public class RequestAttendanceWithTime {

    public String baseon_id;
    public String baseon_type;
    public String staff_id;

    @SerializedName("start")
    public long start;

    @SerializedName("end")
    public long end;

}
