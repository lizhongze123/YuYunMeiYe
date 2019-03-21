package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class ResultMemberBean  extends BaseComparatorBean implements Serializable{


  /**
   * member_id : 30ecd56a-2be2-11e8-82b9-00163e0824d9
   * member_in_sp_id : 70
   * member_in_sp_name : 白云店
   * member_level_id : 324
   * member_level_name : 金卡11
   * member_level_discount : 0.88
   * member_avatar : /respath/17/avatar/member/20180504142601.jpeg
   * member_card : No.110180320
   * member_origins : 1
   * member_name : 丁香
   * member_mobile : 15324523654
   * member_sex : 1
   * member_cross_sp : 1
   * member_cash_time : 2018-03-20 09:58:31
   * member_create_time : 2018-03-20 09:58:31
   * member_recomend_person :
   * member_birth_type : 0
   * member_birth_year : 2018
   * member_birth_month : 5
   * member_birth_day : 4
   * member_status : 1
   * member_storedvalue : 0
   * member_accumulate : 0
   * member_accumulate_sv : 0
   * member_cash_coupon : 0
   * member_arrears : 8880
   * member_expenditure : 62860
   * member_consumption_times_total : 3
   * member_consumption_times_this_month : 0
   * member_consumption_latest_time : 2018-05-04 11:10:01
   * staffMgrMemberRspList : [{"id":55,"group_id":17,"staff_id":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","staff_name":"彭丽平","member_id":"30ecd56a-2be2-11e8-82b9-00163e0824d9","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","create_time":"2018-03-20 09:58:31"}]
   */

  public boolean isChecked;

  public int group_id;

  public String member_description;

  @SerializedName("member_id")
  public String memberId;

  @SerializedName("member_in_sp_id")
  public String memberInSpId;

  public String member_in_sp_name;
  public int member_level_id;

  @SerializedName("member_level_name")
  public String memberLevelName;

  public double member_level_discount;

  @SerializedName("member_avatar")
  public String memberAvatar;

  @SerializedName("member_card")
  public String memberCard;

  public int member_origins;

  @SerializedName("member_name")
  public String memberName;

  @SerializedName("member_mobile")
  public String memberMobile;

  public Sex member_sex;
  public int member_cross_sp;

  @SerializedName("member_cash_time")
  public String memberCashTime;

  public String member_create_time;
  public String member_recomend_person;
  public String member_birth_type;
  public String member_birth_year;
  public String member_birth_month;
  public String member_birth_day;
  public int member_status;

  @SerializedName("member_storedvalue")
  public double memberStoredValue;

  @SerializedName("member_accumulate")
  public double memberAccumulate;


  public double member_accumulate_sv;
  public double member_cash_coupon;

  @SerializedName("member_arrears")
  public double memberArrears;

  public double member_expenditure;
  public double member_consumption_times_total;
  public double member_consumption_times_this_month;

  public BirthDayTillInfoRspBean birthDayTillInfoRsp;

  @SerializedName("member_consumption_latest_time")
  public String memberConsumptionLatestTime;

  public List<StaffMgrMemberRspListBean> staffMgrMemberRspList;

  public static class StaffMgrMemberRspListBean implements Serializable{
    /**
     * id : 55
     * group_id : 17
     * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * staff_name : 彭丽平
     * member_id : 30ecd56a-2be2-11e8-82b9-00163e0824d9
     * create_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * create_time : 2018-03-20 09:58:31
     */

    public int id;
    public int group_id;
    public String staff_id;
    public String staff_name;
    public String member_id;
    public String create_person;
    public String create_time;
  }

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
