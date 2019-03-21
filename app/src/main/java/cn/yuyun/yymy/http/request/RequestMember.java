package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class RequestMember {


    /**
     * cross_sp :
     * level_id :
     * search_text :
     * sex :
     * sp_id : 1
     */

    @SerializedName("sp_id_list")
    public List<String> sp_id_list;
}
