package cn.yuyun.yymy.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author
 * @desc
 * @date
 */
public enum FavoritesStatus {

    @SerializedName("1")
    Favorites("已收藏", true, 1){
        @Override
        public String toString() {
            return "已收藏";
        }
    },
    @SerializedName("0")
    UnFavorites("收藏", false, 0){
        @Override
        public String toString() {
            return "收藏";
        }
    };

    public final String desc;
    public boolean resId;
    public int value;

    FavoritesStatus(String desc, boolean resId, int value) {
        this.desc = desc;
        this.resId = resId;
        this.value = value;
    }
}
