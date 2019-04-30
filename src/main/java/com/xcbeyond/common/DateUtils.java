package com.xcbeyond.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @Auther: xcbeyond
 * @Date: 2019/4/30 15:17
 */
public class DateUtils {

    /**
     * 获取当前时间，yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDate() {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // new Date()为获取当前系统时间
        return df.format(new Date());
    }

    /**
     * 根据输入的日期格式获得系统当前日期
     * @param format
     *        时间格式化，如："yyyy-MM-dd","yyyy/MM/dd","yyyyMMdd"
     * @return
     */
    public static String getCurrentDate(String format) {
        String date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.format(new Date());
        } catch (Exception e) {
            // 默认格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            date = sdf.format(new Date());
        }
        return date;
    }

    /**
     * 获取当前时间（yyyy年MM月dd日）
     * @return
     */
    public static String getYYYYMMDDCHN() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间（yyyyMMdd）
     * @return
     */
    public static String getYYYYMMDD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间（格式自定义）
     * @param type 时间格式化字符串，如："yyyy-MM-dd"
     * @return
     */
    public static String getYYYYMMDD(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间（yyyyMMddHHmmssSSS）
     * @return
     */
    public static String getYYYYMMDDHHMMssSSS() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间 hh24mmss
     * @return
     */
    public static String getHMS() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }


    /**
     * 取得当天之后几个月的天数
     * @param yyyyMM
     * @return
     */
    public static int getDaysOfMonthNext(String yyyyMM, int months) {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMM");
        int retValue = 0;
        for (int i = 0; i <= months; i++) {
            rightNow.add(rightNow.MONTH, i);
            yyyyMM = simpleDate.format(rightNow.getTime());
            String now = getYYYYMMDD("yyyyMMdd");
            if (i == 0) {
                int dd = Integer.parseInt(now.substring(6));
                retValue += (getDaysOfMonth(yyyyMM) - dd + 1);
            } else if (i == months) {
                int dd = Integer.parseInt(now.substring(6));
                retValue += dd - 1;
            } else {
                retValue += getDaysOfMonth(yyyyMM);
            }
        }
        return retValue;
    }

    /**
     * 取得某年某月有多少天
     * @param yyyyMM
     *          年月，注意时间格式 yyyyMM
     * @return
     */
    public static int getDaysOfMonth(String yyyyMM) {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMM"); // 如果写成年月日的形式的话，要写小d，如："yyyy/MM/dd"
        try {
            rightNow.setTime(simpleDate.parse(yyyyMM)); // 要计算你想要的月份，改变这里即可
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    /**
     * 两时间相差的天数 注：日期格式yyyyMMdd
     * @param startDay
     * @param endDay
     * @return
     */
    public static long getDiffDays(String startDay, String endDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        long days = 0;
        try {
            Date s = sdf.parse(startDay);
            Date e = sdf.parse(endDay);
            long sd = s.getTime();
            long ed = e.getTime();
            long diff;
            if (ed > sd) {
                diff = ed - sd;
            } else {
                diff = sd - ed;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days + 1;
    }

    /**
     * 比较两个日期是否在一个月内
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return true说明超过一个月
     */
    public static boolean compareDateInMonth(String beginDate, String endDate) {
        boolean bool = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(beginDate));
            int syear = c.get(Calendar.YEAR);
            int smonth = c.get(Calendar.MONTH);
            int sday = c.get(Calendar.DATE);
            c.setTime(sdf.parse(endDate));
            int eyear = c.get(Calendar.YEAR);
            int emonth = c.get(Calendar.MONTH);
            int eday = c.get(Calendar.DATE);
            if (syear != eyear) {
                if (eyear - syear == 1) {
                    if (smonth - emonth == 11 && eday < sday) {
                        bool = false;
                    } else {
                        bool = true;
                    }
                } else {
                    bool = true;
                }
            } else {
                if (emonth - smonth > 1) {
                    bool = true;
                } else if (emonth - smonth == 1) {
                    if (eday >= sday) {
                        bool = true;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 取到某个日期months个月之后日期
     *
     * @param date
     *            格式yyyyMMdd
     * @param months
     *            月数
     * @return
     */
    public static String getDateOfMonth(String date, int months) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date toDate = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(toDate);
            c.add(Calendar.MONTH, months);
            date = sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式化日期为YYYY年MM月DD日 例如 20130805格式化为2013年08月04日
     * @param str
     *            格式YYYYMMDD
     * @return date 格式YYYY年MM月DD日
     */
    public static String formatDateCHN(String str) {
        String format = "yyyyMMdd";
        if (str.contains("-")) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf1.format(date);
    }

    /**
     * 格式化时间为HH:MM:SS,如果数据不为6位，前补0 例如 091516格式化为09:15:16
     * @param str
     *            格式HHMMSS
     * @return time 格式HH:MM:SS
     */
    public static String formatTime(String str) {
        String format = "HHmmss";
        if (str.length() == 5) {
            str = "0" + str;
        }
        if (str.length() == 9) {
            format = "HHmmssSSS";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        try {
            str = sdf1.format(sdf.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 计算两个日期相隔天数
     * @param smdate
     *            小日期
     * @param bdate
     *            大日期
     * @return int
     * @throws ParseException
     */
    public static int getDaysBetween(String smdate, String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;
        try {
            cal.setTime(sdf.parse(smdate));
            time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            time2 = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 比较两个日期
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     *            结束日期 > 开始日期 返回大于0
     *            结束日期 < 开始日期 返回小于0
     * @return int
     * @throws ParseException
     */
    public static long compareDate(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;
        try {
            cal.setTime(sdf.parse(startDate));
            time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(endDate));
            time2 = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time2 - time1;
    }

    /**
     * 计算两个日期相隔月数
     * @param smdate
     *            小日期
     * @param bdate
     *            大日期
     * @return int
     * @throws ParseException
     */
    public static int getMonthsBetween(String smdate, String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        int compMonth = 0, compYear = 0;
        try {
            calStart.setTime(sdf.parse(smdate));
            calEnd.setTime(sdf.parse(bdate));
            compMonth = calEnd.get(Calendar.MONTH)
                    - calStart.get(Calendar.MONTH);
            compYear = calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        compMonth = compYear * 12 + compMonth;
        return compMonth;
    }

    /**
     * 格式化日期 传入19900101 返回 1990-01-01 传入为空或null 返回空
     * @param str
     * @return
     */
    public static String formatDate(String str) {
        String format = "yyyyMMdd";
        if (null == str) {
            return "";
        } else if (str.equals("")) {
            return "";
        } else if (str.contains("-")) {
            return str;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = new Date();
            try {
                date = sdf.parse(str);
            } catch (ParseException e) {
                return "";
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            return sdf1.format(date);
        }
    }

    /**
     * 格式化日期型字符串
     * @param date
     * @param dateFormatFrom
     * @param dateFormatTo
     * @return
     */
    public static String formatDate(String date, String dateFormatFrom,
                                    String dateFormatTo) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatFrom);
        Date temp = new Date();
        try {
            temp = sdf.parse(date);
        } catch (ParseException e) {
            return date;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormatTo);
        return sdf1.format(temp);
    }
}
