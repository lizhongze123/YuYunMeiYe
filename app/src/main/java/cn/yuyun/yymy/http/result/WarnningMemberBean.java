package cn.yuyun.yymy.http.result;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class WarnningMemberBean extends BaseComparatorBean implements Serializable{

    /**
     * id : 4049
     * member_id : 103beaf5-7130-11e8-b29a-6c92bf16086d
     * consumption_latest_time : 2018-07-10 09:21:37
     * group_id : 17
     * sp_id : 209
     * sp_name : 西子湖店
     * level_id : 340
     * level_name : AA
     * member_card : null
     * name : 内部测试01
     * mobile : 111
     * avatar :
     * sex : null
     * cross_sp : null
     * cash_time : null
     * create_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * create_time : 2018-06-16 14:39:47
     * modify_person : null
     * modify_time : null
     * recomend_person : null
     * birth_year : 2018
     * birth_month : 7
     * birth_day : 4
     * status : 1
     * birthDayTillInfoRsp : {"birth_type":0,"birth_type_name":"农历","next_birth_date":"2018-07-04","birth_date_till_days":19}
     */

    public int id;
    public String member_id;
    public String consumption_latest_time;
    public int group_id;
    public int sp_id;
    public String sp_name;
    public int level_id;
    public String level_name;
    public String member_card;
    public String name;
    public String mobile;
    public String avatar;
    public Sex sex;
    public int cross_sp;
    public String cash_time;
    public String create_person;
    public String create_time;
    public String modify_person;
    public String modify_time;
    public String recomend_person;
    public int birth_year;
    public int birth_month;
    public int birth_day;
    public int status;
    public BirthDayTillInfoRspBean birthDayTillInfoRsp;

    public static class BirthDayTillInfoRspBean implements Serializable{
        /**
         * birth_type : 0
         * birth_type_name : 农历
         * next_birth_date : 2018-07-04
         * birth_date_till_days : 19
         */

        public int birth_type;
        public String birth_type_name;
        public String next_birth_date;
        public int birth_date_till_days;
    }
}
