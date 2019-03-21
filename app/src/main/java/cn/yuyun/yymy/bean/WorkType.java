package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */

public enum WorkType {

    @SerializedName("1")
    DAY("日报"),
    @SerializedName("2")
    WEEK("周报"),
    @SerializedName("3")
    MONTH("月报"),
    @SerializedName("4")
    OTHER("其他");

    public final String desc;
    WorkType(String desc) {
        this.desc = desc;
    }

}
