package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */
public class ResultBillManage implements Serializable{


    public boolean isSystem;

    @SerializedName("record_type")
    public int recordType;

    @SerializedName("storedvalue_id")
    public String storedvalueId;

    @SerializedName("amount_realpay")
    public double amountRealpay;
    public double current;
    public double previous;

    public int id;
    public int group_id;
    public String record_id;
    public String member_id;
    public String member_mobile;
    public String member_card;
    public String member_name;
    public Sex member_sex;
    public String member_in_sp_name;
    public int member_in_sp_id;
    public int member_level_id;
    @SerializedName(value = "member_level_name", alternate = {"level_name"})
    public String member_level_name;
    @SerializedName(value = "member_avatar", alternate = {"avatar"})
    public String member_avatar;
    public Object pos_printer_informations;
    public ConsumeType consume_type;
    public int cashier_sp_id;
    public String cashier_sp_name;
    public String consume_time;
    public String create_time;
    @SerializedName(value = "contain_type_desc", alternate = {"consume_type_contains_desc"})
    public String containTypeDesc;
//    public double consume_amount;
    public double pay_cash;
    public double pay_ali_pay;
    public double pay_wechat_pay;
    public double pay_transfer;
    public double pay_pos;
    public double refund_total;
    public double refund_realpay;
    public double refund_storedvalue;
    public String description;
    public String create_person;
    public String modify_person;
    public String modify_time;
    public int status;
    public String create_person_desc;
    public String modify_person_desc;

    public List<ResultBillManagerTypeDetail.BillAllInfoBean> detailList;

}
