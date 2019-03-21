package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.WorkType;

/**
 * @author
 * @desc
 * @date
 */

public class ResultWork implements Serializable{


    /**
     * likeCount : 1
     * commentCount : 1
     * selfLikeFlag : true
     * selfCommentFlag : true
     * workReportDetailVo : {"workReportId":1737,"type":1,"staffId":"f5b0df83-6154-11e8-b29a-6c92bf16086d","createPersonName":"琪琪","createPersonAvatar":"http://resource.yuyunrj.com/17/avatar/staff/20180712094954.jpeg","createPersonPosition":"店长","content":"我需要发送一条工作汇报一条日报信息","status":1,"createTime":"2018-07-12 21:28:22","baseonType":2,"baseonId":199,"picture":["/17/workReport/201807122128210B87D4FE508192FB3C6E7817AF6E353F.png","/17/workReport/20180712212821ADF8F2241EAE579450C478BE2FC72286.png","/17/workReport/2018071221282119AAC3D298E890F7DC177F13B5CC6809.png"]}
     * workReportApproveVos : [{"workReportApproveId":2647,"workReportId":1737,"approvePerson":"b4ecf981-ec6e-11e7-9a86-00163e0824d9","createPersonName":"谢彩华","createPersonAvatar":"","createPersonPosition":"","content":null,"status":null,"createTime":"2018-07-12 21:28:22","modifyTime":"2018-07-12 21:28:22"},{"workReportApproveId":2648,"workReportId":1737,"approvePerson":"b4f11fd1-ec6e-11e7-9a86-00163e0824d9","createPersonName":"苏惠娟","createPersonAvatar":"","createPersonPosition":"","content":null,"status":null,"createTime":"2018-07-12 21:28:22","modifyTime":"2018-07-12 21:28:22"},{"workReportApproveId":2649,"workReportId":1737,"approvePerson":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","createPersonName":"彭丽平","createPersonAvatar":"/17/avatar/staff/20180717134210.jpeg","createPersonPosition":"美容师","content":"我需要发送一条工作汇报一条日报信息","status":1,"createTime":"2018-07-12 21:28:22","modifyTime":"2018-07-17 14:19:50"}]
     */

    public int likeCount;
    public String likePerson;
    public int commentCount;
    public boolean selfLikeFlag;
    public boolean readFlag;
    public boolean selfCreateFlag;
    public boolean selfCommentFlag;
    /**1.全部*/
    public int select_all_people;
    public WorkReportDetailVoBean workReportDetailVo;
    public List<WorkReportApproveVosBean> workReportApproveVos;
    public List<WorkReportApproveVosBean> workReportlikeVos;

    public static class WorkReportDetailVoBean implements Serializable{
        /**
         * workReportId : 1737
         * type : 1
         * staffId : f5b0df83-6154-11e8-b29a-6c92bf16086d
         * createPersonName : 琪琪
         * createPersonAvatar : http://resource.yuyunrj.com/17/avatar/staff/20180712094954.jpeg
         * createPersonPosition : 店长
         * content : 我需要发送一条工作汇报一条日报信息
         * status : 1
         * createTime : 2018-07-12 21:28:22
         * baseonType : 2
         * baseonId : 199
         * picture : ["/17/workReport/201807122128210B87D4FE508192FB3C6E7817AF6E353F.png","/17/workReport/20180712212821ADF8F2241EAE579450C478BE2FC72286.png","/17/workReport/2018071221282119AAC3D298E890F7DC177F13B5CC6809.png"]
         */

        public int workReportId;
        public WorkType type;
        public String staffId;
        public String createPersonName;
        public String createPersonAvatar;
        public String createPersonPosition;
        public String content;
        public int status;
        public String createTime;
        public int baseonType;
        public int baseonId;
        public List<String> picture;
    }

    public static class WorkReportApproveVosBean implements Serializable{
        /**
         * workReportApproveId : 2647
         * workReportId : 1737
         * approvePerson : b4ecf981-ec6e-11e7-9a86-00163e0824d9
         * createPersonName : 谢彩华
         * createPersonAvatar :
         * createPersonPosition :
         * content : null
         * status : null
         * createTime : 2018-07-12 21:28:22
         * modifyTime : 2018-07-12 21:28:22
         */

        public int workReportApproveId;
        public int workReportId;
        public String approvePerson;
        public String createPersonName;
        public String createPersonAvatar;
        public String createPersonPosition;
        public String content;
        public int status;
        public String createTime;
        public String modifyTime;
    }
}
