package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.SignStatus;

/**
 * @author
 * @desc
 * @date
 */

public class ResultAttendanceWithTime {


    @SerializedName("external")
    public List<AppAttendanceExternalBean> AppAttendanceExternal;

    @SerializedName("internal")
    public List<AppAttendanceInternalBean> AppAttendanceInternal;

    public static class AppAttendanceExternalBean implements Serializable{
        /**
         * id : 115
         * place : 广州市番禺区华荫东路华荟明苑
         * notes :
         * lng : 113.30363416883681
         * lat : 23.048043348524306
         * thumb_url :
         * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * create_time : 2018-03-06 10:03:29
         * create_person_name : 彭丽平
         * create_person_avatar : http://www.qqw21.com/article/uploadpic/2012-9/201291893228996.jpg
         * create_person_position : 销售
         * create_person_position_id : 171
         */

        public int id;
        public String place;
        public String notes;
        public String lng;
        public String lat;
        @SerializedName("pictures")
        public List<String> pictures;
        public String staff_id;
        public String create_time;
        public String create_person_name;
        public String create_person_avatar;
        public String create_person_position;
        public int create_person_position_id;
    }

    public static class AppAttendanceInternalBean implements Serializable{
        /**
         * id : 323
         * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * create_time : 2018-03-06 10:03:01
         * create_person_name : 彭丽平
         * create_person_avatar : http://www.qqw21.com/article/uploadpic/2012-9/201291893228996.jpg
         * create_person_position : 销售
         * create_person_position_id : 171
         */

        public SignStatus status;
        public int span_status;

        @SerializedName("datetime")
        public String dateTime;

    }
}
