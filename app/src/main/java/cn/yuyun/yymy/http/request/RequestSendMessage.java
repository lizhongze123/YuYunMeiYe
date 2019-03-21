package cn.yuyun.yymy.http.request;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/28
 */
public class RequestSendMessage {


    public List<SendMsgListBean> smsSendDetailQoList;

    public static class SendMsgListBean {
        /**
         * member_id : 2d8c5455-5fb5-11e8-b29a-6c92bf16086d
         * member_name : ertrtr
         * phone : 212
         * content :     国庆佳节，举国同庆，一庆祖国越来越昌盛，二庆日子越过越红火，三庆心情越来越欢畅，愿这盛大的节日带给您永远的幸运!
         * sp_name : 开发专用
         * og_type : 2
         * og_id : 209
         * group_id : 17
         * create_person_name : 彭丽萍33333
         * type : success
         */

        public String member_id;
        public String phone;
        public String content;

    }
}
