package cn.lzz.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *@desc  日期时间工具类
 *@author
 *@date   
 */
public class DateTimeUtils {

    static public final String FORMAT_DATETIME_UI = "yyyy-MM-dd HH:mm:ss";
    static public final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";
    static public final String FORMAT_DATETIME_TWO = "yyyy/MM/dd HH:mm";
    static public final String FORMAT_YY_MM = "yyyy-MM";
    static public final String FORMAT_YY = "yyyy";
    static public final String FORMAT_DATETIME_YEAR_MONTH = "yyyy年MM月";
    static public final String FORMAT_DATETIME_YEAR_MONTH_DAY = "yyyy年MM月dd日";
    static public final String FORMAT_DATE_UI = "yyyy-MM-dd";
    static public final String FORMAT_DATE_UI_TWO = "yyyy/MM/dd";
    static public final String FORMAT_TIME_UI = "HH:mm:ss";
    static public final String FORMAT_HH_mm = "HH:mm";
    static public final String FORMAT_MD_CH = "MM月dd日";
    static public final String FORMAT_MD_EN = "MM-dd";
    static public final String FORMAT_MD_EN_TWO = "MM/dd";
    static public final String FORMAT_M_EN = "MM";
    static public final String FORMAT_D_EN = "dd";

    private static final int phpTimeStamp = 10;
    private static final int javaTimeStamp = 13;

    public static String getDate(long time, String format) {
        if (time == 0) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        return df.format(time);
    }

    public static String getInternetTime(String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        URL url = null;
        Date date = null;
        try {
            url = new URL("https://www.baidu.com/");
            URLConnection uc = url.openConnection();
            uc.connect(); //发出连接
            long ld = uc.getDate();
            date = new Date(ld);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return formatter.format(date);
    }

    /**
     * yyyy-MM-dd
     */
    public static String getDate(long time) {
        if (time == 0) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(FORMAT_DATE_UI, Locale.CHINA);
        return df.format(time);
    }

    /**
     * HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getDateStr(long time) {
        if (time == 0) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(FORMAT_TIME_UI, Locale.CHINA);
        return df.format(time);
    }

    /**
     * @return 两个日期的时间差 e.g  1天2小时5分
     */
    public static String minusDateForDaysStr(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays + "天" + diffHours + "小时" + diffMinutes + "分";
    }

    /**
     * @return 两个日期的时间差 e.g  1
     */
    public static long minusDateForDaysCount(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays;
    }

    /**
     * @return 是否大于多少天
     */
    public static Boolean minusDateForDaysCount(long date1, long date2, int day) {
        long diff = date2 - date1;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 );
        if(diffDays > day){
            return false;
        }else{
            return true;
        }
    }

