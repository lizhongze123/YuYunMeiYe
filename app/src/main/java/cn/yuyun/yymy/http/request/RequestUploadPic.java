package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/3/15
 */

public class RequestUploadPic {

    public UploadPicType type;
    public List<File> file;

    public enum UploadPicType{

        @SerializedName("1")
        WORK(1),

        @SerializedName("2")
        LEAVE(2),

        @SerializedName("3")
        UNBOXING(3),

        @SerializedName("4")
        NOTICE(4),

        @SerializedName("5")
        TRAIN(5),

        @SerializedName("6")
        ACTION(6),

        @SerializedName("7")
        OUTSIGN(7),

        @SerializedName("9")
        STAFF(9),

        @SerializedName("10")
        MEMBER(10),

        @SerializedName("12")
        COMMUNICATION(12),

        @SerializedName("11")
        CALLBACK(11);

        public int value;

        UploadPicType(int value){
          this.value = value;
        }

    }

}
