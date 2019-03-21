package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.http.request.RequestStoreStaff;

/**
 * @author
 * @desc
 * @date
 */

public class StaffBean extends BaseComparatorBean implements Serializable{


    /**
     * staff_id : 91807c4c-08ad-11e8-9a86-00163e0824d9
     * baseon_type : 2
     * baseon_type_desc : 门店
     * baseon_id : 68
     * baseon_id_desc : 1店
     * staff_number :
     * staff_name : 静静
     * mobile : 18027291625
     * avatar :
     * status : 1
     * sex : 1
     * position_id : 0
     * position_name : 无
     * cross_sp : 0
     * entry_time : 2018-02-03
     * create_time : 2018-02-03 14:43:39
     */

    @SerializedName("staff_id")
    public String staffId;

    @SerializedName("group_id")
    public String groupId;

    public String baseon_type;
    public String baseon_type_desc;
    public String baseon_id;

    @SerializedName("baseon_id_desc")
    public String baseonIdDesc;

    public String staff_number;

    @SerializedName("staff_name")
    public String staffName;

    public String name;

    public String mobile;
    public String avatar;
    public RequestStoreStaff.JobStatus status;
    public Sex sex;
    public String position_id;

    @SerializedName("position_name")
    public String positionName;

    public int cross_sp;
    @SerializedName("entry_time")
    public String entryTime;
    public String create_time;

    public int mechanic;

    public boolean isChecked;

    public int birth_type;
    public int birth_year;
    public int birth_month;
    public int birth_day;

    public WarnningMemberBean.BirthDayTillInfoRspBean birthDayTillInfoRsp;
}
