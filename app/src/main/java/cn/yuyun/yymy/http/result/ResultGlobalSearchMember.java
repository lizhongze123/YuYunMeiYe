package cn.yuyun.yymy.http.result;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultGlobalSearchMember {


    public MemberBean member;

    public static class MemberBean {

        public List<ResultMemberBean> records;

    }
}
