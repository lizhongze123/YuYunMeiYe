package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/9/14
 */
public class ResultTotalAmount {


    /**
     * consume_time : 2018-09-14 15:31:24
     * member_name : 冯霞
     * subordinate_sp_name : 翠萍庄园
     * cashier_sp_name : 翠萍庄园
     * cashier_type : 储值
     * amount_realpay : 2568
     * related_record_id : SI_50301809141531240119
     * avatar :
     * sex : 1
     * mobile : 13602381825
     * level_name : 会员卡
     * create_time : 2018-09-14 15:31:24
     */

    public String consume_time;
    public String mb_name;
    public String member_name;
    public String subordinate_sp_name;
    public String cashier_sp_name;
    public String mb_sp_name;
    public String cashier_type;
    public double amount_realpay;
    public double consume_amount_sp;
    public double consume_money;
    public double store_performanc_sp;
    public String related_record_id;
    public String avatar;
    public Sex sex;
    @SerializedName(value = "member_mobile", alternate = {"mobile"})
    public String member_mobile;
    @SerializedName(value = "member_level_name", alternate = {"level_name"})
    public String member_level_name;
    public String create_time;
}
