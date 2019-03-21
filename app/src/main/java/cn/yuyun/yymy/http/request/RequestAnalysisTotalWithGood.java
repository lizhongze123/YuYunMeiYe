package cn.yuyun.yymy.http.request;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */

public class RequestAnalysisTotalWithGood implements Serializable{


    /**
     * start : 1512061261
     * end : 1514735999
     * sp_id_list : [33,34,35,42,45]
     */

    public long start;
    public long end;
    public List<String> sp_id_list;
    public String good_id;
    public String good_brand_id;
}
