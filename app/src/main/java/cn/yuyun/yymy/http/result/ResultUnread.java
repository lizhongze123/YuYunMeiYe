package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class ResultUnread implements Serializable{

    public int noticeNoReadCount;
    public int trainInfoNoReadCount;
    public int approveWorkCount;
    public int myReviewCount;
    public int systemMsgCount;

    @SerializedName("latestActivityDetails")
    public List<ActionBean> actionsList;

}
