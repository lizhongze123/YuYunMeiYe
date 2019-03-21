package cn.yuyun.yymy.http.result;

/**
 * @author
 * @desc
 * @date
 */
public class ResultReportFinance {

    /**门店名称*/
    public String cashier_sp_name;

    /**现金支付总额*/
    public double amount_cash_amount;

    /**支付宝支付总额*/
    public double amount_ali_pay;

    /**微信支付总额*/
    public double amount_wechat_pay;

    /**刷卡支付总额*/
    public double amount_pos;

    /**转账支付总额*/
    public double amount_transfer;

    /**总计实收*/
    public double amount_total;

    /**退至储值总额*/
    public double refund_amount_to_storedvalue;

    /**实退总额*/
    public double refund_amount_realpay;

    /**总计退款*/
    public double refund_amount_realpay_count;
    /**当日支出*/
    public double expense;

}
