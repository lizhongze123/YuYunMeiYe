package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAddAppointment {

    public String sp_id;
    @SerializedName("start_time")
    public long startTime;
    @SerializedName("end_time")
    public long endTime;

    /**记录来源 1.APP 2.web 3.微信*/
    public int device_type = 1;


    /**预约的员工id*/
    public List<String> mechanic_list;
    /**预约的会员id*/
    public String member_id;
    /**预约者的姓名*/
    public String name;
    /**预约者的手机号*/
    public String phone;
    /**预约的项目id*/
    public List<String> service_list;

    /**预约者的备注*/
    public String submit_notes;

}
