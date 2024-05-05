package com.javacrud.javacrud.mockedObjects;

import java.util.Date;
import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.util.DailyUsageDTO;
import com.javacrud.javacrud.util.DateManilpulation;

public class MockedObjects {

    public static User getMockUser() {
        User mockedUser = new User("user123", "John", "Doe", "test123");
        mockedUser.setId("1234567890");
        return mockedUser;
    }

    public static Date getCurrentDate() {
        return DateManilpulation.resetTimeToMidnight(DateManilpulation.adjustTimezone(new Date()));
    }

    public static Date addMonthToDate(Date date, int months) {
        return DateManilpulation.addMonthsToDate(date, months);
    }

    public static Date addDaysToDate(Date date, int days) {
        return DateManilpulation.addDaysToDate(date, days);
    }

    public static String generateMockedMdn() {
        return "123456789";
    }

    public static Cycle getMockedCycle(Date startDate, String userId) {
        Cycle mockedCycle = new Cycle(MockedObjects.generateMockedMdn(), startDate, userId);
        mockedCycle.setId("123456789");
        return mockedCycle;
    }

    public static DailyUsageDTO getMockedDailyUsageDTO(String userId, Date usageDate, Number usedInMb, String nextCycleId) {
        return new DailyUsageDTO(MockedObjects.generateMockedMdn(), userId, usageDate, usedInMb, nextCycleId);
    }

    public static DailyUsage getMockedDailyUsage(String userId, Date usageDate, Number usedInMb, String nextCycleId, Cycle cycle) {
        return new DailyUsage(MockedObjects.generateMockedMdn(), userId, usageDate, usedInMb, nextCycleId, cycle);
    }
}
