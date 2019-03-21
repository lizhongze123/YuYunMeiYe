package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.File;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAddMember {

    public int supervisor;

    @SerializedName("sp_id")
    public String spId;

    public int level_id;

    public String member_card;

    public int origins;

    public String avatar;

    public String name;

    public String mobile;

    public Sex sex;

    public String member_id;

    /**1.跨店  0.不跨店*/
    public int cross_sp = 1;

    @SerializedName("cash_time")
    public long cashTime;

    public String recomend_person;

    public String birth_type = "0";

    public String birth_year = "";

    public String birth_month = "";

    public String birth_day = "";

    public String description;

}
