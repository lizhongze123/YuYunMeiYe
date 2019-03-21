package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/21
 */
public class RequestStoreStaff {

    @SerializedName("sp_id_list")
    public List<String> spIdList;

    public String baseon_type;

    @SerializedName("search_text")
    public String searchText="";

    @SerializedName("birth_day_tils")
    public String birthDay="";

    /**0是离职，1是正常，-1是回收站*/
    @SerializedName("status")
    public JobStatus status = JobStatus.IN;

    public enum JobStatus {

        @SerializedName("1")
        IN("在职", R.color.text_green, 1){
            @Override
            public String toString() {
                return "在职";
            }
        },

        @SerializedName("0")
        OUT("离职",R.color.text_red, 0){
            @Override
            public String toString() {
                return "离职";
            }
        },

        @SerializedName("-1")
        RECYCLED("回收站",R.color.text_red, -1){
            @Override
            public String toString() {
                return "回收站";
            }
        };

        public final int resId;
        public final int val;

        JobStatus(String desc, int resId, int val) {
            this.resId = resId;
            this.val = val;
        }

    }
}
