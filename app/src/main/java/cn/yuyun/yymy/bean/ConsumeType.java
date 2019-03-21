package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc 
 * @date
 */

public enum ConsumeType {

    @SerializedName("1")
    VALUE_RECHARGE("储值充值", 1),
    @SerializedName("2")
    VALUE_REFUND("储值退款", 2),
    @SerializedName("3")
    BUY("购买",3),
    @SerializedName("4")
    GIVE("赠送",4),
    @SerializedName("5")
    REPAYMENT("还款",5),
    @SerializedName("6")
    CARD("耗卡",6),
    @SerializedName("7")
    REFUND("退款",7);

    public final String desc;
    public final int value;
    ConsumeType(String desc, int value) {
        this.desc = desc;
        this.value = value;
    }

}
