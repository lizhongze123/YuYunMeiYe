package cn.yuyun.yymy.http.result;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultTrainDetail {


    /**
     * appTrainVo : {"id":201,"group_id":17,"title":"哦哦哦","content":"嗯嗯嗯","thumb_url":"/respath/17/train/20180620151346CCB80011D6BB28069A878F3A24FE9159.jpeg","create_person":"c784f26a-6afc-11e8-b29a-6c92bf16086d","create_person_name":"南村陈奕迅","create_person_avatar":"/respath/17/avatar/staff/20180619134458.jpeg","create_person_position":"美容师","create_person_position_id":2,"create_time":"2018-06-20 15:13:46","status":1,"collect_number":1,"like_number":0,"comment_number":1,"like_myself":0,"collect_myself":1,"haveRead":null}
     * trainDetailCommentVos : [{"groupId":17,"trainId":201,"commentContent":"莫总","createPerson":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","createPersonName":"南村陈奕迅","createPersonAvatar":"/respath/17/avatar/staff/20180505101400.jpeg","createPersonPosition":"美容师","createPersonPositionId":"2","createTime":"2018-06-21 19:41:41"}]
     */

    public AppTrainVoBean appTrainVo;
    public List<TrainDetailCommentVosBean> trainDetailCommentVos;

    public static class AppTrainVoBean {
        /**
         * id : 201
         * group_id : 17
         * title : 哦哦哦
         * content : 嗯嗯嗯
         * thumb_url : /respath/17/train/20180620151346CCB80011D6BB28069A878F3A24FE9159.jpeg
         * create_person : c784f26a-6afc-11e8-b29a-6c92bf16086d
         * create_person_name : 南村陈奕迅
         * create_person_avatar : /respath/17/avatar/staff/20180619134458.jpeg
         * create_person_position : 美容师
         * create_person_position_id : 2
         * create_time : 2018-06-20 15:13:46
         * status : 1
         * collect_number : 1
         * like_number : 0
         * comment_number : 1
         * like_myself : 0
         * collect_myself : 1
         * haveRead : null
         */

        public int id;
        public int group_id;
        public String title;
        public String content;
        public String thumb_url;
        public String create_person;
        public String create_person_name;
        public String create_person_avatar;
        public String create_person_position;
        public int create_person_position_id;
        public String create_time;
        public int status;
        public int collect_number;
        public int like_number;
        public int comment_number;
        public int like_myself;
        public int collect_myself;
        public Object haveRead;
    }

    public static class TrainDetailCommentVosBean {
        /**
         * groupId : 17
         * trainId : 201
         * commentContent : 莫总
         * createPerson : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * createPersonName : 南村陈奕迅
         * createPersonAvatar : /respath/17/avatar/staff/20180505101400.jpeg
         * createPersonPosition : 美容师
         * createPersonPositionId : 2
         * createTime : 2018-06-21 19:41:41
         */

        public int groupId;
        public int trainId;
        public String commentContent;
        public String createPerson;
        public String createPersonName;
        public String createPersonAvatar;
        public String createPersonPosition;
        public String createPersonPositionId;
        public String createTime;
    }
}
