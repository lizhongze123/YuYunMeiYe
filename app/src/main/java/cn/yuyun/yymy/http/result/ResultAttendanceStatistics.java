package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultAttendanceStatistics implements Serializable{


    /**
     * workPersonNum : 2
     * workFullPersonNum : 0
     * comeLatePersonNum : 2
     * leaveEarlyPersonNum : 2
     * absentPersonNum : 22
     * workAttendanceRecordByDateVos : [{"baseon_id":209,"baseon_type":2,"staff_id":"1dc1bf90-5db7-11e8-948d-6c92bf31640f","create_person_name":"订单","create_person_avatar":"","ycqts_count":0,"lateCount":0,"earlyCount":0,"leaves_count":0,"neglect_work_count":8,"out_work_count":0},{"baseon_id":209,"baseon_type":2,"staff_id":"409bbfd8-5fcc-11e8-b29a-6c92bf16086d","create_person_name":"这个员工只打外勤","create_person_avatar":"","ycqts_count":1,"lateCount":1,"earlyCount":1,"leaves_count":0,"neglect_work_count":7,"out_work_count":1},{"baseon_id":209,"baseon_type":2,"staff_id":"6bbd7760-45e1-11e8-948d-6c92bf31640f","create_person_name":"测试员工","create_person_avatar":"","ycqts_count":1,"lateCount":1,"earlyCount":1,"leaves_count":0,"neglect_work_count":7,"out_work_count":1}]
     */

    public int workPersonNum;
    public int workFullPersonNum;
    public int comeLatePersonNum;
    public int leaveEarlyPersonNum;
    public int absentPersonNum;
    public int workOutPersonNum;
    public List<WorkAttendanceRecordByDateVosBean> workAttendanceRecordByDateVos;

    public static class WorkAttendanceRecordByDateVosBean {
        /**
         * baseon_id : 209
         * baseon_type : 2
         * staff_id : 1dc1bf90-5db7-11e8-948d-6c92bf31640f
         * create_person_name : 订单
         * create_person_avatar :
         * ycqts_count : 0
         * lateCount : 0
         * earlyCount : 0
         * leaves_count : 0
         * neglect_work_count : 8
         * out_work_count : 0
         */

        public String baseon_id;
        public int baseon_type;
        public String staff_id;
        public String create_person_name;
        public String rule_name;
        public String create_person_avatar;
        public int ycqts_count;
        public int lateCount;
        public int earlyCount;
        public int leaves_count;
        public int neglect_work_count;
        public int out_work_count;
    }
}
