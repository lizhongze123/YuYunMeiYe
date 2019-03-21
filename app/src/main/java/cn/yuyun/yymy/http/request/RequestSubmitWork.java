package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/24
 */

public class RequestSubmitWork implements Serializable{

    /**汇报类型：1.日报 2.周报 3.月报 4.其他*/
    public int type = 1;

    /**汇报内容*/
    public String content;

    public List<String> picture;

    public String staffId;

    public String baseonType;

    public String baseonId;

    @SerializedName("select_all_people")
    public int selectAllPeople;

    /**审批人*/
    @SerializedName("addWorkReportApproveQoList")
    public List<ApprovePeopleBean> approvePeopleBeanList;

    public static class ApprovePeopleBean implements Serializable{

        /**审批人Id*/
        @SerializedName("approvePerson")
        public String approveStaffId;

        public String content = "";

    }

}
