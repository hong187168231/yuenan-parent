package com.indo.common.utils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class DateUtils {

    public static final String EARLIEST_DATE = "2019-01-01";

    public static final Long EARLIEST_TIME = 1546272000000L;

    public final static long ONE_DAY_SECONDS = 86400;

    public final static int ONE_DAY_MINUTE = 24 * 60;

    public final static String shortFormat = "yyyyMMdd";

    public final static String longFormat = "yyyyMMddHHmmss";

    public final static String longFormat1 = "yyyyMMddHHmmsss";

    public final static String webFormat = "yyyy-MM-dd";

    public final static String timeFormat = "HHmmss";

    public static final String TIME_PATTERN = "HH:mm:ss";

    public final static String monthFormat = "yyyyMM";

    public final static String yearFormat = "yyyy";

    public final static String chineseDtFormat = "yyyy年MM月dd日";

    public final static String newFormat = "yyyy-MM-dd HH:mm:ss";

    public final static String newFormat2 = "yyyy-MM-dd HH:mm";

    public final static String newFormat3 = "yyyy-MM-dd HH";

    public final static String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public final static String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public final static String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";

    public final static String ISO8601_DATE_FORMAT1 = "YYYY-MM-DD'T'hh:mm:ss.sss";

    public final static String RFC3339_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public final static String HOURMINUTEFormat = "HH:mm";

    static {
        /*
         * dateFormat = new SimpleDateFormat(shortFormat);
         * dateFormat.setLenient(false); longDateFormat = new
         * SimpleDateFormat(longFormat); longDateFormat.setLenient(false);
         * dateWebFormat = new SimpleDateFormat(webFormat);
         * dateWebFormat.setLenient(false);
         */
    }


    public static long ONE_DAY_MILL_SECONDS = 86400000;

    public static long ONE_WEEK_MILL_SECONDS = 86400000 * 7;


    public static Date getDateMinuteMinTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Date getDateMinuteMaxTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    public static Date addMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 获取一天最早的时间点
     */
    public static Date getDateMinTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateMinTime(Long millsecord) {
        return getDateMinTime(getDate(millsecord));
    }

    public static Date getDateMinTime(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(webFormat);
        return getDateMinTime(format.parse(dateStr));
    }

    /**
     * 获取一天最晚的时间点
     */
    public static Date getDateMaxTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取一周中的第几天（周日开始，1-7）
     */
    public static Date getDateDayOfWeek(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return calendar.getTime();
    }

    /**
     * 获取本周的开始时间
     */
    @SuppressWarnings("unused")
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayOfWeek);
        return getDateMinTime(cal.getTime());
    }


    /**
     * 获取本周的结束时间
     */
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDateMaxTime(weekEndSta);
    }


    /**
     * 获取上周时间
     */
    @SuppressWarnings("unused")
    public static Date getBeginDayOfLastWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return cal.getTime();
    }


    // 获得本月第一天0点时间
    public static Date getMonthBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONDAY),
                cal.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();

    }


    // 获得本月最后一天24点时间
    public static Date getMonthEnd() {
        Calendar cal = Calendar.getInstance();

        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONDAY),
                cal.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();

    }

    /**
     * 获取当年的开始时间戳
     */
    public static Date getYearStartTime() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.DATE, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当年的最后时间戳
     */
    public static Date getYearEndTime() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();

    }


    /**
     * 获取上月的开始时间
     */
    public static Date getBeginDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        return getDateMinTime(calendar.getTime());
    }


    /**
     * 获取上月的结束时间
     */
    public static Date getEndDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
        return getDateMaxTime(calendar.getTime());
    }


    /**
     * 获取今年是哪一年
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }


    /**
     * 获取本月是哪一月
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * 获取今天是哪天
     */
    public static int getNowDay() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(GregorianCalendar.DAY_OF_YEAR);
    }


    /**
     * 获取当天的开始时间
     */
    public static Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当天的结束时间
     */
    public static Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }


    public static Date getDateMaxTime(Long millsecord) {
        return getDateMaxTime(getDate(millsecord));
    }

    public static Date getDateMaxTime(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(webFormat);
        return getDateMaxTime(format.parse(dateStr));
    }


    public static Long getDateMinuteLong(Date date) {
        return getDateMinuteMinTime(date).getTime();
    }

