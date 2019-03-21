package cn.yuyun.yymy.ui.store.attendance;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/9/19
 */
public class RequestAttendanceStatisticsDetail implements Serializable {

    public long start;
    public long end;
    public String spId;
    public String groupId;
    /**
     * 1.出勤人数 2.迟到 3.早退 4.旷工 5.全勤 6.请假 7.外勤
     */
    public AttendanceType type;

    public enum AttendanceType {

        @SerializedName("1")
        WORK("出勤", 1),

        @SerializedName("2")
        LATE("迟到", 2),

        @SerializedName("3")
        LEAVE_EARLY("早退", 3),

        @SerializedName("4")
        ABSENCE("旷工", 4),

        @SerializedName("5")
        FULL("全勤", 5),

        @SerializedName("6")
        LEAVE("请假", 6),

        @SerializedName("7")
        WORK_OUT("外勤", 7);

        public final String desc;
        public final int value;

        AttendanceType(String desc, int value) {
            this.desc = desc;
            this.value = value;
        }

    }
}
