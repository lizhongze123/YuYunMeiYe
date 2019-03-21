package cn.lzz.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/31
 */

public class StringFormatUtils {

    /**保留两位小数*/
    public static String formatTwoDecimal(double str) {
        BigDecimal bigDec = new BigDecimal(str);
        double total = bigDec.setScale(2, java.math.BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

    /**不用科学计数法显示*/
    public static String formatDecimal(double str) {
        return String.format("%1$.2f", str);
    }

    /**百分数*/
    public static String formatPercent(double str) {
        DecimalFormat format = new DecimalFormat("0.0%");
        return format.format(str);
    }

    public static String percent(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        str = nf.format(p3);
        return str;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param
     * @return
     */
    public static String getUserDate(String time) {
        if(TextUtils.isEmpty(time)){
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "";
        try {
            Date date = dateFormat.parse(time);
            dateString = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static String getUserDate(String time, String format) {
        if(TextUtils.isEmpty(time)){
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.FORMAT_DATETIME_UI);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(format);
        String dateString = "";
        try {
            Date date = dateFormat.parse(time);
            dateString = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

}
