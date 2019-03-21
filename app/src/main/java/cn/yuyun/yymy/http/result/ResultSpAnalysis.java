package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class ResultSpAnalysis implements Serializable{

    public String date;
    public String sp_id;
    public String sp_name;
    public double person_times;

    /**实收金额*/
    @SerializedName("total_amount")
    public double totalAmount;

    /**消耗金额*/
    @SerializedName("amount_consume")
    public double amountConsume;

    /**欠款金额*/
    @SerializedName("amount_arrear")
    public double amountArrear;

    public double refund_realpay;

}
