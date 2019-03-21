package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestDelTrain {

    @SerializedName("trainInfoId")
    public List<Integer> idList;
    public int status = 0;

}
