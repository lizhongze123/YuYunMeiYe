package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultWork2 implements Serializable {


    /**
     * thisTime : 2018-10-18
     * workVos : [{"likeCount":0,"commentCount":0,"alreadyRead":null,"selfLikeFlag":false,"selfCommentFlag":false,"selfCreateFlag":false,"readFlag":false,"select_all_people":1,"workReportDetailVo":{"workReportId":1347,"type":1,"staffId":"18be23da-8fff-11e8-9e22-7cd30ae0f408","createPersonName":"😘😍爱情","createPersonAvatar":"/17/avatar/staff/20180830140338.jpeg","createPersonPosition":"董事长","content":"刚刚应用","status":1,"createTime":"2018-10-18 14:59:52","baseonType":1,"baseonId":17,"picture":["17/activity/20181018145951F57F43441FE99C26E07DD832E14E9ACC.png"]},"workReportApproveVos":[]},{"likeCount":0,"commentCount":0,"alreadyRead":null,"selfLikeFlag":false,"selfCommentFlag":false,"selfCreateFlag":false,"readFlag":false,"select_all_people":1,"workReportDetailVo":{"workReportId":1346,"type":1,"staffId":"18be23da-8fff-11e8-9e22-7cd30ae0f408","createPersonName":"😘😍爱情","createPersonAvatar":"/17/avatar/staff/20180830140338.jpeg","createPersonPosition":"董事长","content":"哈哈","status":1,"createTime":"2018-10-18 14:59:29","baseonType":1,"baseonId":17,"picture":["17/activity/2018101814565369BE35A3BF1530BFEA33712ADB847A21.png"]},"workReportApproveVos":[]},{"likeCount":0,"commentCount":0,"alreadyRead":null,"selfLikeFlag":false,"selfCommentFlag":false,"selfCreateFlag":false,"readFlag":false,"select_all_people":1,"workReportDetailVo":{"workReportId":1345,"type":1,"staffId":"18be23da-8fff-11e8-9e22-7cd30ae0f408","createPersonName":"😘😍爱情","createPersonAvatar":"/17/avatar/staff/20180830140338.jpeg","createPersonPosition":"董事长","content":"哈哈","status":1,"createTime":"2018-10-18 14:57:54","baseonType":1,"baseonId":17,"picture":["17/activity/2018101814565369BE35A3BF1530BFEA33712ADB847A21.png"]},"workReportApproveVos":[]},{"likeCount":0,"commentCount":0,"alreadyRead":null,"selfLikeFlag":false,"selfCommentFlag":false,"selfCreateFlag":false,"readFlag":false,"select_all_people":1,"workReportDetailVo":{"workReportId":1344,"type":1,"staffId":"18be23da-8fff-11e8-9e22-7cd30ae0f408","createPersonName":"😘😍爱情","createPersonAvatar":"/17/avatar/staff/20180830140338.jpeg","createPersonPosition":"董事长","content":"哈哈","status":1,"createTime":"2018-10-18 14:56:29","baseonType":1,"baseonId":17,"picture":["17/activity/2018101814562969BE35A3BF1530BFEA33712ADB847A21.png"]},"workReportApproveVos":[]}]
     */

    public String thisTime;
    public int allTotal;
    public int readTotal;
    public List<ResultWork> workVos;

}
