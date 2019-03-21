package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public class ResultPosition {


    /**
     * position_id : 0
     * group_id : 0
     * name : 董事长
     * avatar :
     * create_time :
     * create_person :
     * modify_time :
     * modify_person :
     * description : 系统内置-董事长
     * status : 1
     */

    @SerializedName("position_id")
    public String positionId;
    public int group_id;
    public String name;
    public String avatar;
    public String create_time;
    public String create_person;
    public String modify_time;
    public String modify_person;
    public String description;
    public int status;

    @Override
    public String toString() {
        return name;
    }
}
