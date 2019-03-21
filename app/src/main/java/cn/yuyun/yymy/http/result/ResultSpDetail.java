package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class ResultSpDetail {

    /**
     * storedvalue : 1.0689391342E8
     * arrears : 943258.2
     * canbe_consume : 1.5597155898E8
     * total_amount : 1.816671683E7
     * add_member : 335
     * person_times : 560
     * amount_consume : 909157.94
     * person_number : 271
     * record_number : 1701
     * amount_total : 1.455284573E7
     * amount_pos : 533023.1
     * amount_transfer : 799749.4
     * amount_wechat_pay : 1351910.25
     * amount_ali_pay : 1218187.65
     */

    public double storedvalue;
    public double arrears;
    public double canbe_consume;

    @SerializedName("total_amount")
    public double totalAmount;

    public int add_member;
    public double person_times;

    @SerializedName("amount_consume")
    public double amountConsume;

    @SerializedName("store_performance")
    public double storePerformance;

    public double person_number;
    public double record_number;
    public double amount_total;
    public double amount_pos;
    public double amount_transfer;
    public double amount_wechat_pay;
    public double amount_ali_pay;
    public int service_numbers;
}
