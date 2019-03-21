package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class ResultReport {


    /**
     * cashier_sp_id : 69
     * cashier_sp_name : 湖海店
     * amount_total : 1200
     * amount_cash_amount : 1000
     * amount_ali_pay : 200
     * amount_wechat_pay : 0
     * amount_transfer : 0
     * amount_pos : 0
     * refund_amount_to_storedvalue : 0
     * refund_amount_realpay : 200
     */

    public int cashier_sp_id;
    public String cashier_sp_name;

    @SerializedName("amount_total")
    public double amountTotal;

    @SerializedName("amount_cash_amount")
    public double amountCashAmount;

    @SerializedName("amount_ali_pay")
    public double amountAlipay;

    @SerializedName("amount_wechat_pay")
    public double amountWechatPay;

    @SerializedName("amount_transfer")
    public double amountTransfer;

    @SerializedName("amount_pos")
    public double amountPos;

    @SerializedName("refund_amount_to_storedvalue")
    public double refundAmountToStoredvalue;

    @SerializedName("refund_amount_realpay")
    public double refundAmountRealpay;

    @SerializedName("refund_amount_count")
    public double refundAmountCount;
}
