package ruilelin.com.shifenlife.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created By WengQiao 2018-09-22 9:35
 * Description :
 * Go
 */
public class TimeUtils {

    /**
     * Created By WengQiao
     * Go
     * 功能描述: 获取两个日期之间间隔的天数
     *
     * @Version:v1.0
     * @param: [beginDate 开始时间, endDate 结束时间]
     * @date: 2018/9/22 9:37
     * @return: int 间隔天数
     */
    public static int getTimeDistance(Date date1, Date date2) {

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(date1);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date2);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        //计算两时间的毫秒数之差大于一天的天数
        int betweenDays;
        if (beginTime < endTime) {
            betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));
        } else {
            betweenDays = (int) ((beginTime - endTime) / (1000 * 60 * 60 * 24));
        }
        //endCalendar减去日期差
        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);
        //endCalendar减去1天
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);
        if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)) {
            return betweenDays + 1;    //相等》跨天
        } else {
            return betweenDays + 0;    //不相等》未跨天
        }
    }

    /**
     * Created By WengQiao
     * Go
     * 功能描述: 给定任意日期，返回日期所对应的星期
     *
     * @Version:v1.0
     * @param: [date 任意日期]
     * @date: 2018/9/27 16:14
     * @return: net.fd.core.enums.WeekEnum
     */
    public static String dayOfWeeks(Date date) {
        String[] weekDays = {
                "周日",
                "周一",
                "周二",
                "周三",
                "周四",
                "周五",
                "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 因为运行环境的DAY_OF_WEEK是从星期天开始的所以这里需要减1
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * Created By WengQiao
     * Go
     * 功能描述:   任意给定两个时间  返回两个日期距离多少天多少小时多少分多少秒
     *
     * @Version:v1.0
     * @param: [dateOne 第一个日期, dateTwo 第二个日期]
     * @date: 2018/10/18 13:26
     * @return: java.util.List<java.lang.Long>
     */
    public static List<Long> getTimeDistanceDetail(Date date1, Date date2) {

        List<Long> result = new ArrayList<>();

        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        long day = diff / (24 * 60 * 60 * 1000);
        result.add(day);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        result.add(hour);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        result.add(min);
        long sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        result.add(sec);

        return result;
    }
}
