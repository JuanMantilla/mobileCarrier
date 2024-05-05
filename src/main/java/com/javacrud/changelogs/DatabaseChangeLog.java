package com.javacrud.changelogs;

import java.util.Date;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.DailyUsageRepository;
import com.javacrud.javacrud.repositories.UserRepository; // Add this import statement
import com.javacrud.javacrud.util.DateManilpulation;

@ChangeLog
public class DatabaseChangeLog {

    @ChangeSet(order = "1", id = "seedDatabase", author = "Seeder")
    public void seedDatabase(UserRepository userRepository, CycleRepository cycleRepository,
            DailyUsageRepository dailyUsageRepository) {
        // Create a new user
        User newUser = userRepository
                .save(createNewUser("Juan", "Mantilla", "js.mantilla128@gmail.com", "password"));

        // Create a new cycle for the user starting from the previous month
        Cycle newCycle = cycleRepository.save(createNewCycle(newUser.getId(), null, null));

        // Create a new daily usage entry for the user's cycle
        DailyUsage newDailyUsage = dailyUsageRepository.save(createNewDailyUsage(newCycle.getMdn(),
                newUser.getId(), newCycle.getStartDate(), null, newCycle, 0));

        // Calculate the number of days until the end of the cycle
        int daysUntiEndOfCycle = DateManilpulation.daysBetweenDates(newDailyUsage.getUsageDate(),
                newCycle.getEndDate());

        // Iterate over each day until the end of the cycle
        for (int i = 1; i <= daysUntiEndOfCycle; i++) {
            // Calculate the date for the next day
            Date newDate = DateManilpulation.addDaysToDate(newDailyUsage.getUsageDate(), i);

            // Create a new daily usage entry for the next day
            DailyUsage newDailyUsageForNextDay = createNewDailyUsage(newCycle.getMdn(),
                    newUser.getId(), newDate, null, newCycle, i*100);

            // If it's the last day of the cycle
            if (i == daysUntiEndOfCycle) {
                newDate = DateManilpulation.addDaysToDate(newDate, 1);
                // Create a new cycle for the next month
                Cycle newCycleForNextMonth =
                        createNewCycle(newUser.getId(), newCycle.getMdn(), newDate);
                cycleRepository.save(newCycleForNextMonth);

                // Set the next cycle ID for the daily usage entry of the last day
                newDailyUsageForNextDay.setNextCycleId(newCycleForNextMonth.getId());

                // Create a new daily usage entry for the next month
                DailyUsage newDailyUsageForNextMonth = createNewDailyUsage(
                        newCycleForNextMonth.getMdn(), newUser.getId(),
                        newDate, null, newCycleForNextMonth, 0);
                dailyUsageRepository.save(newDailyUsageForNextMonth);
            }

            // Save the daily usage entry for the next day
            dailyUsageRepository.save(newDailyUsageForNextDay);
        }
    }


    private User createNewUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        return user;
    }

    private Cycle createNewCycle(String userId, String mdn, Date startDate) {
        if (mdn == null || mdn.isEmpty()) {
            mdn = Long.toString((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
        }
        if (startDate == null) {
            Date currentDate = DateManilpulation.adjustTimezone(DateManilpulation
                    .resetTimeToMidnight(DateManilpulation.addMonthsToDate(new Date(), -1)));
            startDate = DateManilpulation.resetTimeToMidnight(currentDate);
        }
        Cycle cycle = new Cycle(mdn, startDate, userId);
        return cycle;
    }

    private DailyUsage createNewDailyUsage(String mdn, String userId, Date usageDate,
            String nextCycleId, Cycle cycle, Number usedInMb) {

        DailyUsage dailyUsage = new DailyUsage(mdn, userId, usageDate, usedInMb, nextCycleId, cycle);
        return dailyUsage;
    }
}
