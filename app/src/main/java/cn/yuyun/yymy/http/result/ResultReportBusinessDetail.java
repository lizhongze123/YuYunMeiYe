package cn.yuyun.yymy.http.result;

import java.util.List;

import cn.yuyun.yymy.bean.ConsumeType;

/**
 * @author
 * @desc
 * @date
 */
public class ResultReportBusinessDetail {

    public String sp_name;
    public int record_type;
    public String record_id;
    public String consume_time;
    public String member_id;
    public String member_name;
    public String member_card;
    public double transaction_price;
    public double pay_cash;
    public double pay_pos;
    public double pay_transfer;
    public double pay_wechat_pay;
    public double pay_ali_pay;
    public double storedvalue;
    public String service_desc;
    public String product_desc;
    public String package_desc;
    public double accumulate_sv;
    public double free_pay;
    public double cash_coupon_pay;
    public double arrear;
    public double amount_refund_realpay;
    public List<ConsumeType> consume_type_desc;

    /* *//**消费门店*//*
    @SerializedName(value = "cashier_sp_name", alternate = {"sp_name"})
    public String cashier_sp_name;
    *//**消费时间*//*
    public String consume_time;
    *//**消费单据*//*
    public String record_id;
    *//**会员卡号*//*
    public String member_card;
    *//**会员名称*//*
    public String member_name;
    *//**消费类型*//*
    public ConsumeType type;
    *//**项目名称*//*
    public String service_name;
    *//**套餐名称*//*
    public String package_name;
    *//**产品名称*//*
    public String product_name;
    *//**金额*//*
    public double amount_realpay;
    *//**现金*//*
    public double pay_cash;
    *//**刷卡*//*
    public double pay_pos;
    *//**转账*//*
    public double pay_transfer;
    *//**微信*//*
    public double pay_wechat_pay;
    *//**支付宝*//*
    public double pay_ali_pay;
    *//**储值*//*
    public double storedvalue_pay;
    *//**积分余额*//*
    public double accumulate_sv_pay;
    *//**免单*//*
    public double free_pay;
    *//**现金券*//*
    public double coupon_pay;
    *//**欠款*//*
    public double amount_arrear;
    *//**服务人员*//*
    public String server_name;*/
}