//    public static void main(String[] args) {
//    	Date now = new Date();
//		System.out.println(now);
//		System.out.println(getDateMinTime(now));
//		System.out.println(getDateMaxTime(now));
//		
//	}

    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime(String format) {
        return format(new Date(), format);
    }

    public static Date getDate(long millsecord) {
        return new Date(millsecord);
    }

    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df;
    }


    //日期格式转换，返回日期类型
    public static Date formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        ParsePosition pos = new ParsePosition(0);
        Date newDate = formatter.parse(dateString, pos);
        return newDate;
    }

    //日期格式转换，返回String
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(format).format(date);
    }

    public static String formatYYYYMMDD(Date date) {
        if (date == null) {
            return null;
        }
        return String.format("%tF", date);
    }

    public static String formatByLong(long date, String format) {
        return new SimpleDateFormat(format).format(new Date(date));
    }

    public static String formatByString(String date, String format) {
        if (StringUtils.isNotBlank(date)) {
            return new SimpleDateFormat(format).format(new Date(NumberUtils
                    .toLong(date)));
        }
        return StringUtils.EMPTY;
    }


    public static String formatShortFormat(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(shortFormat).format(date);
    }

    public static Date parseDateNoTime(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(shortFormat);

        if ((sDate == null) || (sDate.length() < shortFormat.length())) {
            throw new ParseException("length too little", 0);
        }

        if (!StringUtils.isNumeric(sDate)) {
            throw new ParseException("not all digit", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static Date parseLong(long time) {
        return new Date(time);
    }

    public static Date parseDateNoTime(String sDate, String format)
            throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("Null format. ", 0);
        }

        DateFormat dateFormat = new SimpleDateFormat(format);

        if ((sDate == null) || (sDate.length() < format.length())) {
            throw new ParseException("length too little", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static Date parseDateNoTimeWithDelimit(String sDate, String delimit)
            throws ParseException {
        sDate = sDate.replaceAll(delimit, "");

        DateFormat dateFormat = new SimpleDateFormat(shortFormat);

        if ((sDate == null) || (sDate.length() != shortFormat.length())) {
            throw new ParseException("length not match", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static Date parseDateLongFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(longFormat);
        Date d = null;

        if ((sDate != null) && (sDate.length() == longFormat.length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }

        return d;
    }

    public static Date parseDateNewFormat(String sDate) {
        Date d = parseDateHelp(sDate, newFormat);
        if (null != d) {
            return d;
        }
        d = parseDateHelp(sDate, newFormat2);
        if (null != d) {
            return d;
        }
        d = parseDateHelp(sDate, newFormat3);
        if (null != d) {
            return d;
        }
        d = parseDateHelp(sDate, webFormat);
        if (null != d) {
            return d;
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat(newFormat);
            return dateFormat.parse(sDate);
        } catch (ParseException ex) {
            return null;
        }
    }

    private static Date parseDateHelp(String sDate, String format) {
        if ((sDate != null) && (sDate.length() == format.length())) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * 计算当前时间几小时之后的时间
     */
    public static Date addHours(Date date, long hours) {
        return addMinutes(date, hours * 60);
    }

    /**
     * 计算当前时间几分钟之后的时间
     */
    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, minutes * 60);
    }

    /**
     *
     */

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + (secs * 1000));
    }

    /**
     * 判断输入的字符串是否为合法的小时
     *
     * @return true/false
     */
    public static boolean isValidHour(String hourStr) {
        if (!StringUtils.isEmpty(hourStr) && StringUtils.isNumeric(hourStr)) {
            int hour = new Integer(hourStr).intValue();

            if ((hour >= 0) && (hour <= 23)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断输入的字符串是否为合法的分或秒
     *
     * @return true/false
     */
    public static boolean isValidMinuteOrSecond(String str) {
        if (!StringUtils.isEmpty(str) && StringUtils.isNumeric(str)) {
            int hour = new Integer(str).intValue();

            if ((hour >= 0) && (hour <= 59)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 取得新的日期
     *
     * @param date1 日期
     * @param days  天数
     * @return 新的日期
     */
    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, days * ONE_DAY_SECONDS);
    }

    /**
     * 取得从当前开始多少天后的新日期
     *
     * @param days 天数
     * @return 新的日期
     */
    public static Date addDaysFromNow(long days) {
        return addSeconds(new Date(System.currentTimeMillis()), days
                * ONE_DAY_SECONDS);
    }

    public static String getTomorrowDateString(String sDate)
            throws ParseException {
        Date aDate = parseDateNoTime(sDate);

        aDate = addSeconds(aDate, ONE_DAY_SECONDS);

        return getDateString(aDate);
    }

    public static String getLongDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(longFormat);

        return getDateString(date, dateFormat);
    }

    public static String getNewFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(newFormat);
        return getDateString(date, dateFormat);
    }

    public static String getFullDateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(FULL_DATE_FORMAT);
        return getDateString(date, dateFormat);
    }


    public static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    public static String getYesterDayDateString(String sDate)
            throws ParseException {
        Date aDate = parseDateNoTime(sDate);

        aDate = addSeconds(aDate, -ONE_DAY_SECONDS);

        return getDateString(aDate);
    }

    /**
     * @return 当天的时间格式化为"yyyyMMdd"
     */
    public static String getDateString(Date date) {
        DateFormat df = getNewDateFormat(shortFormat);
        return df.format(date);
    }

    public static String getWebDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(webFormat);
        return getDateString(date, dateFormat);
    }

    /**
     * 取得“X年X月X日”的日期格式
     */
    public static String getChineseDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(chineseDtFormat);

        return getDateString(date, dateFormat);
    }

    public static String getTodayString() {
        DateFormat dateFormat = getNewDateFormat(shortFormat);

        return getDateString(new Date(), dateFormat);
    }

    public static String getTimeString(Date date) {
        DateFormat dateFormat = getNewDateFormat(timeFormat);

        return getDateString(date, dateFormat);
    }

    public static String getBeforeDayString(int days) {
        Date date = new Date(System.currentTimeMillis()
                - (ONE_DAY_MILL_SECONDS * days));
        DateFormat dateFormat = getNewDateFormat(shortFormat);

        return getDateString(date, dateFormat);
    }

    public static Date getBeforeDays(int days) {
        Date date = new Date(System.currentTimeMillis()
                - (ONE_DAY_MILL_SECONDS * days));
        return date;
    }

    /**
     * 取得两个日期间隔毫秒数（日期1-日期2）
     *
     * @return 间隔毫秒数
     */
    public static long getDiffMilliseconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis());
    }

    /**
     * 取得两个日期间隔秒数（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔秒数
     */
    public static long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
    }

    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis())
                / (60 * 1000);
    }

    /**
     * 取得两个日期的间隔天数
     *
     * @return 间隔天数
     */
