package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.yuyun.yymy.bean.ConsumeType;

/**
 * @desc
 * @author
 * @date
 */

public class PersonTimeListBean implements Serializable {

    @SerializedName("staff_name")
    public String staff_name;
    @SerializedName("member_avatar")
    public String member_avatar;

    @SerializedName("sp_name")
    public String spName;

    @SerializedName("member_name")
    public String memberName;
    @SerializedName("member_card_name")
    public String member_card_name;

    /**人次*/
    @SerializedName("person_times")
    public double personTimes;

    /**消费类型 1.储值充值 2.储值退款 3购买 4赠送 5还款 6耗卡 7退款*/
    @SerializedName("type")
    public String type;

    /**关联的消费单据*/
    @SerializedName("record_id")
    public String record_id;

    /**创建时间*/
    @SerializedName("consume_time")
    public String consumeTime;


}
