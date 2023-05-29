package com.emily.infrastructure.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类：
 * ----------------------------------------------
 * 日期字符串-日期字符串
 * 字符串-Date
 * <p>
 * 字符串-LocalDateTime
 * Date-LocalDateTime
 * LocalDate-LocalDateTime
 * <p>
 * 字符串-LocalDate
 * Date-LocalDate
 * LocalDateTime-LocalDate
 * <p>
 * 字符串-LocalTime
 * Date-LocalTime
 * LocalDateTime-LocalTime
 * ----------------------------------------------
 *
 * @author Emily
 * @program: spring-parent
 * @create: 2020/06/16
 */
public class DateConvertUtils {
    /**
     * 字符串日期格式化
     *
     * @param str           字符串日期
     * @param sourcePattern 原始日期格式
     * @param targetPattern 目标格式化格式
     * @return 格式化后的日期
     */
    public static String format(String str, String sourcePattern, String targetPattern) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("非法参数");
        }
        if (sourcePattern == null || sourcePattern.length() == 0) {
            throw new IllegalArgumentException("非法参数");
        }
        if (targetPattern == null || targetPattern.length() == 0) {
            throw new IllegalArgumentException("非法参数");
        }
        DateFormat sdf = new SimpleDateFormat(targetPattern);
        return sdf.format(toDate(str, sourcePattern));
    }

    /**
     * 字符串日期格式化
     *
     * @param pattern 目标格式化格式
     * @return 格式化后的日期
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("非法参数");
        }
        DateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 日期对象转字符串
     *
     * @param date    日期对象
     * @param pattern 日期格式
     * @return 字符串日期
     */
    public static String format(LocalTime date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("非法参数");
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期对象转字符串
     *
     * @param date    日期对象
     * @param pattern 日期格式
     * @return 字符串日期
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("非法参数");
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期对象转字符串
     *
     * @param date    日期对象
     * @param pattern 日期格式
     * @return 字符串日期
     */
    public static String format(LocalDateTime date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("非法参数");
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    //----------------------------------------------------------Date----------------------------------------------------------------------------------

    /**
     * 字符串日期格式化
     *
     * @param str     字符串日期
     * @param pattern 日期格式
     * @return 格式化后的日期
     */
    public static Date toDate(String str, String pattern) {
        try {
            if (str == null || str.length() == 0) {
                throw new IllegalArgumentException("非法参数");
            }
            if (pattern == null || pattern.length() == 0) {
                throw new IllegalArgumentException("非法参数");
            }
            DateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException("非法参数");
        }
    }

    /**
     * 将LocalDateTime转为Date
     *
     * @param localDateTime 日期类型
     * @return Date日期对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将LocalDateTime转为Date
     *
     * @param localDate 日期类型
     * @return Date日期对象
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将毫秒转换为日期对象
     *
     * @param milliseconds 毫秒，如：System.currentTimeMillis()
     * @return 日期对象
     */
    public static Date toDate(long milliseconds) {
        return new Date(milliseconds);
    }
    //-----------------------------------------------------------LocalDateTime---------------------------------------------------------------------------------

    /**
     * 将LocalDate转换为LocalDateTime
     *
     * @param localDate 日期对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        if (localDate == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return localDate.atStartOfDay();
    }

    /**
     * 将Date数据类型转换为LocalDateTime
     *
     * @param date 日期对象
     * @return 转换后的LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将字符串日期转换为LocalDateTime对象
     *
     * @param str     字符串日期
     * @param pattern 格式
     * @return 日期对象
     */
    public static LocalDateTime toLocalDateTime(String str, String pattern) {
        if (str == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将日期对象和时间对象拼接成一个日期对象
     *
     * @param date1 日期对象
     * @param date2 时间对象
     * @return 拼接后的时间对象
     */
    public static LocalDateTime toLocalDateTime(LocalDate date1, LocalTime date2) {
        if (date1 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (date2 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return LocalDateTime.of(date1, date2);
    }

    /**
     * 将指定时间转化为对应时区的时间
     * ------------------------------------------------------
     * zoneId示例：
     * ZoneId ZONE_CN = ZoneId.of("Asia/Shanghai");
     * ZoneId ZONE_US = ZoneId.of("US/Eastern")
     * ------------------------------------------------------
     *
     * @param date1  日期对象
     * @param zoneId 时区对象
     * @return 转换后的日期对象
     */
    public static LocalDateTime toLocalDateTime(LocalDateTime date1, ZoneId zoneId) {
        if (date1 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (zoneId == null) {
            zoneId = ZoneId.systemDefault();
        }
        return date1.atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId).toLocalDateTime();
    }

    //-------------------------------------------------------------------LocalDate-------------------------------------------------------------------------

    /**
     * 将Date数据类型转换为LocalDate
     *
     * @param date 日期对象
     * @return 转换后的LocalDate对象
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 将LocalDateTime 转 LocalDate
     *
     * @param localDateTime 日期对象
     * @return LocalDate日期对象
     */
    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return localDateTime.toLocalDate();
    }

    /**
     * 将字符串日期转换为LocalDate对象
     *
     * @param str     字符串日期
     * @param pattern 格式
     * @return 日期对象
     */
    public static LocalDate toLocalDate(String str, String pattern) {
        if (str == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    //-----------------------------------------------------------------------LocalTime---------------------------------------------------------------------

    /**
     * 将Date数据类型转换为LocalDate
     *
     * @param date 日期对象
     * @return 转换后的LocalDate对象
     */
    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * 将LocalDateTime转 LocalTime对象
     *
     * @param localDateTime 日期对象
     * @return LocalTime对象
     */
    public static LocalTime toLocalTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return localDateTime.toLocalTime();
    }

    /**
     * 将字符串日期转换为LocalDate对象
     *
     * @param str     字符串日期
     * @param pattern 格式
     * @return 日期对象
     */
    public static LocalTime toLocalTime(String str, String pattern) {
        if (str == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return LocalTime.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    //----------------------------------------------------------------combine----------------------------------------------------------------------------

    /**
     * 将日期和时间类型拼接成 LocalDateTime对象
     *
     * @param date1 日期
     * @param date2 时间
     * @return 拼接后的时间对象
     */
    public static LocalDateTime combine(LocalDate date1, LocalTime date2) {
        if (date1 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (date2 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return LocalDateTime.of(date1, date2);
    }

    /**
     * 将日期和时间类型拼接成 LocalDateTime对象
     *
     * @param date1    日期
     * @param pattern1 日期date1的格式类型
     * @param date2    时间
     * @param pattern2 日期date2的格式类型
     * @return 拼接后的时间对象
     */
    public static LocalDateTime combine(String date1, String pattern1, String date2, String pattern2) {
        if (date1 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern1 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (date2 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern2 == null) {
            throw new IllegalArgumentException("非法参数");
        }
        return LocalDateTime.of(LocalDate.parse(date1, DateTimeFormatter.ofPattern(pattern1)), LocalTime.parse(date2, DateTimeFormatter.ofPattern(pattern2)));
    }


    //---------------------------------------------------------------dateToInt-----------------------------------------------------------------------------

    /**
     * 将日期对象转换为整数日期
     *
     * @param date    日期对象
     * @param pattern 日期 格式
     * @return 整数日期
     */
    public static long dateToInt(Date date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("非法参数");
        }
        return Long.parseLong(format(date, pattern));
    }

    /**
     * 将时间对象转换为整数类型
     *
     * @param date    时间对象
     * @param pattern 日期格式
     * @return 整数类型日期
     */
    public static long dateToInt(LocalTime date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null || pattern.length() == 0) {
            pattern = DatePatternInfo.HHMMSS;
        }
        return Long.parseLong(date.format(DateTimeFormatter.ofPattern(pattern)));
    }

    /**
     * 将日期对象转换为整数类型
     *
     * @param date    日期对象
     * @param pattern 日期格式对象
     * @return 整数类型日期
     */
    public static long dateToInt(LocalDate date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null || pattern.length() == 0) {
            pattern = DatePatternInfo.YYYYMMDD;
        }
        return dateToInt(date.atStartOfDay(), pattern);
    }

    /**
     * 日期类型转换为整数类型
     *
     * @param date    日期对象
     * @param pattern 日期格式
     * @return 整数类型日期
     */
    public static long dateToInt(LocalDateTime date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (pattern == null || pattern.length() == 0) {
            pattern = DatePatternInfo.YYYYMMDDHHMMSS;
        }
        return Long.parseLong(date.format(DateTimeFormatter.ofPattern(pattern)));
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------
}
