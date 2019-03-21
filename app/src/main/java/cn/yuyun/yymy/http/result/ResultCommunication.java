package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class ResultCommunication implements Serializable{


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

    public int memberCommunicationId;
    public String memberId;
    public String staff_id;
    public String content;

    @SerializedName("comTime")
    public String comTime;

    @SerializedName("vis_time")
    public String visTime;

    @SerializedName("create_time")
    public String createTime;

    @SerializedName("spName")
    public String spName;

    @SerializedName("pictures")
    public List<String> pictures;

    public String create_person;
    public int status;
    @SerializedName("memberName")
    public String memberName;

    @SerializedName("memberAvatar")
    public String memberAvatar;

    @SerializedName("createPersonName")
    public String createPersonName;
    @SerializedName("memberLevelName")
    public String memberLevelName;
    public String createPersonAvatar;
    public String createPersonPosition;
    public int create_person_position_id;

}
