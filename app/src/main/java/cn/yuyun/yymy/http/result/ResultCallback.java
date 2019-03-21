package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class ResultCallback implements Serializable{


    /**
     * id : 41669
     * member_id : 2880c18a-01b7-11e8-9a86-00163e0824d9
     * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * content : 哈哈
     * com_time : 2018-02-02 15:20:31
     * create_time : 2018-02-02 15:20:31
     * create_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * status : 1
     * member_name : 米奇
     * member_avatar : /respath/17/avatar/member/20180125180440.jpeg
     * create_person_name : 彭丽平
     * create_person_avatar : http://www.qqw21.com/article/uploadpic/2012-9/201291893228996.jpg
     * create_person_position : 无
     * create_person_position_id : 0
     */

    public int memberReturnVisitId;
    @SerializedName("pictures")
    public List<String> pictures;
    public String memberId;
    public String staffId;
    public String content;
    public String spName;
    public String visTime;
    public String createTime;
    public String createPerson;
    public int status;
    public String memberName;
    public String memberAvatar;
    public String createPersonName;
    public String createPersonAvatar;
    public String createPersonPosition;
    public String memberLevelName;
}
