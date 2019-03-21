package cn.yuyun.yymy.http.result;

/**
 * @author
 * @desc
 * @date
 */
public class ResultWorkCommentNotice {


    /**
     * staff_name : 彭丽平
     * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * staff_avatar : /17/avatar/staff/20180725154621.jpeg
     * content : 小可爱
     * report : {"workReportId":1390,"type":1,"staffId":"18be23da-8fff-11e8-9e22-7cd30ae0f408","staff_name":"😘😍爱情","content":"研发成本呼呼","status":1,"createTime":"2018-11-24 10:48:28","date":null,"baseonType":2,"baseonId":499,"pictures":"","select_all_people":0,"group_id":null}
     */

    public String staff_name;
    public String staff_id;
    public String staff_avatar;
    public String content;
    public String create_time;
    public ReportBean report;

    public static class ReportBean {
        /**
         * workReportId : 1390
         * type : 1
         * staffId : 18be23da-8fff-11e8-9e22-7cd30ae0f408
         * staff_name : 😘😍爱情
         * content : 研发成本呼呼
         * status : 1
         * createTime : 2018-11-24 10:48:28
         * date : null
         * baseonType : 2
         * baseonId : 499
         * pictures :
         * select_all_people : 0
         * group_id : null
         */

        public int workReportId;
        public int type;
        public String staffId;
        public String staff_name;
        public String content;
        public int status;
        public String createTime;
        public Object date;
        public int baseonType;
        public int baseonId;
        public String pictures;
        public int select_all_people;
        public String group_id;
    }
}
