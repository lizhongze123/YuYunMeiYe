package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

public enum LeaveReason {

    @SerializedName("1")
    SICK("病假", 1){
        @Override
        public String toString() {
            return "病假";
        }
    },
    @SerializedName("2")
    PERSONAL("事假", 2){
        @Override
        public String toString() {
            return "事假";
        }
    },
    @SerializedName("3")
    MARRY("婚假", 3){
        @Override
        public String toString() {
            return "婚假";
        }
    },
    @SerializedName("4")
    REST("调休", 4){
        @Override
        public String toString() {
            return "调休";
        }
    },
    @SerializedName("5")
    OTHER("其他", 5){
        @Override
        public String toString() {
            return "其他";
        }
    };

    public String desc;
    public int value;

    LeaveReason(String desc, int value) {
        this.desc = desc;
        this.value = value;
    }
}
