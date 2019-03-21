package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class ResultPicWall implements Serializable{


    /**
     * memberLifePhotoId : 550
     * groupId : 17
     * memberId : 5063fa8f-80c8-11e8-9e22-7cd30ae0f408
     * memberName : 错
     * memberAvatar : /17/avatar/staff/20180727174848.png
     * thumbUrls : ["17/thumb_wall/20180717171701ED85157AAB12E02D54B15E8AA87EADA9.jpeg"]
     * createPerson : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * createTime : 2018-07-17 17:17:01
     * status : 1
     */

    public int memberLifePhotoId;
    public int groupId;
    public String memberId;
    public String memberName;
    public String memberAvatar;
    public String createPerson;
    public String createTime;
    public int status;
    public List<String> thumbUrls;
}
