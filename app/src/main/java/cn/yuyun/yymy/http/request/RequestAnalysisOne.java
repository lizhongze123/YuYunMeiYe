package cn.yuyun.yymy.http.request;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestAnalysisOne implements Serializable{

    public String group_id;
    public List<String> sp_id_list;
    public long start_date;
    public long end_date;
    public int start_row;
    public int count;
    /**是否导出 1导出 否则不导*/
    public int export;
    /**
     * 1.
     * 1 品项实收 2 品项成交 3 品项业绩 4 品项消耗
     *
     *
     * */
    public int sort;
    /**排序类型 1 正序 2 倒序*/
    public int sort_type;

    public String search_brand_name;

    public String good_brand_id;
    public String good_brand_name;
    public String good_id;

}
