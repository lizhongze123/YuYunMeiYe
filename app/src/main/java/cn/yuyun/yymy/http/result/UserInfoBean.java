package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.bean.StaffStatus;
import cn.yuyun.yymy.http.ErrorResult;


/**
 * @author
 * @desc
 * @date
 */

public class UserInfoBean implements Serializable {

    /**
     * 员工id
     */
    @SerializedName("staff_id")
    public String staffId;
    /**
     * 员工工号
     */
    @SerializedName("staff_number")
    public String staffNumber;
    /**
     * 员工名称
     */
    @SerializedName("staff_name")
    public String staffName;
    /**
     * 员工手机号
     */
    @SerializedName("mobile")
    public String mobile;
    /**
     * 员工头像
     */
    @SerializedName("avatar")
    public String avatar;
    /**
     * 员工状态
     */
    @SerializedName("status")
    public StaffStatus staffStatus;
    /**
     * 员工性别
     */
    @SerializedName("sex")
    public Sex sex;

    /**
     * 员工职务id
     */
    @SerializedName("position_id")
    public String positionId;
    /**
     * 员工职务名称
     */
    @SerializedName("position_name")
    public String positionName;

    @SerializedName("baseon_type")
    public String baseonType;

    @SerializedName("baseon_id")
    public String baseonId;

    @SerializedName("baseon_type_desc")
    public String baseonTypeDesc;

    @SerializedName("baseon_id_desc")
    public String baseonIdDesc;

    /**是否技师 0否  1是*/
    @SerializedName("mechanic")
    public int mechanic;

}
