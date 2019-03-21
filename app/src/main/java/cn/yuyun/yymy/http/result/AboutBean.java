package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class AboutBean implements Serializable{


    /**
     * id : 67
     * group_id : 17
     * url: "http://baidu.com"
     */

    public int id;
    public int group_id;

    @SerializedName("url")
    public String url;

}
