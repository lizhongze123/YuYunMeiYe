package cn.yuyun.yymy.ui.home.notice;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/24
 */

public class NoticeBean implements Serializable{


    /**
     * collectCount : 1
     * likeCount : 1
     * commentCount : 3
     * collectFlag : false
     * likeFlag : false
     * commentFlag : false
     * readNameList : ["15600000000","15011719601"]
     * noticeVo : {"noticeId":722,"groupId":17,"content":"我再发通知公告。^_^℃1\u2026\u2026丽～8\u20e3️阿拉8\u20e3️、是因为自己方便的东西不知道是不是在我的身边朋友也可以的！你说啥呢！我在学校等会我就回来了\u2026\u2026我再发通知公告。^_^℃1\u2026\u2026丽～8\u20e3️阿拉8\u20e3️、是因为自己方便的东西不知道是不是在我的身边朋友也可以的！你说啥呢！我在学校等会我就回来了\u2026\u2026我再发通知公告。^_^℃1\u2026\u2026丽～8\u20e3️阿拉8\u20e3️、是因为自己方便的东西不知道是不是在我的身边朋友也可以的！你说啥呢！我在学校等会我就回来了\u2026\u2026我再发通知公告。^_^℃1\u2026\u2026丽～8\u20e3️阿拉8\u20e3️、是因为自己方便的东西不知道是不是在我的身边朋友也可以的！你说啥呢！我在学校等会我就回来了\u2026\u2026我在学校里的时候就会有很多人在我面前我都没有了？是否可以接受这个现实社会生活状态就是我们最喜欢😘！你是谁吗、不能在我心里了、不能够接受任何地方的政府采购制度建设项目可行性和项目研究人员在要求进行修改、在于他们自己人是否在自己心中都很喜欢这两件事情上发生地震灾区群众生活方式不同！是不是要给他一个人一🦊🔧❤️👂👀冲出重围、在线教育部门在全国范围内发现的一些企业和公司都会觉得我们是个小习惯的养成了每天要注意的啊、不会","pictures":["17/announce/201807181637162CC25356C005068E7479318E0B5CBE93.jpeg","17/announce/20180718163716354744BC4F63F66C5104F012870967B5.jpeg","17/announce/201807181637161BBB5874EF7F24BFA9E9ECE973686E7E.jpeg","17/announce/201807181637161822639F5C8ED16DC4CA7745C425D941.jpeg"],"createPerson":"f5b0df83-6154-11e8-b29a-6c92bf16086d","createPersonName":"琪琪","createPersonAvatar":"http://resource.yuyunrj.com/17/avatar/staff/20180712094954.jpeg","createPersonPosition":"店长","createTime":"2018-07-18 16:37:18","status":1}
     */

    public boolean readFlag;
    public int collectCount;
    public int likeCount;
    public int commentCount;
    public int readCount;
    public boolean collectFlag;
    public boolean selfCreateFlag;
    public boolean likeFlag;
    public boolean commentFlag;
    public NoticeVoBean noticeVo;
    public List<String> readNameList;

    public static class NoticeVoBean implements Serializable{
        /**
         * noticeId : 722
         * groupId : 17
         * content : 我再发通知公告。^_^℃1……丽～8⃣️阿拉8⃣️、是因为自己方便的东西不知道是不是在我的身边朋友也可以的！你说啥呢！我在学校等会我就回来了……我再发通知公告。^_^℃1……丽～8⃣️阿拉8⃣️、是因为自己方便的东西不知道是不是在我的身边朋友也可以的！你说啥呢！我在学校等会我就回来了……我再发通知公告。^_^℃1……丽～8⃣️阿拉8⃣️、是因为自己方便的东西不知道是不是在我的身边朋友也可以的！你说啥呢！我在学校等会我就回来了……我再发通知公告。^_^℃1……丽～8⃣️阿拉8⃣️、是因为自己方便的东西不知道是不是在我的身边朋友也可以的！你说啥呢！我在学校等会我就回来了……我在学校里的时候就会有很多人在我面前我都没有了？是否可以接受这个现实社会生活状态就是我们最喜欢😘！你是谁吗、不能在我心里了、不能够接受任何地方的政府采购制度建设项目可行性和项目研究人员在要求进行修改、在于他们自己人是否在自己心中都很喜欢这两件事情上发生地震灾区群众生活方式不同！是不是要给他一个人一🦊🔧❤️👂👀冲出重围、在线教育部门在全国范围内发现的一些企业和公司都会觉得我们是个小习惯的养成了每天要注意的啊、不会
         * pictures : ["17/announce/201807181637162CC25356C005068E7479318E0B5CBE93.jpeg","17/announce/20180718163716354744BC4F63F66C5104F012870967B5.jpeg","17/announce/201807181637161BBB5874EF7F24BFA9E9ECE973686E7E.jpeg","17/announce/201807181637161822639F5C8ED16DC4CA7745C425D941.jpeg"]
         * createPerson : f5b0df83-6154-11e8-b29a-6c92bf16086d
         * createPersonName : 琪琪
         * createPersonAvatar : http://resource.yuyunrj.com/17/avatar/staff/20180712094954.jpeg
         * createPersonPosition : 店长
         * createTime : 2018-07-18 16:37:18
         * status : 1
         */

        public int noticeId;
        public int groupId;
        public String content;
        public String createPerson;
        public String createPersonName;
        public String createPersonAvatar;
        public String createPersonPosition;
        public String createTime;
        public int status;
        public List<String> pictures;
    }
}
