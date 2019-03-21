package cn.yuyun.yymy.http.request;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/10/31
 */
public class RequestReportLiabilitiesThree implements Serializable{


    /**
     * group_id : 17
     * sp_id_list : [499]
     * start_row : 1
     * count : 10
     * start_date : 1538352000
     * end_date : 1540944000
     * export : 0
     */

    public String group_id;
    public long start_date;
    public long end_date;
    public Object brand_id;
    public List<String> sp_id_list;
}
