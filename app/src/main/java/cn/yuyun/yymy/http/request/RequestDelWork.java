package cn.yuyun.yymy.http.request;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestDelWork {

    public List<Integer> workReportIdList;
    /**公告状态 0 不显示 1 显示  -1删除*/
    public int status = 0;

}
