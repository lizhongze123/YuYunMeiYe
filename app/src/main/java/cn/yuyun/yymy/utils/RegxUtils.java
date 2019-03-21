package cn.yuyun.yymy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @desc
 * @date
 */

public class RegxUtils {

    private static Pattern RULE = Pattern.compile("(\\d+\\.\\d+)|(\\d+)");

    public static float getPureDouble(String str) {
        if (str == null || str.length() == 0) return 0;
        float result = 0;
        try {
            //如何提取带负数d ???
            Matcher matcher = RULE.matcher(str);
            matcher.find();
            //提取匹配到的结果
            String string = matcher.group();
            result = Float.parseFloat(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args){

        test();
    }
    public static void test(){
        System.out.println(getPureDouble("12"));
        System.out.println(getPureDouble("wew3423.36"));
        System.out.println(getPureDouble("wewsf"));
        System.out.println(getPureDouble("000"));
        System.out.println(getPureDouble(null));
    }

}
