package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestConsumeBill {


    /**
     * cashier_sp_id_list : ["209"]
     * search_text :
     * search_canb :
     * start :
     * end :
     * level_id :
     * less_than :
     * greater_than :
     */

    @SerializedName("search_text")
    public String searchText;

    public String search_canb;
    public String start;
    public String end;

    @SerializedName("level_id")
    public String levelId;
    public String less_than;
    public String greater_than;

    @SerializedName("cashier_sp_id_list")
    public List<String> spIdList;
}
