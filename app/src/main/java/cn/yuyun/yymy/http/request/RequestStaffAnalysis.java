package cn.yuyun.yymy.http.request;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestStaffAnalysis {

    public String group_id;
    public String baseon_type;
    public List<String> sp_id_list;
    public long start_date;
    public long end_date;


    /**
     *
     * 1 售前业绩 2 售前退款金额 3 销售金额 4 销售退款金额
     * 5.消耗金额  6.手工费  7.人数  8.人次  9.项目数
     *
     * */
    public int sort;
    /**排序类型 1 正序 2 倒序*/
    public int sort_type;

}
