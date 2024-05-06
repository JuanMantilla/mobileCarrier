package com.javacrud.javacrud.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateManilpulation {

    /**
     * Reset the time of a date to midnight.
     *
     * @param date The date to reset.
     * @return The date with the time reset to midnight.
     */
    public static Date resetTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    /**
     * Add months to a date.
     *
     * @param date The date to add months to.
     * @param months The number of months to add.
     * @return The date with the months added.
     */
    public static Date addMonthsToDate(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * Add days to a date.
     *
     * @param date The date to add days to.
     * @param days The number of days to add.
     * @return The date with the days added.
     */
    public static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * Calculate the number of days between two dates.
     *
     * @param startDate The start date.
     * @param endDate The end date.
     * @return The number of days between the two dates.
     */
    public static int daysBetweenDates(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        return (int) ((end.getTimeInMillis() - start.getTimeInMillis()) / (1000 * 60 * 60 * 24));
    }

    /**
     * Adjust the timezone of a date to UTC.
     *
     * @param date The date to adjust.
     * @return The date with the timezone adjusted to UTC.
     */
    public static Date adjustTimezone(Date date) {
        // Set the desired output timezone, e.g., UTC
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        
        // Get the current timezone of the date object
        TimeZone originalTimeZone = TimeZone.getDefault();
        
        // Calculate the offset between the original timezone and UTC
        int offset = utcTimeZone.getRawOffset() - originalTimeZone.getRawOffset();
        
        // Adjust the time by the offset
        return new Date(date.getTime() + offset);
    }

    /**
     * Calculate the number of days until the end of the month.
     *
     * @param date The date to calculate the days until the end of the month.
     * @return The number of days until the end of the month.
     */
    public static int daysUntilEndOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH);
    }
}
