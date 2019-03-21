package cn.yuyun.yymy.http.result;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Expired;

/**
 * @author
 * @desc
 * @date
 */

public class ResultProduct implements Serializable{


    /**
     * id : 55947
     * group_id : 17
     * asset_id : D101812041830180234
     * member_id : b453b9ce-f16b-11e8-aa79-7cd30ae45cee
     * member_name :
     * member_card : null
     * member_in_sp_id : null
     * member_in_sp_name : null
     * member_description : null
     * product_id : 67268
     * product_name : 禁用
     * thumb_url : https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/good/product/9.jpg
     * transaction_type : 1
     * total_num : 1
     * transaction_price : 100
     * amount_already_paid : 100
     * amount_already_refund : 0
     * amount_canbe_refund : 100
     * amount_arrear : 0
     * num_refund : 0
     * num_canbe_refund : 1
     * create_time : 2018-12-04 18:30:18
     * consume_time : 2018-12-04 18:30:17
     * create_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * create_person_name : null
     * modify_time : 2018-12-04 18:30:18
     * modify_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * status : 1
     * display_to_customer : 1
     * description :
     */

    public int id;
    public String group_id;
    public String asset_id;
    public String member_id;
    public String member_name;
    public String member_card;
    public String member_in_sp_id;
    public String member_in_sp_name;
    public String member_description;
    public int product_id;
    public String product_name;
    public String thumb_url;
    public double transaction_type;
    public int total_num;
    public double transaction_price;
    public double amount_already_paid;
    public double amount_already_refund;
    public double amount_canbe_refund;
    public double amount_arrear;
    public double num_refund;
    public double num_canbe_refund;
    public String create_time;
    public String consume_time;
    public String create_person;
    public Object create_person_name;
    public String modify_time;
    public String modify_person;
    public Expired status;
    public int display_to_customer;
    public String description;
}
