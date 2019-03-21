package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class ResultStoredvalueBean {

    private String storedvalue_id;
    private String member_id;
    private String consume_time;
    private int status;
    private int cashier_sp_id;
    private String related_consumption_id;
    private StoredvalueType related_consumption_type;
    private double current;
    private double previous;
    private double pay_cash;
    private double pay_ali_pay;
    private double pay_wechat_pay;
    private double pay_pos;
    private double pay_transfer;
    private String create_time;
    private String create_person;
    private String description;

    public String getStoredvalue_id() {
        return storedvalue_id;
    }

    public void setStoredvalue_id(String storedvalue_id) {
        this.storedvalue_id = storedvalue_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getConsume_time() {
        return consume_time;
    }

    public void setConsume_time(String consume_time) {
        this.consume_time = consume_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCashier_sp_id() {
        return cashier_sp_id;
    }

    public void setCashier_sp_id(int cashier_sp_id) {
        this.cashier_sp_id = cashier_sp_id;
    }

    public String getRelated_consumption_id() {
        return related_consumption_id;
    }

    public void setRelated_consumption_id(String related_consumption_id) {
        this.related_consumption_id = related_consumption_id;
    }

    public StoredvalueType getRelated_consumption_type() {
        return related_consumption_type;
    }

    public void setRelated_consumption_type(StoredvalueType related_consumption_type) {
        this.related_consumption_type = related_consumption_type;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getPrevious() {
        return previous;
    }

    public void setPrevious(double previous) {
        this.previous = previous;
    }

    public double getPay_cash() {
        return pay_cash;
    }

    public void setPay_cash(double pay_cash) {
        this.pay_cash = pay_cash;
    }

    public double getPay_ali_pay() {
        return pay_ali_pay;
    }

    public void setPay_ali_pay(double pay_ali_pay) {
        this.pay_ali_pay = pay_ali_pay;
    }

    public double getPay_wechat_pay() {
        return pay_wechat_pay;
    }

    public void setPay_wechat_pay(double pay_wechat_pay) {
        this.pay_wechat_pay = pay_wechat_pay;
    }

    public double getPay_pos() {
        return pay_pos;
    }

    public void setPay_pos(double pay_pos) {
        this.pay_pos = pay_pos;
    }

    public double getPay_transfer() {
        return pay_transfer;
    }

    public void setPay_transfer(double pay_transfer) {
        this.pay_transfer = pay_transfer;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_person() {
        return create_person;
    }

    public void setCreate_person(String create_person) {
        this.create_person = create_person;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public enum StoredvalueType {

        @SerializedName("1")
        VALUE_RECHARGE("储值充值", 1),
        @SerializedName("2")
        VALUE_REFUND("储值退款", 2),
        @SerializedName("3")
        VALUE_BUY("储值购买",3),
        @SerializedName("5")
        VALUE_REPAYMENT("储值还款",5),
        @SerializedName("7")
        VALUE_REFUND_TWO("退款到储值",7);

        public final String desc;
        public final int value;
        StoredvalueType(String desc, int value) {
            this.desc = desc;
            this.value = value;
        }

    }

}
