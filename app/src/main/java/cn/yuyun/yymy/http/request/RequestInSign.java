package cn.yuyun.yymy.http.request;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class RequestInSign implements Serializable{

    public String baseon_id;
    public String baseon_type;
    /**1上班  2下班*/
    public int span_status;
}
