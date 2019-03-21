package cn.yuyun.yymy.http.request;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestReportBusinessDetail {

    public String group_id;
    public List<String> sp_id_list;
    public long start_date;
    public long end_date;
    public int start_row;
    public int count;
    /**1日 2月 3年*/
    public int type = 1;
    /**
     * 1 成交金额 2 现金 3 刷卡 4 转账 5 微信 6 支付宝 7 储值 8 积分余额 9 免单 10 现金券 11 欠款
     * */
    public int sort;
    /**
     * 1 正序 2 倒序
     * */
    public int sort_type;

}
