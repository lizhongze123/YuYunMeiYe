package cn.yuyun.yymy.http.result;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class ResultStaffAnalysis implements Serializable{

    public String sp_id;
    public String sp_name;
    public String staff_id;
    public String staff_name;
    public String staff_avatar;
    public String staff_position_name;
    public Sex staff_sex;
    public double presale;
    public double presale_refund;
    public double sale;
    public double sale_refund;
    public double consume;
    public double consume_count;
    public double handmake;
    public double person_times;
    public int person_number;

}
