package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAddCommunication {

    public long vis_time;

    public String groupId;
    public String memberId;
    public String staffId;
    public String spName;
    public String content;
    public long comTime;
    public List<String> pictures;

}
