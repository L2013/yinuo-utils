package com.yinuo.utils.toolbox;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liang
 * @create 2020-05-15 8:44
 * hutool DateUtils 的简化封装
 */
public class DateKit {

    private static final String YYYY_MM_DD = "yyyyMMdd";
    private static final String YYYY_MM = "yyyyMM";

    public static Date parseDate(String date) {
        return DateUtil.parse(date, YYYY_MM_DD);
    }

    public static Date parseDate(String date, String format) {
        return DateUtil.parse(date, format);
    }

    public static String formatDate(Date date) {
        return DateUtil.format(date, YYYY_MM_DD);
    }

    public static String formatDate(Date date, String format) {
        return DateUtil.format(date, format);
    }

    public static Date parseMonth(String date) {
        return DateUtil.parse(date, YYYY_MM);
    }

    public static String formatMonth(Date date) {
        return DateUtil.format(date, YYYY_MM);
    }

    /**
     * 产生指定日期区间的日期队列
     *
     * @param from
     * @param to
     * @return
     */
    public static List<String> getDateInRange(String from, String to) {
        return getDateInRange(parseDate(from), parseDate(to));
    }

    /**
     * 产生指定日期区间的日期队列
     *
     * @param fromDt
     * @param toDt
     * @return
     */
    public static List<String> getDateInRange(Date fromDt, Date toDt) {
        List<String> rt = new ArrayList<>();
        for (int i = 0; i <= DateUtil.betweenDay(fromDt, toDt, true); i++) {
            rt.add(formatDate(DateUtil.offsetDay(fromDt, i)));
        }
        return rt;
    }

    public static boolean isWeekend(Date date) {
        return DateUtil.dayOfWeekEnum(date) == Week.SUNDAY || DateUtil.dayOfWeekEnum(date) == Week.SATURDAY;
    }

    public static String getToday() {
        return formatDate(new Date());
    }

    public static String getYesterday() {
        return formatDate(getOffsetDate(new Date(), -1));
    }

    public static Date getOffsetMonth(Date refDate, int i) {
        return DateUtil.offsetMonth(refDate, i);
    }

    public static Date getMonthFirstDay(String month) {
        return DateUtil.beginOfMonth(parseMonth(month));
    }

    public static Date getMonthLastDay(String month) {
        return DateUtil.endOfMonth(parseMonth(month));
    }

    public static Date getOffsetDate(Date refDate, int i) {
        return DateUtil.offsetDay(refDate, i).toJdkDate();
    }

    public static List<String> getMonthSince(String from) {
        return getMonthBetween(from, formatMonth(new Date()));
    }

    public static List<String> getMonthBetween(String from, String to) {
        List<String> r = new ArrayList<String>();
        Date begin = parseMonth(from);
        long n = DateUtil.betweenMonth(begin, parseMonth(to), true);
        for (int i = 0; i < n; i++) {
            Date t = DateUtil.offsetMonth(begin, i);
            String curr = formatMonth(t);
            r.add(curr);
        }
        return r;
    }

    public static List<String> getDateSince(String from) {
        return getDateInRange(from, formatDate(new Date()));
    }

    /**
     * 校验日期字符串合法性
     * @param dateStr
     * @return
     */
    public static boolean isLegalDate(String dateStr) {
        int legalLen = YYYY_MM_DD.length();
        if ((dateStr == null) || (dateStr.length() != legalLen)) {
            return false;
        }
        try {
            Date date = parseDate(dateStr);
            return dateStr.equals(formatDate(date));
        } catch (Exception e) {
            return false;
        }
    }

}
