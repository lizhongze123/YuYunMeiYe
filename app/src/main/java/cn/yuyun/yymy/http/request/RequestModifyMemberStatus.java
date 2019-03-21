package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class RequestModifyMemberStatus {

    @SerializedName("member_id")
    public String memberId;
    public int status;
    @SerializedName("sp_id")
    public String spId;

}
