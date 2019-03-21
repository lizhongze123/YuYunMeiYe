package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class RequestModifyStaffStatus {

    @SerializedName("staff_id")
    public String staffId;
    public RequestStoreStaff.JobStatus status;

}
