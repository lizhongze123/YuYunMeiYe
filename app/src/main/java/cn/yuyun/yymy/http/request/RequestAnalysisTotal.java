package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAnalysisTotal {


    /**
     * start : 1512061261
     * end : 1514735999
     * sp_id_list : [33,34,35,42,45]
     * group_id : 17
     */


    public long start;
    public long end;
    public List<String> sp_id_list;

    @SerializedName("group_id")
    public String groupId;
    /**1.售前业绩  2.售前退款  3.销售业绩  4.销售退款  5.消耗业绩  6.手工费  7.人次*/
    public String sort;
    /**1.正序  2.倒序*/
    public int sort_type = 1;
    /**1.全部  2.总部  3.门店*/
    public int type = 3;
}
