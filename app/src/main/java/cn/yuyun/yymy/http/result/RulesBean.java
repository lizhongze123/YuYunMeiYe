package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RulesBean implements Serializable{


    /**
     * id : 96
     * group_id : 17
     * name : sad as
     * start_hour : 14
     * start_min : 44
     * end_hour : 14
     * end_min : 45
     * effective : 200
     * place : 广东省广州市番禺区-洛溪(地铁站)
     * lng : 113.29867
     * lat : 23.042896
     * create_person : 124fa3555e4wq646ew554ewr54564-357556
     * create_time : 2018-03-05 18:19:25
     * attendanceOgList : [{"id":47,"og_type":2,"og_id":78,"rule_id":96,"og_name":"捌号店"},{"id":51,"og_type":2,"og_id":99,"rule_id":96,"og_name":"abc店"}]
     */

    public int id;
    public int group_id;
    public String name;

    @SerializedName("start_hour")
    public String startHour;

    @SerializedName("start_min")
    public String startMin;

    @SerializedName("end_hour")
    public String endHour;

    @SerializedName("end_min")
    public String endMin;

    public double effective;
    public String place;
    public double lng;
    public double lat;
    public String create_person;
    public String create_time;
    public List<AttendanceOgListBean> attendanceOgList;

    public static class AttendanceOgListBean {
        /**
         * id : 47
         * og_type : 2
         * og_id : 78
         * rule_id : 96
         * og_name : 捌号店
         */

        public int id;
        public int og_type;
        public int og_id;
        public int rule_id;
        public String og_name;
    }
}
