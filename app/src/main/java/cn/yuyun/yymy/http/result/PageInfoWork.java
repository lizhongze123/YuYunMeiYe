package cn.yuyun.yymy.http.result;

import com.example.http.PageInfo;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class PageInfoWork {


    public PageInfo<ResultWork2> workReportVoPage;
    public List<WorkReportReadCountListVoListBean> workReportReadCountListVoList;


    public static class WorkReportReadCountListVoListBean {
        /**
         * staff_name : 彭丽平
         * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * staff_avatar : /17/avatar/staff/20180725154621.jpeg
         * content : 小明
         * create_time : null
         * report : null
         */

        public String staff_name;
        public String staff_id;
        public String staff_avatar;
        public String content;
        public String create_time;
        public String report;
    }
}
