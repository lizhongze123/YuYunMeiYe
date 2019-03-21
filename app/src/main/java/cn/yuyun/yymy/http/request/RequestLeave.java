package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.bean.LeaveReason;

/**
 * @author
 * @desc
 * @date
 */

public class RequestLeave implements Serializable{


    /**请假内容*/
    @SerializedName("content")
    public String content;

    /**请假类型*/
    @SerializedName("reason")
    public LeaveReason reason;

    /**请假开始时间*/
    @SerializedName("start_leave")
    public long startTime;

    /**请假结束时间*/
    @SerializedName("end_leave")
    public long endTime;

    /**1 上午 2 下午*/
    @SerializedName("start_span")
    public int startSpan;

    /**1 上午 2 下午*/
    @SerializedName("end_span")
    public int endSpan;

    /**请假时长*/
    @SerializedName("times_long")
    public String timesLong;

    public List<String> imgUrls;

    /**审批人*/
    @SerializedName("list_confirm_person")
    public String approvePeopleBeanList;

    public static class ApprovePeopleBean implements Serializable{

        /**审批人id*/
        @SerializedName("approve_person")
        public String approvePerson;

    }

}
