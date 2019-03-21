package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/25
 */
public class RequestStock {

    /**
     * sh_id_list : [20]
     * start_row : 1
     * count : 10
     * search_text :
     */

    @SerializedName("start_row")
    public int startRow;

    public int count;

    public String hq_id;

    @SerializedName("search_text")
    public String searchText;

    @SerializedName("sh_id_list")
    public List<String> shIdList;
}