    public static int countDays(Calendar cal1, Calendar cal2) {
        if (cal1.after(cal2)) {
            return countDays(cal2, cal1);
        }
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 == year2) {
            int day1 = cal1.get(Calendar.DAY_OF_YEAR);
            int day2 = cal2.get(Calendar.DAY_OF_YEAR);
            return day2 - day1 + 1;
        } else {
            int yearNum = year2 - year1;
            int count = 0;
            Calendar countCal = (Calendar) cal1.clone();
            for (int i = 0; i < yearNum; i++) {
                count += countCal.getMaximum(Calendar.DAY_OF_YEAR);
                countCal.add(Calendar.YEAR, 1);
            }
            return count + countDays(countCal, cal2);
        }
    }

    /**
     * @return 两个日期的时间差 e.g  2小时5分
     */
    public static String minusDateForHoursStr(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        return diffHours + "小时" + diffMinutes + "分";
    }

    /**
     * @return 两个日期的时间差 e.g  2小时5分
     */
    public static long minusDateForHours(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        return diff / (60 * 60 * 1000);
    }

    /**
     * @return e.g  2小时5分
     */
    public static String getHoursMinute(int minute) {
        long minutesValue = minute % 60;
        long hoursValue = minute / 60;
        if (hoursValue > 0) {
            return hoursValue + "小时" + minutesValue + "分钟";
        } else {
            return minutesValue + "分钟";
        }
    }

    /**
     * @return e.g  2年5月
     */
    public static String formYearMonth(int month) {
        long monthValue = month % 12;
        long yearValue = month / 12;
        return yearValue + "年" + monthValue + "月";
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串转化日期
     *
     * @param date
     * @return
     */
    public static Date parse(String date) {
        if (date == null) {
            return new Date();
        }
        DateFormat df = new SimpleDateFormat(FORMAT_DATETIME_UI, Locale.CHINA);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm格式的字符串转化日期
     */
    public static Date parseDatetime(String date) {
        if (date == null) {
            return new Date();
        }
        DateFormat df = new SimpleDateFormat(FORMAT_DATETIME, Locale.CHINA);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 将Date转化时间戳
     *
     * @return 10位时间戳
     */
    public static long parseTimestamp(Date date) {
        if (date == null) {
            return 0;
        }
        return date.getTime() / 1000;
    }

    /**
     * 将yyyy-MM-dd HH:mm格式的字符串转化时间戳
     *
     * @return 10位时间戳
     */
    public static long parseTimestamp(String date) {
        if (date == null) {
            return 0;
        }
        DateFormat df = new SimpleDateFormat(FORMAT_DATETIME, Locale.CHINA);
        try {
            return df.parse(date).getTime() / 1000;
        } catch (ParseException e) {
            return 0;
        }
    }


    /**
     * @return yyyy-MM-dd HH:mm
     */
    public static String parseDatetimeYearMonth(long time) {
        if (time == 0) {
            return "";
        }
        if (String.valueOf(time).length() == 10) {
            time = time * 1000;
        }
        DateFormat df = new SimpleDateFormat(FORMAT_DATETIME_YEAR_MONTH, Locale.CHINA);
        return df.format(time);
    }

    /**
     * @return yyyy年MM月
     */
    public static String parseDatetime(long time) {
        if (time == 0) {
            return "";
        }
        if (String.valueOf(time).length() == 10) {
            time = time * 1000;
        }
        DateFormat df = new SimpleDateFormat(FORMAT_DATETIME_UI, Locale.CHINA);
        return df.format(time);
    }


    /**
     * 将日期时间文本转化时间戳
     *
     * @return
     */
    public static long parseToStamp(String date, String format) {
        if (date == null) {
            return 0;
        }
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return df.parse(date).getTime() / 1000;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 将时间戳转为日期时间文本
     */
    public static String getDateTimeText(long time, String format) {
        if (time == 0) {
            return "";
        }
        if (String.valueOf(time).length() == phpTimeStamp) {
            time = time * 1000;
        }
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        return df.format(time);
    }

    /**
     * 将Date转为日期时间文本
     */
    public static String getDateTimeText(Date dbDate, String format) {
        if (dbDate == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        return df.format(dbDate.getTime());
    }

    /**
     * 判断是不是同一天
     */
    public static boolean isSameDay(long time1, long time2) {
        time1 = time1 * 1000;
        time2 = time2 * 1000;
        long diffDays = (time1 - time2) / (24 * 60 * 60 * 1000);
        DateFormat df = new SimpleDateFormat("dd", Locale.CHINA);
        return diffDays == 0 && TextUtils.equals(df.format(time1), df.format(time2));
    }

    /**
     * 获取前几天的日期文本
     *
     * @param customDay 要获取的是前几天
     * @return
     */
    public static String getCustomDay(int customDay, String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -customDay);
        String date = new SimpleDateFormat(format).format(cal.getTime());
        System.out.println(date);
        return date;
    }

    public static long getCustomDay(int customDay) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -customDay);
        return cal.getTime().getTime() / 1000;
    }

    /**
     * 获取后几天的日期文本
     *
     * @param customDay 要获取的是后几天
     * @return
     */
    public static String getCustomDay2(int customDay, String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, customDay);
        String date = new SimpleDateFormat(format).format(cal.getTime());
        return date;
    }

    /*public static Date getCurrentYearFirst(){
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }*/

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        // 获得一个日历
        Calendar cal = Calendar.getInstance();
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 指示一个星期中的某天。
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获得本月第一天0点的10位的时间戳
     */
    @SuppressLint("WrongConstant")
    public static long getTimesMonthMorning(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        cal.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis() / 1000;
    }


    /**
     * 获得本月第一天0点的10位的时间戳
     */
    @SuppressLint("WrongConstant")
    public static long getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获得本月最后一天24点的10位时间戳
     */
    @SuppressLint("WrongConstant")
    public static long getTimesMonthNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTimeInMillis() / 1000 - 1000;
    }

    /**
     * 获得今天0点的时间戳
     */
    public static long getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获得今天24点的时间戳
     */
    public static long getTimesNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 与当前时间比较早晚
     *
     * @param time 需要比较的时间
     * @return 输入的时间比现在时间晚则返回true
     */
    public static boolean compareNowTime(String time) {
        boolean isDayu = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date parse = dateFormat.parse(time);
            Date parse1 = dateFormat.parse(getNowTime());

            long diff = parse1.getTime() - parse.getTime();
            if (diff <= 0) {
                isDayu = true;
            } else {
                isDayu = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isDayu;
    }

    /**
     * 获取当前时间文本
     *
     * @return
     */
    public static String getNowTime() {
        String timeString = null;
        Calendar calendar = Calendar.getInstance();
        String year = thanTen(calendar.get(Calendar.YEAR));
        String month = thanTen(calendar.get(Calendar.MONTH) + 1);
        String monthDay = thanTen(calendar.get(Calendar.DAY_OF_MONTH));
        timeString = year + "-" + month + "-" + monthDay;
        return timeString;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getNowTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当天0点的时间戳
     *
     * @return
     */
    public static long getNowTimeZeroStamp() {
        long zero = System.currentTimeMillis() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        return zero / 1000;
    }

    /**
     * 十一下加零
     *
     * @param str
     * @return
     */
    public static String thanTen(int str) {
        String string = null;
        if (str < 10) {
            string = "0" + str;
        } else {
            string = "" + str;
        }
        return string;
    }

    /**
     * 获取当年的第一天
     *
     * @param
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @param
     * @return
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);

    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        if(TextUtils.isEmpty(dateStr)){
            return "";
        }
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try{
            date = sdf.parse(dateStr);
            SimpleDateFormat s = new SimpleDateFormat(formatStr);
            return s.format(date);
        }catch (ParseException e){
            e.printStackTrace();
            return  "";
        }
    }
}