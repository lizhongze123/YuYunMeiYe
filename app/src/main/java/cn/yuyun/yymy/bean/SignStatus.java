package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */

public enum SignStatus {

    @SerializedName("1")
    NORMAL("正常",R.color.text_green, true),

    @SerializedName("2")
    LATE("迟到",R.color.text_red, false),

    @SerializedName("3")
    LEAVE("早退",R.color.text_red, false);

    public final String desc;
    public final int resId;
    public boolean isSelected;

    SignStatus(String desc, int resId, boolean isSelected) {
        this.desc = desc;
        this.resId = resId;
        this.isSelected = isSelected;
    }

}
