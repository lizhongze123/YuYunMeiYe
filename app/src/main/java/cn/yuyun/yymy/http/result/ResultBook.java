package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */
public class ResultBook implements Serializable{

    /**
     * id : 1855
     * sp_id : 209
     * sp_name : 开发专用
     * mechanic_name : 测试员工
     * mechanic_staff_id : 6bbd7760-45e1-11e8-948d-6c92bf31640f
     * service_id : 16050,
     * service_name : dgdg,
     * date : 2018-06-02
     * start_time : 10:30:00
     * end_time : 11:00:00
     * member_id : 8e2ab990-6240-11e8-b29a-6c92bf16086d
     * member_name : null
     * phone : 18529446161
     * name : 唐纳德啊
     * submit_notes :
     * status : 1
     */

    public int expired;
    public String id;
    public int sp_id;

    @SerializedName("sp_name")
    public String spName;

    @SerializedName("mechanic_name")
    public String mechanicName;

    @SerializedName("mechanic_staff_name")
    public String mechanicStaffName;

    public String mechanic_staff_id;
    public String service_id;

    @SerializedName("service_name")
    public String serviceName;
    public String date;

    @SerializedName("start_time")
    public String startTime;

    @SerializedName("end_time")
    public String endTime;
    public String member_id;
    public String create_time;

    @SerializedName("member_name")
    public String memberName;

    @SerializedName("member_avatar")
    public String memberAvatar;
    @SerializedName("member_sex")
    public Sex memberSex;
    public String phone;
    public String name;
    public String submit_notes;
    public String member_level_name;

    public String sp_thumb_url;
    public String sp_addr;

    public AppointmentType status;

    public enum AppointmentType {
        @SerializedName("0")
        REVIEWING("待审核", R.color.text_gray, R.drawable.approval_ing),

        @SerializedName("1")
        AGREE("已通过", R.color.text_green, R.drawable.approval_agree),

        @SerializedName("2")
        ARRIVAL("到店", R.color.text_gray, R.drawable.approval_arrival),

        @SerializedName("3")
        END("结束预约", R.color.text_gray, R.drawable.approval_finish),

        @SerializedName("-2")
        CANCEL("已取消", R.color.text_gray, R.drawable.approval_cancel),

        @SerializedName("-1")
        REFUSED("已拒绝", R.color.text_red, R.drawable.approval_refused);

        public String desc;
        public int resId;
        public int imgId;

        AppointmentType(String desc, int resId, int imgId) {
            this.desc = desc;
            this.resId = resId;
            this.imgId = imgId;
        }
    }

}
