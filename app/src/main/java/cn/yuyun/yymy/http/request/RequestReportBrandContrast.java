package cn.yuyun.yymy.http.request;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestReportBrandContrast implements Serializable{

    public String group_id;
    public List<String> sp_id_list;
    public long start_date;
    public long end_date;
    public int start_row;
    public int count;
    /**
     * 1 总业绩 2 业绩占比 3 总消耗 4 消耗占比
     * */
    public int sort;
    /**
     * 1 正序 2 倒序
     * */
    public int sort_type;

}
