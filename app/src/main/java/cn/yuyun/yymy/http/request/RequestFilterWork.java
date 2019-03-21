package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestFilterWork implements Serializable{

    /**汇报类型:1.日报 2.周报 3.月报 4.其他*/
    public int type = 1;

    @SerializedName("person_Id")
    public List<String>personId;

    @SerializedName("start_Time")
    public String startTime;

    @SerializedName("end_Time")
    public String endTime;

    public int pageIndex;

    public int pageSize;

}
