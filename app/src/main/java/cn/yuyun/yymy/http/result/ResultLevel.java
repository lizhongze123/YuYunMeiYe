package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */

public class ResultLevel {

    @SerializedName("level_id")
    public int levelId;
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
