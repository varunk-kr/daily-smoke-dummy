package com.krogerqa.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    private static DateUtils instance;

    private DateUtils() {
    }

    public synchronized static DateUtils getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DateUtils.class) {
            if (instance == null) {
                instance = new DateUtils();
            }
        }
        return instance;
    }

    /**
     * @param format        date format in string. For example, "yyyy-MM-dd"
     * @param daysFromToday integer to indicate number of days to be added or subtracted from current date. Example, -1 for previous day, 0 for current day, 1 for next day
     * @return required date string in the desired date format
     */
    public static String getRequiredDateString(String format, int daysFromToday) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(getDate(daysFromToday));
    }

    /**
     * @param daysFromToday integer to indicate number of days to be added or subtracted from current date. Example, -1 for previous day, 0 for current day, 1 for next day
     * @return required date object
     */
    public static Date getDate(int daysFromToday) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysFromToday);
        return cal.getTime();
    }

    /* To check whether daylight saving is enabled and return the hoursSpan accordingly */
     public static int isDayLightSavingsEnabled() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        int hoursSpan=5;
        if (timeZone.observesDaylightTime()) {
            hoursSpan = 4;
        }
        return hoursSpan;
    }
    
    /**
     * @param year  required year of the date
     * @param month required month of the date
     * @param day   required day of the date
     * @return particular day's date string in yyyy-MM-dd format
     */
    public static String getDateFromDayMonthYear(int year, int month, int day) {
        return LocalDate.of(year, month, day).toString();
    }

    /**
     * @param daysFromToday particular day's date string in yyyy-MM-dd format
     * @return number of milliseconds since the epoch until required date
     */
    public static long getRequiredDateMilliseconds(int daysFromToday) {
        String requiredDateString = getRequiredDateString(Constants.Date.ISO_DATE_FORMAT, daysFromToday);
        long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(requiredDateString));
        return (Instant.now().toEpochMilli() + (86400000 * daysDifference));
    }

    /*Used for creating a rush order and normal pickUp order with particular pickUp time*/
    public static String selectRequiredHours(int hoursSpan) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 5 + hoursSpan);
        Date date = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        return format.format(date);
    }

    public static String getTodaysDate() {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateObj.format(formatter);
    }

    public static String getCurrentTimeInUTCTimeFormat() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return currentTime.format(formatter);
    }
}
