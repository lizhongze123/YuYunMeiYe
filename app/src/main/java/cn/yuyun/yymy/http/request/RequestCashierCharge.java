package cn.yuyun.yymy.http.request;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestCashierCharge {

    /**1.充值 2.退款*/
    public int related_consumption_type = 1;
    public String cashier_sp_id;
    public String description;
    public double amount;
    public double pay_cash;
    public double pay_ali_pay;
    public double pay_wechat_pay;
    public double pay_pos;
    public double pay_transfer;
    public String member_id;
    public String consume_time;
    public List<RequestCashier.StaffPreSaleServiceRecordQoListBean> staffPreSaleServiceRecordQoList;
    public List<RequestCashier.ListStaffpersonTimesQoBean> staffPersonTimesQoList;
}
