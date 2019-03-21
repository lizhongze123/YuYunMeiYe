package cn.yuyun.yymy.http.request;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.RecordType;

/**
 * @author
 * @desc
 * @date
 */
public class RequestCashier implements Serializable{

    public String cashier_sp;
    public String member_id;
    public String description;
    /**消费类型 1正常  2补单*/
    public int consume_type = 1;
    /**消费日期*/
    public long consume_time;
    public double pay_total;
    public double pay_cash;
    public double pay_ali_pay;
    public double pay_wechat_pay;
    public double pay_transfer;
    public double pay_pos;
    public double refund_cash;
    public double refund_storedvalue;
    public String cash_code;
    public List<ListCbBuyQoBean> list_cb_buyQo;
    public List<?> list_cb_giveawayQo;
    public List<?> list_cb_repaymentQo;
    public List<?> list_cb_consumeQo;
    public List<ListStaffpersonTimesQoBean> list_staffperson_timesQo;

    public static class ListCbConsumeQoBean implements Serializable{
        public String asset_id;
        public int asset_type;
        public double consume_amount_now;
        public int consume_time_now;
        public int consume_num_now;
        public List<StaffPreSaleServiceRecordQoListBean> staffPreSaleServiceRecordQoList;
    }

    public static class ListCbBuyQoBean implements Serializable{

        public String good_id;
        public int asset_type;
        public double consume_amount_now;
        public Object dead_line;
        public double consume_time_now;
        public double consume_num_now;
        public int total_num;
        public double transaction_price;
        public double amount_realpay;
        public double storedvalue_pay;
        public double free_pay;
        public double accumulate_sv_pay;
        public double coupon_pay;
        public double arrear_pay;
        public List<StaffPreSaleServiceRecordQoListBean> staffPreSaleServiceRecordQoList;
        public List<?> coupon_list;

    }

    public static class ListStaffpersonTimesQoBean implements Serializable{
        public String staff_id;
        public double person_times;
    }

    public static class StaffPreSaleServiceRecordQoListBean implements Serializable{
        public RecordType sale_type;
        public String staff_id;
        public double achieve_amount;
        public String person_times = "";
        public double handmake;
    }
}
