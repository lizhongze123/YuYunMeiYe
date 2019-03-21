package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestStoreAnalysis {

    //    public long start_date;
//    public long end_date;
    public List<String> sp_id_list;
    @SerializedName("group_id")
    public String groupId;
    /**1.累未耗 2储值 3欠款*/
    public int type;
}
