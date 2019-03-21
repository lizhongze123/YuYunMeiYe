package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAddRead {

    @SerializedName("new_publish_time")
    public long newPublishTime;

    /**1.工作汇报 2.培训资料 3.请假审核*/
    @SerializedName("status")
    public int status;
}
