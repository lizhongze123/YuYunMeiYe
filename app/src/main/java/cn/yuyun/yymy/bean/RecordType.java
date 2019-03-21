package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 业绩类型：1售前 2销售 3服务
 */
public enum RecordType {

    @SerializedName("1")
    BEFORE("售前记录","售前详情"){
        @Override
        public String toString() {
            return "售前记录";
        }
    },
    @SerializedName("2")
    SALE(" ","销售详情"){
        @Override
        public String toString() {
            return "销售记录";
        }
    },
    @SerializedName("3")
    SERVICE("消耗记录", "消耗详情"){
        @Override
        public String toString() {
            return "消耗记录";
        }
    };

    public String desc;
    public String detail;
    RecordType(String text, String detail) {
        this.desc = text;
        this.detail = detail;
    }

}
