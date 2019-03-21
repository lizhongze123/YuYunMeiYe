package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/24
 */
public class RequestReportBusiness {

    public String group_id;
    public List<String> sp_id_list;
    public long start_date;
    public long end_date;

}
