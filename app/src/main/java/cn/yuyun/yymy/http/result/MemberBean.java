package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class MemberBean implements Serializable{


    /**
     * id : 67
     * group_id : 17
     * url: "http://baidu.com"
     */

    public int id;
    public String account_name;
    public String member_id;
    public String create_person;
    public String create_time;
    private String PinYin;
    private String FirstPinYin;


    public String getPinYin() {
        return PinYin;
    }

    public void setPinYin(String pinYin) {
        PinYin = pinYin;
    }

    public String getFirstPinYin() {
        return FirstPinYin;
    }

    public void setFirstPinYin(String firstPinYin) {
        FirstPinYin = firstPinYin;
    }

}
