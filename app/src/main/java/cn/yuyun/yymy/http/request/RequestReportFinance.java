package cn.yuyun.yymy.http.request;

import java.util.List;

/**RequestReportFinance
 * @author
 * @desc
 * @date
 */
public class RequestReportFinance {

    public String group_id;
    public List<String> sp_id_list;
    public long start_date;
    public long end_date;
    public int start_row;
    public int count;
    /**1日 2月 3年*/
    public int type = 1;
    /**
     * 1 现金 2 支付宝 3 微信 4 刷卡 5转账 6 当日实收 7 退至储值 8 当日实收 9 当日总退款
     * */
    public int sort;
    /**
     * 1 正序 2 倒序
     * */
    public int sort_type;

}
