package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public enum LikeStatus {

    @SerializedName("1")
    Like("已点赞", true, 1){
        @Override
        public String toString() {
            return "已点赞";
        }
    },
    @SerializedName("0")
    UnLike("点赞", false, 0){
        @Override
        public String toString() {
            return "点赞";
        }
    };

    public final String desc;
    public boolean resId;
    public int value;

    LikeStatus(String desc, boolean resId, int value) {
        this.desc = desc;
        this.resId = resId;
        this.value = value;
    }
}
