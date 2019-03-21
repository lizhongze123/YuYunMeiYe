package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestStoreOutput {

    public String group_id;
    public String og_id;
    public int og_type;

    @SerializedName("sh_id_list")
    public List<String> sp_id_list;

    public long start_date;
    public long end_date;
    public int start_row;
    public int count;

    /**1.总部出库表  2.门店销售表*/
    public int type = 2;
    /**是否导出 1导出 否则不导*/
    public int export;
    /**
     * 1.
     * 1 员工价 2 门店价 3 指导价 4 实售价 5 出库数量 6 门店总额
     *
     *
     * */
    public int sort;
    /**排序类型 1 正序 2 倒序*/
    public int sort_type;

    public String out_store_type;
}
