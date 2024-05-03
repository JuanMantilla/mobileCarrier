package com.javacrud.javacrud.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateManilpulation {
    public static Date resetTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

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
}
