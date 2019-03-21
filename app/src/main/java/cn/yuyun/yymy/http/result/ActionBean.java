package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class ActionBean implements Serializable{

    /**
     * collectCount : 1
     * likeCount : 0
     * commentCount : 2
     * collectFlag : false
     * likeFlag : false
     * commentFlag : false
     * latestActivityVo : {"latestActivityId":308,"groupId":17,"title":"测试活动","content":"今天测试","pictures":["/17/activity/2018070310004735554D1FB08EFC30E3149B43CE8D9B96.jpeg"],"createPerson":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","createPersonName":"彭丽平","createPersonAvatar":"/17/avatar/staff/20180717134210.jpeg","createPersonPosition":"美容师","createTime":"2018-07-03 10:00:47","status":1}
     */

    public int collectCount;
    public int likeCount;
    public int commentCount;
    public int readCount;
    public boolean collectFlag;
    public boolean selfCreateFlag;
    public boolean likeFlag;
    public boolean commentFlag;
    public boolean readFlag;
    public LatestActivityVoBean latestActivityVo;

    public class LatestActivityVoBean implements Serializable{
        /**
         * latestActivityId : 308
         * groupId : 17
         * title : 测试活动
         * content : 今天测试
         * pictures : ["/17/activity/2018070310004735554D1FB08EFC30E3149B43CE8D9B96.jpeg"]
         * createPerson : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * createPersonName : 彭丽平
         * createPersonAvatar : /17/avatar/staff/20180717134210.jpeg
         * createPersonPosition : 美容师
         * createTime : 2018-07-03 10:00:47
         * status : 1
         */

        public int latestActivityId;
        public int groupId;
        public String title;
        public String content;
        public String createPerson;
        public String createPersonName;
        public String createPersonAvatar;
        public String createPersonPosition;
        public String createTime;
        public int status;
        public List<String> pictures;
    }
}
