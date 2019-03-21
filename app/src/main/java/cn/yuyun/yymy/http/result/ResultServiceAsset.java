package cn.yuyun.yymy.http.result;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Expired;

/**
 * @author
 * @desc
 * @date
 */

public class ResultServiceAsset implements Serializable{

    public int id;
    public String group_id;
    public String asset_id;
    public String member_id;
    public String member_name;
    public String member_description;
    public String member_card;
    public String member_in_sp_id;
    public String member_in_sp_name;
    public int service_id;
    public String service_name;
    public String thumb_url;
    public int transaction_type;
    public int total_num;
    public double transaction_price;
    public double amount_already_paid;
    public double amount_already_refund;
    public double amount_already_used;
    public double amount_still_here;
    public double amount_canbe_refund;
    public double amount_arrear;
    public int num_consume;
    public int num_refund;
    public double num_frozen;
    public int num_canbe_used;
    public double num_canbe_refund;
    public String create_time;
    public String consume_time;
    public String create_person;
    public String create_person_name;
    public String modify_time;
    public String modify_person;
    public int status;
    public int display_to_customer;
    public String description;
    public Expired expired;
    public String dead_line;


}