//    public static long getDiffDays(Date one, Date two) {
//        Calendar sysDate = new GregorianCalendar();
//
//        sysDate.setTime(one);
//
//        Calendar failDate = new GregorianCalendar();
//
//        failDate.setTime(two);
//        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis())
//                / (24 * 60 * 60 * 1000);
//    }
    public static String getBeforeDayString(String dateString, int days) {
        Date date;
        DateFormat df = getNewDateFormat(shortFormat);

        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            date = new Date();
        }

        date = new Date(date.getTime() - (ONE_DAY_MILL_SECONDS * days));

        return df.format(date);
    }

    public static boolean isValidShortDateFormat(String strDate) {
        if (strDate.length() != shortFormat.length()) {
            return false;
        }

        try {
            Integer.parseInt(strDate); // ---- 避免日期中输入非数字 ----
        } catch (Exception NumberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(shortFormat);

        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidShortDateFormat(String strDate,
                                                 String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidShortDateFormat(temp);
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     */
    public static boolean isValidLongDateFormat(String strDate) {
        if (strDate.length() != longFormat.length()) {
            return false;
        }

        try {
            Long.parseLong(strDate); // ---- 避免日期中输入非数字 ----
        } catch (Exception NumberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(longFormat);

        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     */
    public static boolean isValidLongDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidLongDateFormat(temp);
    }

    public static String getShortDateString(String strDate) {
        return getShortDateString(strDate, "-|/");
    }

    public static String getShortDateString(String strDate, String delimiter) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        String temp = strDate.replaceAll(delimiter, "");

        if (isValidShortDateFormat(temp)) {
            return temp;
        }

        return null;
    }

    public static String getShortFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat df = getNewDateFormat(shortFormat);

        return df.format(cal.getTime());
    }

    public static String getWebTodayString() {
        DateFormat df = getNewDateFormat(webFormat);

        return df.format(new Date());
    }

    public static String getWebFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat df = getNewDateFormat(webFormat);

        return df.format(cal.getTime());
    }

    public static String convert(String dateString, DateFormat formatIn,
                                 DateFormat formatOut) {
        try {
            Date date = formatIn.parse(dateString);

            return formatOut.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static String convert2WebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(webFormat);

        return convert(dateString, df1, df2);
    }

    public static String convert2ChineseDtFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(chineseDtFormat);

        return convert(dateString, df1, df2);
    }

    public static String convertFromWebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat(shortFormat);
        DateFormat df2 = getNewDateFormat(webFormat);

        return convert(dateString, df2, df1);
    }

    public static boolean webDateNotLessThan(String date1, String date2) {
        DateFormat df = getNewDateFormat(webFormat);

        return dateNotLessThan(date1, date2, df);
    }

    /**
     *
     */
    public static boolean dateNotLessThan(String date1, String date2,
                                          DateFormat format) {
        try {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);

            if (d1.before(d2)) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getEmailDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

        todayStr = sdf.format(today);
        return todayStr;
    }

    public static String getSmsDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");

        todayStr = sdf.format(today);
        return todayStr;
    }

    public static String formatMonth(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(monthFormat).format(date);
    }

    /**
     * 获取系统日期的前一天日期，返回Date
     */
    public static Date getBeforeDate() {
        Date date = new Date();

        return new Date(date.getTime() - (ONE_DAY_MILL_SECONDS));
    }

    /**
     * 得到系统当前的时间
     */
    public static String currentTime(String format) {
        if (StringUtils.isBlank(format)) {
            return format(new Date(), newFormat);
        } else {
            return format(new Date(), format);
        }
    }

    /**
     * 获取任意时间的上一个月 描述:<描述函数实现的功能>.
     */
    public static String getLastMonth(String repeatDate) {
        String lastMonth = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dft = new SimpleDateFormat(monthFormat);
        int year = Integer.parseInt(repeatDate.substring(0, 4));
        String monthsString = repeatDate.substring(4, 6);
        int month;
        if ("0".equals(monthsString.substring(0, 1))) {
            month = Integer.parseInt(monthsString.substring(1, 2));
        } else {
            month = Integer.parseInt(monthsString.substring(0, 2));
        }
        cal.set(year, month - 2, Calendar.DATE);
        lastMonth = dft.format(cal.getTime());
        return lastMonth;
    }


    /**
     * Returns true if endDate is after startDate or if startDate equals endDate. Returns false if
     * either value is null. If equalOK, returns true if the dates are equal.
     **/
    public static boolean isValidDateRange(Date startDate, Date endDate,
                                           boolean equalOK) {
        // false if either value is null
        if (startDate == null || endDate == null) {
            return false;
        }

        if (equalOK) {
            // true if they are equal
            if (startDate.equals(endDate)) {
                return true;
            }
        }

        // true if endDate after startDate
        if (endDate.after(startDate)) {
            return true;
        }

        return false;
    }

    public static boolean isYesterday(long time) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(System.currentTimeMillis());

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
                .get(Calendar.DAY_OF_YEAR) + 1 == cal2
                .get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isTomorrow(long time) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(System.currentTimeMillis());

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
                .get(Calendar.DAY_OF_YEAR) - 1 == cal2
                .get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 检查指定的时间与当前时间的间隔是否大于interval
     */
    public static boolean compareWithNow(long time, long interval) {
        if ((System.currentTimeMillis() - time) > interval) {
            return true;
        }
        return false;
    }

    /**
     * 当前时间与指定时间比，还有几天
     */
    public static long getDiffDaysWithNow(long target) {
        long t1 = target - System.currentTimeMillis();
        if (t1 < 0) {
            return -1;
        }
        return t1 / (24 * 60 * 60 * 1000);
    }

    /**
     * <pre>
     * 指定时间据当前时间已过去多少天
     * 不足的一天的天数不算入结果
     * 如 2.99天--->2天
     * </pre>
     */
    public static long getPastDaysWithNow(long target) {
        long t1 = System.currentTimeMillis() - target;
        if (t1 < 0) {
            return -1;
        }
        return t1 / (24 * 60 * 60 * 1000);
    }

    /**
     * <pre>
     * 输入时间和当前时间比较
     * 多于24小时，--> X天
     * 多于1小时， --> X小时
     * 多于1分钟， --> X分钟
     * 多于1秒， --> X秒
     * 小于1秒， --> 0
     * 如果输入时间比当前时间小，--> 0
     * </pre>
     */
    public static String getDynamicLeftTime(long target) {
        long t1 = target - System.currentTimeMillis();
        if (t1 < 0) {
            return "0";
        }
        long days = t1 / (24 * 60 * 60 * 1000);
        if (days > 0) {
            return days + "天";
        }
        long hours = t1 / (60 * 60 * 1000);
        if (hours > 0) {
            return hours + "小时";
        }
        long minutes = t1 / (60 * 1000);
        if (minutes > 0) {
            return minutes + "分钟";
        }
        long seconds = t1 / (1000);
        if (seconds > 0) {
            return seconds + "秒";
        }
        return "0";
    }

    public static String getDynamicPassTime(long target) {
        String meaningfulTimeStr = null;
        long curTime = System.currentTimeMillis();
        long timeGap = (curTime - target) / 1000;
        if (timeGap > 60 * 60 * 24 * 2) {
            // 超过昨天前，显示日期
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            Date targetDate = new Date(target);
            meaningfulTimeStr = formater.format(targetDate);
        } else if (timeGap > 60 * 60 * 24 && timeGap <= 60 * 60 * 24 * 2) {// 小于2天，显示昨天
            meaningfulTimeStr = "昨天";
        } else if (timeGap > 60 * 60 && timeGap <= 60 * 60 * 24) { // 小于一天，显示x小时前
            Integer hourNum = (int) (timeGap / (60 * 60));
            meaningfulTimeStr = hourNum + "小时前";
        } else if (timeGap > 60 * 5 && timeGap <= 60 * 60) { // 小于一小时，显示x分钟前
            Integer minNum = (int) (timeGap / 60);
            meaningfulTimeStr = minNum + "分钟前";
        } else if (timeGap <= 60 * 5) { // 小于5分钟，显示刚刚
            meaningfulTimeStr = "刚刚";
        }

        return meaningfulTimeStr;

    }

    /**
     * 获取前一天日期
     */
    public static Date getBeforeDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取下一天
     */
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }


    /**
     * 在指定时间加多少秒
     */
    public static Date addDataSecond(Date date, int second) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.SECOND, second);
        return ca.getTime();
    }


    public static Date getWeekMinTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        return calendar.getTime();
    }

    public static Date getMonthMinTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取每月的最大时间
     */
    public static Date getMonthMaxTime(Date date) {
        Date date1 = getMaxMonthDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    /**
     * 获取任意时间的月的最后一天
     */
    private static Date getMaxMonthDate(Date repeatDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(repeatDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }


    public static Date[] getDateSe(String dateType) {
        Date[] date = new Date[2];

        if ("1".equals(dateType)) {
            // 昨天
            date[0] = getDateMinTime(DateUtils.getBeforeDate());
            date[1] = getDateMaxTime(DateUtils.getBeforeDate());

        } else if ("2".equals(dateType)) {
            // 周榜单 计算上周一到本周日
            Date lastWeek = getBeginDayOfLastWeek();
            Date lastMonday = getDateMinTime(getDateDayOfWeek(lastWeek, 2));
            date[0] = lastMonday;
            //本周日
            date[1] = getDateMaxTime(getDateDayOfWeek(getNowDate(), 1));
        } else if ("3".equals(dateType)) {
            // 月榜单 计算上个月时间
            date[0] = getBeginDayOfLastMonth();
            date[1] = getEndDayOfLastMonth();
        }
        log.info("date[0] :{} , date[1] :{}", date[0], date[1]);
        return date;
    }


    public static Date parseDateFromStringYYYYmmdd(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(webFormat);
        return format.parse(date);
    }

    public static Duration differentDate(Date date1, Date date2) {
        LocalDateTime ldt1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime ldt2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return Duration.between(ldt1, ldt2);
    }

    /**
     * 两个日期相差的天数
     */
    public static int differentDays(Date date1, Date date2) {
        if (date1.after(date2)) {
            Date date = date1;
            date1 = date2;
            date2 = date;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            // 不同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
                {
                    timeDistance += 366;
                } else // 不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {
            //同年
            return day2 - day1;
        }
    }


    public static String CSTDateTOutc(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.UTC_DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 8);

        return sdf.format(calendar.getTime());
    }

    public static Date utcTOCSTDate(String utcTime) throws ParseException {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.UTC_DATE_FORMAT);
        date = sdf.parse(utcTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }


    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }


    public static boolean sameYearAndMonth(Date date1, Date date2) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        if (year1 != year2) {
            return false;
        }
        int month1 = calendar1.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);
        if (month1 == month2) {
            return true;
        }
        return false;
    }

    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    public static Date getTodayStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getYesterdayStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getYesterdayEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取带时区格式的时间
     */
    public static String getTimeAndZone() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        ZoneId zoneId = ZoneId.ofOffset("GMT",ZoneOffset.ofHours(8));
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        df.setTimeZone(timeZone);
        return df.format(cal.getTime());
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date smdate, Date bdate) {
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));

            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            log.error("计算时间间隔天数异常======,smdate{},bdate {}", smdate, bdate);
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int daysBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            long to = df.parse(smdate).getTime();
            long from = df.parse(bdate).getTime();
            return (int) Math.abs((from - to) / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            log.error("计算时间间隔天数异常======,smdate{},bdate {}", smdate, bdate);
        }
        return 0;
    }

    public static Long getUTC8TimeLength10() {
        LocalDateTime utcNow = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime utc4Time = utcNow.plusHours(8);
        return utc4Time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000;
    }

    /**
     * 获取北京时间 unix时间戳
     * @return
     */
    public static long getGMT8TimeLength10(){
        long epoch = 0;
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.CHINESE);
            Calendar day = Calendar.getInstance();
            day.set(Calendar.YEAR, cal.get(Calendar.YEAR));
            day.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            day.set(Calendar.DATE, cal.get(Calendar.DATE));
            day.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            day.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            day.set(Calendar.SECOND, cal.get(Calendar.SECOND));
            Date gmt8 = day.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String t = df.format(gmt8);
            epoch = df.parse(t).getTime() / 1000;
        } catch (Exception e) {
            System.out.println("获取GMT8时间 getGMT8Time() error !");
            e.printStackTrace();
        }
        return  epoch;
    }

    /**

     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"<br />

     * 如果获取失败，返回null

     * @return

     */

    public static Long getUTCTimeStr(String formatStr) {
        DateFormat format = new SimpleDateFormat(formatStr);
        StringBuffer UTCTimeBuffer = new StringBuffer();

        // 1、取得本地时间：

        Calendar cal = Calendar.getInstance() ;

        // 2、取得时间偏移量：

        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

        // 3、取得夏令时差：

        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：

        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        int year = cal.get(Calendar.YEAR);

        int month = cal.get(Calendar.MONTH)+1;

        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        int minute = cal.get(Calendar.MINUTE);

        int millisecond = cal.get(Calendar.MILLISECOND);

        UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day) ;

        UTCTimeBuffer.append(" ").append(hour).append(":").append(minute).append(":").append(millisecond);

        try{

            return format.parse(UTCTimeBuffer.toString()).getTime()/1000;

//            return UTCTimeBuffer.toString() ;

        }catch(ParseException e)

        {

            e.printStackTrace() ;

        }

        return null ;

    }

    /**

     * 将UTC时间转换为东八区时间

     * @param UTCTime

     * @return

     */

    public static String getLocalTimeFromUTC(String UTCTime,DateFormat format){

        java.util.Date UTCDate = null ;

        String localTimeStr = null ;

        try {

            UTCDate = format.parse(UTCTime);

            format.setTimeZone(TimeZone.getTimeZone("GMT-8")) ;

            localTimeStr = format.format(UTCDate) ;

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return localTimeStr ;

    }

    public static void main(String[] args) {
//        System.out.println(daysBetween("2022-01-25", "2022-01-28"));
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(getUTCTimeStr(DateUtils.newFormat));
//        System.out.println(getLocalTimeFromUTC("637922749635693535",format));
//DateUtils.getTimeAndZone();
//        ZonedDateTime zbj = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sssXXX");

//                System.out.println("--------------1-----"+DateTimeFormatter.ISO_OFFSET_DATE.format(zbj));
//        System.out.println("--------------2-----"+DateTimeFormatter.ISO_ZONED_DATE_TIME.format(zbj));
//        System.out.println("--------------3-----"+df.format(zbj));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        ZoneId zoneId = ZoneId.ofOffset("GMT",ZoneOffset.ofHours(8));
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        df.setTimeZone(timeZone);
        System.out.println("--------------3-----"+df.format(cal.getTime()));
    }

}