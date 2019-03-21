package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestBook {


    /**
     * group_id : 17
     * sp_id_list : ["209"]
     * start_date : 1527782400
     * end_date : 1528070400
     * seach_text :
     * status :
     */

    @SerializedName("group_id")
    public String groupId;

    @SerializedName("start_date")
    public long startDate;

    @SerializedName("end_date")
    public long endDate;

    @SerializedName("seach_text")
    public String seachText;
    public String status;

    @SerializedName("sp_id_list")
    public List<String> spIdList;
}
