package cn.yuyun.yymy.ui.store;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/6/21
 */
public class RequestMemberAnalysis {

    public String group_id;
    public List<String> sp_id_list;
    public long start_date;
    public long end_date;
    public int start_row;
    public int count;
    /**是否导出 1导出 否则不导*/
    public int export;
    /**
     * 1.
     * 1 消费 2 消耗
     *
     *
     * */
    public int sort;
    /**排序类型 1 正序 2 倒序*/
    public int sort_type;

}
