package cn.yuyun.yymy.http.request;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/8
 */

public class RequestOutSign implements Serializable{

    public String place;
    public String notes = "";
    public double lng;
    public double lat;
    public String baseon_id;
    public String baseon_type;
    public List<String> pictures;

}
