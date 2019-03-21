package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.LeaveReason;

/**
 * @author
 * @desc
 * @date
 */

public class LeaveBean implements Serializable{

    public int id;

    public LeaveReason reason;

    public String content;

    @SerializedName("start_leave")
    public String startTime;

    @SerializedName("end_leave")
    public String endTime;

    @SerializedName("start_span")
    public int startSpan;

    @SerializedName("end_span")
    public int endSpan;

    @SerializedName("times_long")
    public double timesLong;

    @SerializedName("img_url")
    public String imgUrl;

    @SerializedName("imgUrls")
    public List<String> imgUrls;

    @SerializedName("create_person")
    public String create_person;

    public String createPersonName;
    public String createPersonAvatar;
    public String createPersonPosition;

    public LeaveStatus approveStatus;
    public String create_time;

    public enum LeaveStatus{

        @SerializedName("0")
        WAITTING("等待审批",R.color.approve_gray, R.drawable.approval_ing),

        @SerializedName("1")
        AGREE("同意",R.color.approve_green, R.drawable.approval_agree),

        @SerializedName("2")
        REFUESD("拒绝", R.color.approve_red, R.drawable.approval_refused);

        public String desc;
        public int resId;
        public int bgId;

        LeaveStatus(String desc, int resId, int bgId) {
            this.desc = desc;
            this.resId = resId;
            this.bgId = bgId;
        }
    }
}
