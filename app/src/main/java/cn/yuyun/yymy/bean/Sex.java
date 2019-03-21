package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

import cn.yuyun.yymy.R;

public enum Sex {

    @SerializedName("2")
    MALE("男", R.drawable.avatar_default_male, R.drawable.icon_male, 2){
        @Override
        public String toString() {
            return "男";
        }
    },
    @SerializedName("1")
    FEMALE("女", R.drawable.avatar_default_female,R.drawable.icon_female, 1){
        @Override
        public String toString() {
            return "女";
        }
    },
    @SerializedName("0")
    MALE2("男", R.drawable.avatar_default_male, R.drawable.icon_male, 0){
        @Override
        public String toString() {
            return "男";
        }
    };

    public final String desc;
    public int resId;
    public int sexId;
    public int value;

    Sex(String desc, int resId, int sexId, int value) {
        this.desc = desc;
        this.resId = resId;
        this.sexId = sexId;
        this.value = value;
    }
}
