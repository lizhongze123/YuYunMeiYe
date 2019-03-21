package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */

public class AppointmentBean implements Serializable {


    /**
     * appointment_start : 09:00:00
     * appointment_end : 23:00:00
     * appointment_list : [{"staff_id":"f5b0df83-6154-11e8-b29a-6c92bf16086d","staff_name":"琪琪","reservationBookRspList":[{"sp_reservation_id":"APPT_1872413550","sp_name":"泰山店","status":1,"reach_time":"","leave_time":"","mechanic_staff_id":"f5b0df83-6154-11e8-b29a-6c92bf16086d","mechanic_staff_name":"琪琪","service_id":"16939,7650","service_name":"007,123","date":"2018-07-28","start_time":"10:00:00","end_time":"11:00:00","member_id":"3bc5205a-8edc-11e8-9e22-7cd30ae0f408","member_name":"陈荷","member_avatar":"","member_sex":"1","phone":"13526368887","name":"琪琪","submit_notes":"","device_type":1,"create_time":"2018-07-24 13:55:00"}]}]
     */

    public String appointment_start;
    public String appointment_end;
    public List<AppointmentListBean> appointment_list;


    public static class AppointmentListBean implements Serializable{

        /**
         * staff_id : f5b0df83-6154-11e8-b29a-6c92bf16086d
         * staff_name : 琪琪
         * reservationBookRspList : [{"sp_reservation_id":"APPT_1872413550","sp_name":"泰山店","status":1,"reach_time":"","leave_time":"","mechanic_staff_id":"f5b0df83-6154-11e8-b29a-6c92bf16086d","mechanic_staff_name":"琪琪","service_id":"16939,7650","service_name":"007,123","date":"2018-07-28","start_time":"10:00:00","end_time":"11:00:00","member_id":"3bc5205a-8edc-11e8-9e22-7cd30ae0f408","member_name":"陈荷","member_avatar":"","member_sex":"1","phone":"13526368887","name":"琪琪","submit_notes":"","device_type":1,"create_time":"2018-07-24 13:55:00"}]
         */

        public String staff_id;
        public String staff_name;
        public List<ReservationBookRspListBean> reservationBookRspList;

        public static class ReservationBookRspListBean implements Serializable{
            /**
             * sp_reservation_id : APPT_1872413550
             * sp_name : 泰山店
             * status : 1
             * reach_time :
             * leave_time :
             * mechanic_staff_id : f5b0df83-6154-11e8-b29a-6c92bf16086d
             * mechanic_staff_name : 琪琪
             * service_id : 16939,7650
             * service_name : 007,123
             * date : 2018-07-28
             * start_time : 10:00:00
             * end_time : 11:00:00
             * member_id : 3bc5205a-8edc-11e8-9e22-7cd30ae0f408
             * member_name : 陈荷
             * member_avatar :
             * member_sex : 1
             * phone : 13526368887
             * name : 琪琪
             * submit_notes :
             * device_type : 1
             * create_time : 2018-07-24 13:55:00
             */

            public int expired;
            public String sp_reservation_id;
            public String sp_name;
            public ResultBook.AppointmentType status;
            public String reach_time;
            public String leave_time;
            public String mechanic_staff_id;
            public String mechanic_staff_name;
            public String service_id;
            public String service_name;
            public String date;
            @SerializedName("start_time")
            public String startTime;
            @SerializedName("end_time")
            public String endTime;
            public String member_id;
            public String member_name;
            public String member_avatar;
            public String member_level_name;
            public String member_sex;
            public String phone;
            public String name;
            public String submit_notes;
            public int device_type;
            public String create_time;
        }
    }
}
