package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 员工状态  -1删除 0离职 1在职
 */
public enum StaffStatus {

    @SerializedName("-1")
    DELETE("删除"){
        @Override
        public String toString() {
            return "删除";
        }
    },
    @SerializedName("0")
    OFF("离职"){
        @Override
        public String toString() {
            return "离职";
        }
    },
    @SerializedName("0")
    ON("在职"){
        @Override
        public String toString() {
            return "在职";
        }
    };

    public final String desc;
    StaffStatus(String desc) {
        this.desc = desc;
    }
}
