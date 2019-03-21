package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class ResultStaffPicWall implements Serializable{


    /**
     * employeeLifePhotoId : 249
     * groupId : 17
     * staffId : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * thumbUrls : ["17/staff/life_photos/201807311749043B8F410AF6D30FEB02B46E471262256D.jpeg"]
     * createPerson : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * createTime : 2018-07-31 17:49:04
     * status : 1
     */

    public int employeeLifePhotoId;
    public int groupId;
    public String staffId;
    public String createPerson;
    public String createTime;
    public int status;
    public List<String> thumbUrls;
}
