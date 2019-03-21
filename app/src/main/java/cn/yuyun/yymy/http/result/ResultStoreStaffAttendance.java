package cn.yuyun.yymy.http.result;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultStoreStaffAttendance {


    /**
     * appStaffDayAttenVos : null
     * workNum : 13
     * workOutNum : 1
     * comeLateNum : 10
     * leaveEarlyNum : 3
     * absentNum : 4
     * restNum : 0
     * earlyList : [{"date":"2018-09-01","week":"星期六","desc":"迟到613分钟"},{"date":"2018-09-03","week":"星期一","desc":"迟到48分钟"},{"date":"2018-09-04","week":"星期二","desc":"迟到55分钟"},{"date":"2018-09-06","week":"星期四","desc":"迟到5分钟"},{"date":"2018-09-07","week":"星期五","desc":"迟到649分钟"},{"date":"2018-09-12","week":"星期三","desc":"迟到1分钟"},{"date":"2018-09-13","week":"星期四","desc":"迟到1分钟"},{"date":"2018-09-14","week":"星期五","desc":"迟到618分钟"},{"date":"2018-09-17","week":"星期一","desc":"迟到21分钟"}]
     * leaveEarlyList : [{"date":"2018-09-02","week":"星期七","desc":"未打卡"},{"date":"2018-09-03","week":"星期一","desc":"早退147分钟"},{"date":"2018-09-17","week":"星期一","desc":"未打卡"}]
     * absentList : ["2018-09-09","2018-09-10","2018-09-11","2018-09-16"]
     * workList : ["2018-09-01","2018-09-02","2018-09-03","2018-09-04","2018-09-05","2018-09-06","2018-09-07","2018-09-08","2018-09-12","2018-09-13","2018-09-14","2018-09-15","2018-09-17"]
     * workOutList : ["2018-09-02"]
     * restList : []
     */

    private Object appStaffDayAttenVos;
    private int workNum;
    private int workOutNum;
    private int comeLateNum;
    private int leaveEarlyNum;
    private int absentNum;
    private int restNum;
    private List<EarlyListBean> earlyList;
    private List<LeaveEarlyListBean> leaveEarlyList;
    private List<String> absentList;
    private List<String> workList;
    private List<String> workOutList;
    private List<String> restList;

    public Object getAppStaffDayAttenVos() {
        return appStaffDayAttenVos;
    }

    public void setAppStaffDayAttenVos(Object appStaffDayAttenVos) {
        this.appStaffDayAttenVos = appStaffDayAttenVos;
    }

    public int getWorkNum() {
        return workNum;
    }

    public void setWorkNum(int workNum) {
        this.workNum = workNum;
    }

    public int getWorkOutNum() {
        return workOutNum;
    }

    public void setWorkOutNum(int workOutNum) {
        this.workOutNum = workOutNum;
    }

    public int getComeLateNum() {
        return comeLateNum;
    }

    public void setComeLateNum(int comeLateNum) {
        this.comeLateNum = comeLateNum;
    }

    public int getLeaveEarlyNum() {
        return leaveEarlyNum;
    }

    public void setLeaveEarlyNum(int leaveEarlyNum) {
        this.leaveEarlyNum = leaveEarlyNum;
    }

    public int getAbsentNum() {
        return absentNum;
    }

    public void setAbsentNum(int absentNum) {
        this.absentNum = absentNum;
    }

    public int getRestNum() {
        return restNum;
    }

    public void setRestNum(int restNum) {
        this.restNum = restNum;
    }

    public List<EarlyListBean> getEarlyList() {
        return earlyList;
    }

    public void setEarlyList(List<EarlyListBean> earlyList) {
        this.earlyList = earlyList;
    }

    public List<LeaveEarlyListBean> getLeaveEarlyList() {
        return leaveEarlyList;
    }

    public void setLeaveEarlyList(List<LeaveEarlyListBean> leaveEarlyList) {
        this.leaveEarlyList = leaveEarlyList;
    }

    public List<String> getAbsentList() {
        return absentList;
    }

    public void setAbsentList(List<String> absentList) {
        this.absentList = absentList;
    }

    public List<String> getWorkList() {
        return workList;
    }

    public void setWorkList(List<String> workList) {
        this.workList = workList;
    }

    public List<String> getWorkOutList() {
        return workOutList;
    }

    public void setWorkOutList(List<String> workOutList) {
        this.workOutList = workOutList;
    }

    public List<String> getRestList() {
        return restList;
    }

    public void setRestList(List<String> restList) {
        this.restList = restList;
    }

    public static class EarlyListBean {
        /**
         * date : 2018-09-01
         * week : 星期六
         * desc : 迟到613分钟
         */

        private String date;
        private String week;
        private String desc;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class LeaveEarlyListBean {
        /**
         * date : 2018-09-02
         * week : 星期七
         * desc : 未打卡
         */

        private String date;
        private String week;
        private String desc;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
