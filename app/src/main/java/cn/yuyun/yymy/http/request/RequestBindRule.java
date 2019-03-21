package cn.yuyun.yymy.http.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


/**
 * @author
 * @desc
 * @date
 */

public class RequestBindRule {

    public int ruleId;

    public String createPerson;

    public String groupId;

    public int autoUntieFlag;

    public List<OgRule> ogRuleList;

    public static class OgRule implements Serializable {

        /**组织机构id 根据og_type判断当前为集团id还是门店id*/
        public String ogId;

        /**考勤类型:1集团 2门店*/
        public int ogType = 2;

        public String ogName;

    }

}
