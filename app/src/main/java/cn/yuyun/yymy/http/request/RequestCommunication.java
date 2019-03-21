package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class RequestCommunication {

    @SerializedName("staff_id")
    public String staffId;

    public long start;
    public long end;

}
