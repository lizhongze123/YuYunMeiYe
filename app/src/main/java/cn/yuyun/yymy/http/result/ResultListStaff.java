package cn.yuyun.yymy.http.result;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */
public class ResultListStaff implements Serializable{

    /**
     * group_id : 17
     * staff_id : 63f042df-8bed-11e8-9e22-7cd30ae0f408
     * baseon_type : 2
     * baseon_type_desc : 门店
     * baseon_id : 439
     * baseon_id_desc : 湘西店
     * staff_number :
     * staff_name : 杨幂
     * mobile : 13685855555
     * avatar :
     * status : 1
     * sex : 1
     * position_id : 0
     * position_name : -
     * idcard :
     * mechanic : 1
     * description : null
     * cross_sp : 1
     * entry_time : 2018-07-20
     * create_time : 2018-07-20 15:20:33
     */

    public int group_id;
    public String staff_id;
    public int baseon_type;
    public String baseon_type_desc;
    public int baseon_id;
    public String baseon_id_desc;
    public String staff_number;
    public String staff_name;
    public String mobile;
    public String avatar;
    public int status;
    public Sex sex;
    public int position_id;
    public String position_name;
    public String idcard;
    public int mechanic;
    public String description;
    public int cross_sp;
    public String entry_time;
    public String create_time;


    public boolean isChecked;

    /**收银作业用*/
    public double sale;
    public double before;
    public double person_times;
}
