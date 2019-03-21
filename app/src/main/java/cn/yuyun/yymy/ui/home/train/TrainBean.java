package cn.yuyun.yymy.ui.home.train;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.FavoritesStatus;
import cn.yuyun.yymy.bean.LikeStatus;

import static cn.yuyun.yymy.bean.FavoritesStatus.UnFavorites;
import static cn.yuyun.yymy.bean.LikeStatus.UnLike;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/24
 */

public class TrainBean implements Serializable{

    /**
     * collectCount : 0
     * likeCount : 0
     * commentCount : 0
     * collectFlag : false
     * likeFlag : false
     * commentFlag : false
     * readNameList : ["15011719601"]
     * trainInfoVo : {"trainInfoId":227,"groupId":17,"title":"uusee","content":"hhjj","pictures":[],"createPerson":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","createPersonName":"彭丽平","createPersonAvatar":"http://resource.yuyunrj.com/17/avatar/staff/20180628152703.jpeg","createPersonPosition":"美容师","createTime":"2018-07-16 11:42:23","status":1}
     */

    public boolean readFlag;
    public int collectCount;
    public int likeCount;
    public int commentCount;
    public int readCount;
    public boolean collectFlag;
    public boolean likeFlag;
    public boolean commentFlag;
    public boolean selfCreateFlag;
    public TrainInfoVoBean trainInfoVo;
    public List<String> readNameList;

    public class TrainInfoVoBean implements Serializable{
        /**
         * trainInfoId : 227
         * groupId : 17
         * title : uusee
         * content : hhjj
         * pictures : []
         * createPerson : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * createPersonName : 彭丽平
         * createPersonAvatar : http://resource.yuyunrj.com/17/avatar/staff/20180628152703.jpeg
         * createPersonPosition : 美容师
         * createTime : 2018-07-16 11:42:23
         * status : 1
         */

        public int trainInfoId;
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
