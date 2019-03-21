package cn.yuyun.yymy.ui.store.attendance;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/6/9
 */
public class AttendanceStaff implements Serializable{

    public String desc;
    public int number;
    public List<AttendanceStaffBean> attendanceStaffBeanList;

    public static class AttendanceStaffBean {
        public String name;
        public String avatar;
        public String staffId;
        public int ycqts_count;
        public int lateCount;
        public int earlyCount;
        public int leaves_count;
        public int neglect_work_count;
        public int out_work_count;
    }


}
