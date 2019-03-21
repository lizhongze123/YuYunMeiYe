package cn.yuyun.yymy.http.result;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class ResultProject implements Serializable{

    public String good_id;
    public int status;
    public String name;
    public double guideprice;
    public String thumb_url;
    public String description;
    public boolean isChecked;
    public int good_type;
    public double achieve_percent;
    public int achieve_statistics_type;
}
