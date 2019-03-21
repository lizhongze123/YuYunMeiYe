package cn.yuyun.yymy.http.result;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class ResultMyMemberBean  extends BaseComparatorBean implements Serializable{


    /**
     * id : 3
     * member_id : f213a979-ec73-11e7-9a86-00163e0824d9
     * consumption_latest_time : null
     * group_id : 17
     * sp_id : null
     * sp_name : null
     * level_id : null
     * level_name : null
     * member_card : null
     * name : 他天天
     * mobile : null
     * avatar : null
     * sex : null
     * cross_sp : null
     * cash_time : null
     * create_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * create_time : 2018-03-17 21:14:56
     * modify_person : null
     * modify_time : null
     * recomend_person : null
     * birth_year : null
     * birth_month : null
     * birth_day : null
     * status : null
     */

    public int id;
    public String member_id;
    public String consumption_latest_time;
    public int group_id;
    public String sp_id;
    public String sp_name;
    public String level_id;
    public String level_name;
    public String member_card;
    public String name;
    public String mobile;
    public String avatar;
    public Sex sex;
    public Object cross_sp;
    public Object cash_time;
    public String create_person;
    public String create_time;
    public Object modify_person;
    public Object modify_time;
    public Object recomend_person;
    public String birth_year;
    public String birth_month;
    public String birth_day;
    public Object status;
}
