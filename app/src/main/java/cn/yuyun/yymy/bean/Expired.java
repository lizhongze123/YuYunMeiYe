package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */
public enum Expired {

    @SerializedName("0")
    DONE("已用完", R.color.text_orange),

    @SerializedName("1")
    ING("使用中",R.color.colorPrimary);

    public final String desc;
    public final int resId;

    Expired(String desc, int resId) {
        this.desc = desc;
        this.resId = resId;
    }

}
