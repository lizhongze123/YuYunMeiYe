package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class CommunicationRecordBean implements Serializable{

    public int id;
    public String member_id;
    public String staff_id;
    /**沟通内容*/
    public String content;
    /**沟通时间*/
    @SerializedName("com_time")
    public String comTime;
    public String create_time;
    public String create_person;
    public int status;
    public String create_person_name;
    public String create_person_avatar;
    public String create_person_position;
    public String create_person_position_id;

}
