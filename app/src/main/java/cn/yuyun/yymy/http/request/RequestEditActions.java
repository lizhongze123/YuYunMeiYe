package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestEditActions {

    @SerializedName("activity_id")
    public int actionId;

    public String title;

    public String content;

    public List<String> pictures;

}
