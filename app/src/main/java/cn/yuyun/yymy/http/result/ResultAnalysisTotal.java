package cn.yuyun.yymy.http.result;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class ResultAnalysisTotal implements Serializable{
    /**
     * sp_name : 2店
     * asset_type_name : 产品
     * good_brand_id : 66
     * good_brand_name : 0001
     * good_id : 1883
     * total_pay_amount : 6400
     * total_consume_amount : 0
     * total_refund_amount : 0
     */

    public String sp_name;
    public String good_brand_id;
    public String good_brand_name;
    public String good_id;

    public String member_name;
    public String member_card;
    public String consume_type;
    public String asset_type_name;
    public String good_brand;
    public String good_name;
    public double total_pay_amount;
    public double total_consume_amount;
    public double total_refund_amount;
    public String related_record_id;
    public String consume_time;

  /*  public String asset_type_name;
    public String good_brand_name;
    public String good_id;
    public String good_name;
    public double total_pay_amount;
    public double total_consume_amount;
    public String cashier_sp_id;
    public String cashier_sp_name;*/



}
