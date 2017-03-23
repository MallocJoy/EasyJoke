package com.utouu.easyjoke.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Create by 黄思程 on 2017/3/20  13:26
 * Function：
 * Desc：时间工具类
 */
public class TimeUtils {

    public static final int HOUR_SECONDS = 60 * 60;
    public static final int DAY_SECONDS = 60 * 60;

    //毫秒转秒
    public static String long2String(long time) {

        //毫秒转秒
        int sec = (int) time / 1000;
        int min = sec / 60;    //分钟
        sec = sec % 60;        //秒
        if (min < 10) {    //分钟补0
            if (sec < 10) {    //秒补0
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {    //秒补0
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }
    }

    /**
     * 返回当前时间的格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 返回当前时间的格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(time);
    }

    /**
     * 返回当前时间的格式为 yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentSimpleTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    /**
     * 格式化时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * 格式化倒计时
     *
     * @param startTime
     * @param interval  间隔时间 单位为分
     * @param format
     * @return
     */
    public static String formatCountDown(long startTime, int interval, String format) {
        long endTime = startTime + interval * 60l * 1000l;
        long offsetTime = endTime - System.currentTimeMillis();
        if (offsetTime <= 0) {
            return "00:00:00";
        }
        return formatTime(offsetTime, "HH:mm:ss");
    }

    /**
     * 格式化倒计时
     *
     * @param offsetTime
     * @return
     */
    public static String formatCountDown(long offsetTime) {
        return formatTime(offsetTime, "HH:mm:ss");
    }

    /**
     * 获取过去多少时间
     *
     * @param start
     * @return
     */
    public static String getPastedTimeDesc(long start) {
        long timeMillis = System.currentTimeMillis();
        int offsetTime = (int) ((timeMillis - start) / 1000);

        if (offsetTime <= 60) {
            return "刚刚";
        } else if (offsetTime < HOUR_SECONDS) {
            return (offsetTime / 60) + "分钟前";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int curDay = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.setTimeInMillis(start);
            int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

            int offsetDay = curDay - lastDay;

            if (offsetDay == 1) {
                return "昨天";
            } else if (offsetDay == 0) {
                return (offsetTime / HOUR_SECONDS) + "小时前";
            }
            return getCurrentTime(start);
        }
    }

    /**
     * 将毫秒数换算成x天x时x分（天数大于1）或者x时x分x秒（天数小于1）
     *
     * @param ms
     * @return
     */
    public static String long2Hms(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        return day < 1 ? strHour + ":" + strMinute + ":" + strSecond : strDay +"天"+ ":" + strHour + "小时" ;
    }

}
