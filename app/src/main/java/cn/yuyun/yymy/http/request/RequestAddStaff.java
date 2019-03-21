package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.File;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAddStaff {


    public String avatar;

    public String mobile = "";
    public Sex sex;
    @SerializedName("cross_sp")
    public int crossSp;

    @SerializedName("birth_year")
    public String birthYear = "";

    @SerializedName("birth_month")
    public String birthMonth = "";

    @SerializedName("birth_day")
    public String birthDay = "";

    @SerializedName("birth_type")
    public String birthType = "";

    public String position = "";
    public String mechanic = "";

    @SerializedName("baseon_type")
    public String baseonType = "";

    @SerializedName("baseon_id")
    public String baseonId = "";

    @SerializedName("staff_number")
    public String staffNumber = "";

    public String name = "";

    @SerializedName("entry_time")
    public long entryTime;

    @SerializedName("idcard")
    public String idCard = "";

    @SerializedName("emergency_person")
    public String emergencyPerson = "";

    @SerializedName("emergency_mobile")
    public String emergencyMobile = "";

}
