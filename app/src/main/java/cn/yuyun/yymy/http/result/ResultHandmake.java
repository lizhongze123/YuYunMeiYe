package cn.yuyun.yymy.http.result;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class ResultHandmake implements Serializable{

    public String staff_name;
    public String cashier_sp_name;
    public String member_name;
    public String good_brand;
    public String good_type;
    public double handmake;
    /**会员消耗*/
    public double consume_amount_now;
    /**员工消耗*/
    public double consume_amount_staff;
    /**门店消耗*/
    public String consume_amount_sp;
    /**会员级别*/
    public String level_name;
    public String good_name;
    public String consume_time;
    public String related_record_id;
    public String related_consumption_id;
    public String staff_subordinate_sp_name;
    public String member_subordinate_sp_name;
    public int consumeNum;
    public double serveTime;
    public String create_time;
    public String member_avatar;

}
