package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultUnboxingBean implements Serializable{

    /**
     * collectCount : 0
     * likeCount : 0
     * commentCount : 0
     * collectFlag : false
     * likeFlag : false
     * commentFlag : false
     * shareOrderVo : {"shareOrderId":211,"groupId":17,"content":"今天帮一个客户做服务，本来是快铺垫好了的，后来还是要考虑考虑，今天开了一笔大单给大家分享一下我是如何做到得","pictures":[],"createPerson":"0eb8a149-62f6-11e8-b29a-6c92bf16086d","createPersonName":"胡帅","createPersonAvatar":"http://resource.yuyunrj.com/17/avatar/staff/20180529120918.jpeg","createPersonPosition":"","createTime":"2018-07-19 20:48:33","status":1,"labelNameList":[]}
     */

    public int collectCount;
    public int likeCount;
    public int commentCount;
    public int readCount;
    public boolean collectFlag;
    public boolean selfCreateFlag;
    public boolean likeFlag;
    public boolean commentFlag;
    public ShareOrderVoBean shareOrderVo;

    public static class ShareOrderVoBean implements Serializable{
        /**
         * shareOrderId : 211
         * groupId : 17
         * content : 今天帮一个客户做服务，本来是快铺垫好了的，后来还是要考虑考虑，今天开了一笔大单给大家分享一下我是如何做到得
         * pictures : []
         * createPerson : 0eb8a149-62f6-11e8-b29a-6c92bf16086d
         * createPersonName : 胡帅
         * createPersonAvatar : http://resource.yuyunrj.com/17/avatar/staff/20180529120918.jpeg
         * createPersonPosition :
         * createTime : 2018-07-19 20:48:33
         * status : 1
         * labelNameList : []
         */

        public int shareOrderId;
        public int groupId;
        public String content;
        public String createPerson;
        public String createPersonName;
        public String createPersonAvatar;
        public String createPersonPosition;
        public String createTime;
        public int status;
        public List<String> pictures;
        public List<String> labelNameList;
        public List<ResultUnboxingLabel.UnboxingLabelBean> labelList;
    }
}
