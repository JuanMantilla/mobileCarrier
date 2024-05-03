package com.javacrud.javacrud.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

import com.javacrud.javacrud.util.DateManilpulation;

public class DateManipulationTest {

    @Test
    public void testResetTimeToMidnight() {
        // Create a date with arbitrary time
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 12, 30, 45);
        Date date = calendar.getTime();
        
        // Reset time to midnight
        Date result = DateManilpulation.resetTimeToMidnight(date);
        
        // Create a date with expected midnight time
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date expected = calendar.getTime();
        
        // Assert that the result matches the expected value
        assertEquals(expected, result);
    }

    @Test
    public void testAdjustTimezone() {
        // Create a date with arbitrary time
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 12, 30, 45);
        Date date = calendar.getTime();
        
        // Set the default timezone to a specific value (e.g., EST)
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        
        // Adjust timezone to UTC
        Date result = DateManilpulation.adjustTimezone(date);
        
        // Calculate the expected result manually
        // Calculate the offset between EST and UTC
        int offset = TimeZone.getTimeZone("UTC").getRawOffset() - TimeZone.getTimeZone("America/New_York").getRawOffset();
        Date expected = new Date(date.getTime() + offset);
        
        // Assert that the result matches the expected value
        assertEquals(expected, result);
    }
}
