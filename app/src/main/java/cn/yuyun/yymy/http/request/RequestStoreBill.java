package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/6/9
 */
public class RequestStoreBill {

    @SerializedName("search_text")
    public String searchText = "";

    @SerializedName("cashier_sp_id_list")
    public List<String> spId;

    @SerializedName("greater_than")
    public String gerate_than;

    @SerializedName("less_than")
    public String less_than;

    public String level_id;

    @SerializedName("member_id")
    public String memberId;

    public long start;
    public long end;

}
