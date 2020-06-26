package com.zbb.common.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author sunflower
 * @className DateUtil
 * @description TODO
 * @date 2019/9/25 17:18
 */

public class DateUtil {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_PATTERN2 = "yyyy-MM-dd";

    /**
     * 以毫秒表示的时间
     */
    private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
    private static final long HOUR_IN_MILLIS = 3600 * 1000;
    private static final long MINUTE_IN_MILLIS = 60 * 1000;
    private static final long SECOND_IN_MILLIS = 1000;

    /**
     * 获取当前时间字符串
     *
     * @return string
     */
    public static String getNow() {
        return date2Str(new Date());
    }

    /**
     * 时间戳转日期格式
     *
     * @param pattern
     * @param timestamp
     * @return
     */
    public static String timestamp2Str(String pattern, int timestamp) {
        return new SimpleDateFormat(pattern).format(new Date(timestamp * 1000));
    }

    /**
     * 时间转字符串
     *
     * @param date date
     * @return String
     */
    public static String date2Str(Date date) {
        return date2Str(date, DEFAULT_PATTERN);
    }

    /**
     * 时间转字符串
     *
     * @param date    date
     * @param pattern pattern
     * @return String
     */
    public static String date2Str(Date date, String pattern) {
        SimpleDateFormat sim = new SimpleDateFormat(pattern);
        return sim.format(date);
    }


    /**
     * 字符串转Date
     *
     * @param str str
     * @return
     */
    public static Date str2Date(String str) {
        return str2Date(str, DEFAULT_PATTERN);
    }

    /**
     * 计算两个时间之间的差值，根据标志的不同而不同
     *
     * @param calSrc 减数
     * @param calDes 被减数
     * @return 两个日期之间的差值
     */
    public static int dateDiff(Calendar calSrc, Calendar calDes) {
        long millisDiff = getMillis(calSrc) - getMillis(calDes);
        return (int) (millisDiff / MINUTE_IN_MILLIS);
    }

    /**
     * 计算时间 差值
     *
     * @param beginDate 大时间
     * @param endDate   小时间
     * @return 时长(分钟)
     */
    public static int dateCalculation(Date beginDate, Date endDate) {
        Calendar bCalendar = Calendar.getInstance();
        bCalendar.setTime(beginDate);
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setTime(endDate);
        long millisDiff = getMillis(bCalendar) - getMillis(eCalendar);
        return (int) (millisDiff / MINUTE_IN_MILLIS);
    }

    /**
     * 指定日历的毫秒数
     *
     * @param cal 指定日历
     * @return 指定日历的毫秒数
     */
    private static long getMillis(Calendar cal) {
        return cal.getTime().getTime();
    }


    /**
     * 指定日期的毫秒数
     *
     * @param date 指定日期
     * @return 指定日期的毫秒数
     */
    private static long getMillis(Date date) {
        return date.getTime();
    }

    /**
     * 系统时间的毫秒数
     *
     * @return 系统时间的毫秒数
     */
    private static long getMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 字符串转date
     *
     * @param str     str
     * @param pattern pattern
     * @return Date
     */
    public static Date str2Date(String str, String pattern) {
        SimpleDateFormat sim = new SimpleDateFormat(pattern);
        try {
            return sim.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 给date日期增加指定天数
     *
     * @param date,days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /**
     * 给date日期增加指定分钟
     *
     * @param date,days
     * @return
     */
    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    /**
     * 给定时间与当前时间相隔的天数。
     *
     * @param date
     * @return >0 比今天晚多少天，=0同一天，<0比今天早多少天
     */
    public static int betweenDays(Date date) {
        return betweenDays(new Date(), date);
    }


    /**
     * 给定时间与当前时间相隔的天数。
     *
     * @param begin
     * @param end
     * @return >0 begin比end晚多少天，=0同一天，<0 begin比end早多少天
     */
    public static int betweenDays(Date begin, Date end) {
        try {
            begin = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(begin));
            end = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(end));
            long days = (end.getTime() - begin.getTime()) / (1000 * 3600 * 24);
            if (days == 0) {
                LocalDate a = begin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate b = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return Period.between(a, b).getDays();
            }
            return (int) days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 获取某天的起始时间
     *
     * @return
     */
    public static Date getStartTimeOfDay(Date date) {
        Calendar startOfDate = Calendar.getInstance();
        startOfDate.setTime(date);
        startOfDate.set(Calendar.HOUR_OF_DAY, 0);
        startOfDate.set(Calendar.MINUTE, 0);
        startOfDate.set(Calendar.SECOND, 0);
        startOfDate.set(Calendar.MILLISECOND, 0);
        return startOfDate.getTime();
    }

    /**
     * 获取某天最后一刻
     *
     * @return
     */
    public static Date getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        Date endOfDate = calendar.getTime();
        return endOfDate;
    }


    /**
     * 获取年 - 月 - 日
     *
     * @return YYyyMMDD
     */
    public static String getCodingDate() {
        String date;
        Calendar calendar = new GregorianCalendar();
        String year = getMDate();
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String dayOfMonth = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        date = year + month + dayOfMonth;
        return date;
    }


    /**
     * 获取四位数年
     *
     * @return YYYY
     */
    public static String getMDate() {
        Calendar calendar = new GregorianCalendar();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }
}
