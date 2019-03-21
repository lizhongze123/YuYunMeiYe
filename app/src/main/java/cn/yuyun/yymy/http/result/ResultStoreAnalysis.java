package cn.yuyun.yymy.http.result;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class ResultStoreAnalysis implements Serializable{

    public int group_id;
    public String member_id;
    public String member_name;
    public String member_avatar;
    public String member_card_name;
    public Sex member_sex;
    public String member_mobile;
    public int sp_id;
    public String sp_name;
    public double canbe_consume;
    public double storedvalue;
    public double arrears;

}
