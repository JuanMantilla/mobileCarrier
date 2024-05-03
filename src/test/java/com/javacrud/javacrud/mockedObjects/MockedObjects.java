package com.javacrud.javacrud.mockedObjects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.util.DailyUsageDTO;

public class MockedObjects {

    public static User getMockUser() {
        User mockedUser = new User("user123", "John", "Doe", "test123");
        mockedUser.setId("1234567890");
        return mockedUser;
    }

    public static Date getMockDate(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date mockedDate;
        try {
            mockedDate = dateFormat.parse(date);
            return mockedDate;
        } catch (ParseException e) {
            System.err.println("Error parsing date");
            return null;
        }
    }

    public static String generateMockedMdn() {
        return "123456789";
    }

    public static Cycle getMockedCycle(Date startDate, Date endDate, String userId) {
        Cycle mockedCycle = new Cycle(MockedObjects.generateMockedMdn(), startDate, endDate, userId);
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
