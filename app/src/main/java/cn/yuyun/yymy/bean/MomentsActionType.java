package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */

public enum MomentsActionType {

    @SerializedName("0")
    NO,
    @SerializedName("1")
    LIKE;

    MomentsActionType() {
    }

}
