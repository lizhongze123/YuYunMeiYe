package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import cn.yuyun.yymy.bean.RecordType;

/**
 * @author lzz
 * @desc 业绩类型：１售前２销售３服务
 * @date 2018/1/19
 */

public class RequestRecord {

    /**10位时间戳*/
    @SerializedName("start")
    public long start;

    @SerializedName("end")
    public long end;

    @SerializedName("sale_type")
    public RecordType recordType;

    @SerializedName("staff_id")
    public String staffId;

}
