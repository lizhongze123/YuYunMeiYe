package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class ResultAvailableStaff {


    /**
     * staff_id : 6bbd7760-45e1-11e8-948d-6c92bf31640f
     * name : 测试员工
     * mobile : 15633300000
     * avatar : /respath/17/avatar/staff/20180514183946.jpeg
     * description :
     * appointment_available : 1
     */


    @SerializedName("staff_id")
    public String staffId;

    public String name;
    public String mobile;
    public String avatar;
    public String description;

    //1.可被预约 2.不可被预约
    @SerializedName("appointment_available")
    public int appointmentAvailable;
}
