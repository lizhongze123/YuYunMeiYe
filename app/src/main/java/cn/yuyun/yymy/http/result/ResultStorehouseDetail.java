package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class ResultStorehouseDetail {


    /**
     * order_id : DSP1853152455
     * date : 2018-05-03 15:24:53
     * type : 发货
     * sh_name : 开发专用
     * product_name : 环保产品
     * amount : 300
     * guideprice : -1500
     * purchaseprice : 1500
     * numbers : -5
     * description :
     */

    @SerializedName("order_id")
    public String orderId;
    public int product_id;

    public String date;
    public String type;
    public String sh_name;
    public String product_name;
    public double amount;
    public double guideprice;
    public double purchaseprice;
    public int numbers;
    public String description;
}
