package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestProject {

    @SerializedName("sp_id_list")
    public List<String> spIdList;

    public String search_text;

    /**1产品  2项目  3套餐*/
    public int good_type = 2;
    public int status = 1;

}
