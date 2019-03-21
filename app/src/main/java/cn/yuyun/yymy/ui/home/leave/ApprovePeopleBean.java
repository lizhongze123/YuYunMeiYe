package cn.yuyun.yymy.ui.home.leave;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;

/**
 * @author
 * @desc
 * @date
 */

public class ApprovePeopleBean implements Serializable{

    public List<ApproveByLeaveIDBean> approve_by_leaveID;

    public enum ApproveStatus{

        @SerializedName("0")
        WAITING("等待审批", R.color.approve_gray, 0),

        @SerializedName("1")
        AGREE("同意", R.color.approve_green, 1),

        @SerializedName("2")
        REFUSE("拒绝", R.color.approve_red, 2);

        public String desc;
        public int resId;
        public int val;

        ApproveStatus(String desc, int resId, int val) {
            this.desc = desc;
            this.resId = resId;
            this.val = val;
        }
    }

    public static class ApproveByLeaveIDBean implements Comparable{
        /**
         * id : 495
         * related_leave_id : 399
         * approve_person : d095abac-9078-11e8-9e22-7cd30ae0f408
         * approve_agreement : null
         * status : 0
         * create_time : 2018-07-26 14:39:53
         * approve_person_name : 小芝
         * approve_person_avatar :
         * approve_person_position :
         * approve_person_position_id : null
         */

        public int id;
        public int related_leave_id;
        public String approve_person;
        public String approve_agreement;
        public ApproveStatus status;
        public String create_time;
        public String approve_person_name;
        public String approve_person_avatar;
        public String approve_person_position;
        public String approve_person_position_id;

        @Override
        public int compareTo(Object obj) {
            int result = 0;
            if(obj instanceof ApproveByLeaveIDBean) {
                ApproveByLeaveIDBean b = (ApproveByLeaveIDBean)obj;
                if(b.status.val > this.status.val) {
                    return 1;
                } else if (b.status.val < this.status.val) {
                    return -1;
                }
            }
            return result;
        }
    }
}
