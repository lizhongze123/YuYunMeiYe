package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/30
 */

public class RequestStoreStaffList {

    public String search_text = "";

    public int baseon_type ;
    public int status = 1;
    @SerializedName("sp_id_list")
    public List<String> spIdList;

}
