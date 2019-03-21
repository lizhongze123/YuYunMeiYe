package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */

public class ResultDepositAsset implements Serializable{


    public int memberDepositId;
    public String depositId;
    public String memberId;
    public String name;
    public Value type;
    public String description;
    public String fetchTime;
    public String createTime;
    public String createPerson;
    public String modifyTime;
    public String modifyPerson;
    public Status status;
    public String memberAvatar;
    public String memberName;

    public enum Value {

        @SerializedName("1")
        FEMALE("贵重", R.color.text_red){
            @Override
            public String toString() {
                return "贵重";
            }
        },
        @SerializedName("2")
        MALE2("普通", R.color.text_green){
            @Override
            public String toString() {
                return "普通";
            }
        };

        public final String desc;
        public int resId;
        Value(String desc, int resId) {
            this.desc = desc;
            this.resId = resId;
        }
    }

    public enum Status {

        @SerializedName("0")
        TAKED("已取件", R.color.text_red){
            @Override
            public String toString() {
                return "已取件";
            }
        },
        @SerializedName("1")
        SAVING("保存中", R.color.text_red){
            @Override
            public String toString() {
                return "保存中";
            }
        },
        @SerializedName("2")
        OTHER("其他", R.color.text_green){
            @Override
            public String toString() {
                return "其他";
            }
        };

        public final String desc;
        public int resId;
        Status(String desc, int resId) {
            this.desc = desc;
            this.resId = resId;
        }
    }
}
