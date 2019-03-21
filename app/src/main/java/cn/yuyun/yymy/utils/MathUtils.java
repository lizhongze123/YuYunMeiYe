package cn.yuyun.yymy.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * @author
 * @desc
 * @date
 */
public class MathUtils {

    public static double add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).doubleValue();
    }

    public static double sub(double d1, double d2) {        // 进行减法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double d1, double d2) {        // 进行乘法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double d1, double d2, int len) {// 进行除法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double parseDouble(String text){
        if(TextUtils.isEmpty(text) || text.equals(".")){
            return 0;
        }else{
            return Double.valueOf(text);
        }
    }


    /**
     * <pre>
     * 数字格式化显示
     * 小于万默认显示 大于万以1.7万方式显示最大是9999.9万
     * 大于亿以1.1亿方式显示最大没有限制都是亿单位
     * </pre>
     * @param num
     *            格式化的数字
     * @param kBool
     *            是否格式化千,为true,并且num大于999就显示999+,小于等于999就正常显示
     * @return
     */
    public static String formatNum(String num, Boolean kBool) {
        StringBuffer sb = new StringBuffer();
        /*if (!StringUtils.isNumeric(num)){
            return "0";
        }*/
        if (kBool == null){
            kBool = false;
        }

        BigDecimal b0 = new BigDecimal("1000");
        BigDecimal b1 = new BigDecimal("10000");
        BigDecimal b3 = new BigDecimal(num);

        String formatNumStr = "";
        String nuit = "";

        // 以千为单位处理
        if (kBool) {
            if (b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1) {
                return "999+";
            }
            return num;
        }

        // 以万为单位处理
        if (b3.compareTo(b1) == -1) {
            sb.append(b3.toString());
        } else {
            formatNumStr = b3.divide(b1).toString();
            nuit = "万";
        }
        if (!"".equals(formatNumStr)) {
            int i = formatNumStr.indexOf(".");
            if (i == -1) {
                sb.append(formatNumStr).append(nuit);
            } else {
                String v = formatNumStr.substring(0, i);
                sb.append(v).append(nuit);
            }
        }
        if (sb.length() == 0){
            return "0";
        }
        return sb.toString();
    }

    public static String formatNum(String num, Boolean kBool, TextView textView) {
        StringBuffer sb = new StringBuffer();
        /*if (!StringUtils.isNumeric(num)){
            return "0";
        }*/
        if (kBool == null){
            kBool = false;
        }

        BigDecimal b0 = new BigDecimal("1000");
        BigDecimal b1 = new BigDecimal("10000");
        BigDecimal b3 = new BigDecimal(num);

        String formatNumStr = "";

        // 以千为单位处理
        if (kBool) {
            if (b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1) {
                return "999+";
            }
            return num;
        }

        // 以万为单位处理
        if (b3.compareTo(b1) == -1) {
            sb.append(b3.toString());
        } else {
            formatNumStr = b3.divide(b1).toString();
        }
        if (!"".equals(formatNumStr)) {
            int i = formatNumStr.indexOf(".");
            if (i == -1) {
                sb.append(formatNumStr);
//                textView.setVisibility(View.VISIBLE);
            } else {
                String v = formatNumStr.substring(0, i);
                sb.append(v);
//                textView.setVisibility(View.VISIBLE);
            }
        }
        if (sb.length() == 0){
//            textView.setVisibility(View.GONE);
            return "0";
        }
        return sb.toString();
    }

    private static String removeLast0(String string) {
        if (string==null || string.length() == 0) {
            return "0";
        }
        try {
            while (string.charAt(string.length() - 1) == '0') {
                string = string.substring(0, string.length() - 1);
            }
            if (string.charAt(string.length() - 1) == '.') {
                string = string.substring(0, string.length() - 1);
            }
        } catch (Exception e) {
            return "";
        }
        return string;
    }

    public static String formatNum(String value) {
        try {
            double v = Double.parseDouble(value);
            if (v > 10000) {
                String str = String.valueOf(v / 10000);
                return removeLast0(str) + "万";
            } else {
                return value;
            }
        } catch (Exception e) {
            return value;
        }
    }

}
