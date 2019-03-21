package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */

public enum ListType {

    @SerializedName("1")
    ALL(1),

    @SerializedName("2")
    MYSELF(2),

    @SerializedName("3")
    COLLECT(3);

    public int value;

    ListType(int value){
        this.value = value;
    }

}
