package cn.yuyun.yymy.http.request;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestBindMember {

    public List<StaffMgrMemberQoListBean> staffMgrMemberQoList;

    public static class StaffMgrMemberQoListBean {
        /**
         * member_id : 6bc97b93-4630-11e9-aa79-7cd30ae45cee
         * staff_id : 77c1c70b-462d-11e9-aa79-7cd30ae45cee
         */
        public String member_id;
        public String staff_id;
    }
}
