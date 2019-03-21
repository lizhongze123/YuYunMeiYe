package cn.yuyun.yymy.ui.me.entity;

import java.io.Serializable;

import cn.yuyun.yymy.http.result.BaseComparatorBean;

/**
 * @desc
 *
 * @author lzz
 * @date
 */

public class PeopleNumberBean extends BaseComparatorBean implements Serializable {


    /**
     * member_name : 大家家
     * member_avatar : https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/member_female/2.png
     * member_mobile : 18824183045
     * member_sex : 1
     * member_card_name :
     * sp_name : APP店
     * consumption_times_this_month : 1
     * consumption_latest_time : 2018-09-06 16:45:50
     * person_number : 1
     * record_id : CANB_611018090616440706225
     */

    private String member_name;
    private String member_avatar;
    private String member_mobile;
    private String member_sex;
    private String member_card_name;
    private String sp_name;
    private int consumption_times_this_month;
    private String consumption_latest_time;
    private int person_number;
    private String record_id;

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_avatar() {
        return member_avatar;
    }

    public void setMember_avatar(String member_avatar) {
        this.member_avatar = member_avatar;
    }

    public String getMember_mobile() {
        return member_mobile;
    }

    public void setMember_mobile(String member_mobile) {
        this.member_mobile = member_mobile;
    }

    public String getMember_sex() {
        return member_sex;
    }

    public void setMember_sex(String member_sex) {
        this.member_sex = member_sex;
    }

    public String getMember_card_name() {
        return member_card_name;
    }

    public void setMember_card_name(String member_card_name) {
        this.member_card_name = member_card_name;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public int getConsumption_times_this_month() {
        return consumption_times_this_month;
    }

    public void setConsumption_times_this_month(int consumption_times_this_month) {
        this.consumption_times_this_month = consumption_times_this_month;
    }

    public String getConsumption_latest_time() {
        return consumption_latest_time;
    }

    public void setConsumption_latest_time(String consumption_latest_time) {
        this.consumption_latest_time = consumption_latest_time;
    }

    public int getPerson_number() {
        return person_number;
    }

    public void setPerson_number(int person_number) {
        this.person_number = person_number;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }
}
