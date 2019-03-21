package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class RequestBillManage {

    @SerializedName("search_text")
    public String searchText = "";

    @SerializedName("staff_id")
    public String staffId;

    public long start;
    public long end;

}
