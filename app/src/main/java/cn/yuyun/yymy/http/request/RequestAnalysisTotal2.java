package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAnalysisTotal2 {

    /**
     * start : 1512061261
     * end : 1514735999
     * sp_id_list : [33,34,35,42,45]
     * group_id : 17
     */

    public long start_date;
    public long end_date;
    public List<String> sp_id_list;

    @SerializedName("group_id")
    public String groupId;
}
