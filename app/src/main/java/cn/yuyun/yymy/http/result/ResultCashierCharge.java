package cn.yuyun.yymy.http.result;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultCashierCharge {

    public int record_type;
    public List<RecordIdBean> record_id;

    public static class RecordIdBean {

        public String storedvalue_id;
    }
}
