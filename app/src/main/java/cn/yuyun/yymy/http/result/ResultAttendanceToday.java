package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.SignStatus;

/**
 * @author
 * @desc
 * @date
 */
public class ResultAttendanceToday {

    /**内勤次数*/
    public int internal_count;

    /**0：未打卡, 1：迟到， 2：正常*/
    public StatusOnType type_up;

    public String time_up;

    /**0: 没打卡状态， 1： 内勤， 2： 外勤*/
    public SignType status_up;

    /**0：未打卡, 1：早退， 2：正常*/
    public StatusOffType type_down;

    public String time_down;

    /**0: 没打卡状态， 1： 内勤， 2： 外勤*/
    public SignType status_down;

    public String head;

    public String rule_name;

    public String staff_name;

    public String rule_up;

    public String rule_down;

    public double lng;

    /**经度*/
    public double effective;

    /**纬度*/
    public double lat;

    public enum StatusOnType {

        @SerializedName("0")
        NONE("未打卡",R.color.text_orange, true),

        @SerializedName("1")
        LATE("迟到", R.color.text_red, false),

        @SerializedName("2")
        NORMAL("正常", R.color.sign_normal, true);

        public final String desc;
        public final int resId;
        public boolean isSelected;

        StatusOnType(String desc, int resId, boolean isSelected) {
            this.desc = desc;
            this.resId = resId;
            this.isSelected = isSelected;
        }

    }

    public enum StatusOffType {

        @SerializedName("0")
        NONE("未打卡",R.color.text_orange, true),

        @SerializedName("1")
        LATE("早退", R.color.text_red, false),

        @SerializedName("2")
        NORMAL("正常", R.color.text_green, true);

        public final String desc;
        public final int resId;
        public boolean isSelected;

        StatusOffType(String desc, int resId, boolean isSelected) {
            this.desc = desc;
            this.resId = resId;
            this.isSelected = isSelected;
        }

    }

    public enum SignType {
        @SerializedName("0")
        NONE("未知",R.color.text_orange, true),

        @SerializedName("1")
        IN("内勤",R.color.text_orange, true),

        @SerializedName("2")
        OUT("外勤", R.color.text_orange, false);

        public final String desc;
        public final int resId;
        public boolean isSelected;

        SignType(String desc, int resId, boolean isSelected) {
            this.desc = desc;
            this.resId = resId;
            this.isSelected = isSelected;
        }

    }

}
