package cn.yuyun.yymy.http.request;

import java.io.Serializable;

import cn.yuyun.yymy.bean.MomentsActionType;

/**
 * @author
 * @desc
 * @date
 */

public class RequestMomentsAction implements Serializable{

    public String content = "";
    public int workReportId;
    public int select_all_people;
    public MomentsActionType status;
}
