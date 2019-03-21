package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultUnboxingLabel implements Serializable{


    public List<UnboxingLabelBean> record;

    public static class UnboxingLabelBean implements Serializable{
        /**
         * labelId : 33
         * labelName : 啊？
         * labelType : 1
         * groupId : 17
         * createTime : 2018-08-25 13:43:10
         */

        public int labelId;
        public String labelName;
        public int labelType;
        public int groupId;
        public String createTime;
    }
}
