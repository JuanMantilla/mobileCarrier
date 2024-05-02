package com.javacrud.javacrud.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.DailyUsageRepository;
import com.javacrud.javacrud.repositories.UserRepository;
import com.javacrud.javacrud.util.DateManilpulation;

@Service
public class DailyUsageService {

    private DailyUsageRepository dailyUsageRepository;
    private UserRepository userRepository;
    private CycleRepository cycleRepository;


    private static final Logger logger = LoggerFactory.getLogger(DailyUsageService.class);

    public DailyUsageService(
            DailyUsageRepository dailyUsageRepository,
            UserRepository userRepository,
            CycleRepository cycleRepository,
            CycleService cycleService,
            MongoTemplate mongoTemplate) {
        this.dailyUsageRepository = dailyUsageRepository;
        this.userRepository = userRepository;
        this.cycleRepository = cycleRepository;
    }

    /**
     * Creates or updates a daily usage record for a user.
     *
     * @param dailyUsage The daily usage object to create or update.
     * @return The updated or newly created daily usage object.
     * @throws ResponseStatusException If the user with the specified ID does not exist.
     */
    public DailyUsage createOrUpdate(DailyUsage dailyUsage) {
        try {
            // Check if the user exists
            userRepository.findById(dailyUsage.getUserId()).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with ID " + dailyUsage.getUserId() + " does not exist.");
        }

        Date usageDate;
        if (dailyUsage.getUsageDate() != null) {
            usageDate = DateManilpulation.resetTimeToMidnight(dailyUsage.getUsageDate());
        } else {
            usageDate = DateManilpulation.resetTimeToMidnight(new Date());
        }

        // Get current daily usage
        DailyUsage currDailyUsage = dailyUsageRepository.findByMdnAndUsageDateAndUserId(dailyUsage.getMdn(), usageDate, dailyUsage.getUserId());

        if (currDailyUsage != null) {
            // Update current daily usage since it already exists
            Number newUsage = dailyUsage.getUsedInMb().longValue() + currDailyUsage.getUsedInMb().longValue();
            logger.info("Updating Mbs used by user [{}] and mdn [{}] to [{} Mbs]", dailyUsage.getUserId(), dailyUsage.getMdn(), newUsage);
            currDailyUsage.setUsedInMb(newUsage);

            if (currDailyUsage.getCycle().getEndDate().compareTo(usageDate) == 0 && currDailyUsage.getNextCycleId() == null) {
                // Today is the last day of the cycle and currDailyUsage doesn't have a next cycle ID,
                // so we need to create the next cycle and dailyUsage
                logger.info("Creating next cycle and daily usage for user [{}] and mdn [{}]...", dailyUsage.getUserId(), dailyUsage.getMdn());
                Calendar start = Calendar.getInstance();
                start.setTime(usageDate);
                start.add(Calendar.DATE, 1);
                Date startDate = start.getTime();
                start.add(Calendar.MONTH, 1);
                Date endDate = start.getTime();

                Cycle newCycle = this.cycleRepository.save(new Cycle(dailyUsage.getMdn(), startDate, endDate, dailyUsage.getUserId()));
                this.dailyUsageRepository.save(new DailyUsage(dailyUsage.getMdn(), dailyUsage.getUserId(), startDate, 0, null, newCycle));
                currDailyUsage.setNextCycleId(newCycle.getId());
                logger.info("Next cycle and daily usage for user [{}] and mdn [{}] created with start date of [{}]", dailyUsage.getUserId(), dailyUsage.getMdn(), newCycle.getStartDate());
            }
            return dailyUsageRepository.save(currDailyUsage);
        } else {
            // First dailyUsage update of the day and not the first day of the cycle
            logger.info("Creating new daily usage for user [{}] and mdn [{}]...", dailyUsage.getUserId(), dailyUsage.getMdn());
            Calendar today = Calendar.getInstance();
            today.setTime(usageDate);
            // Getting date of yesterday
            today.add(Calendar.DATE, -1);
            Date yesterday = today.getTime();

            DailyUsage previoUsage = dailyUsageRepository.findByMdnAndUsageDateAndUserId(dailyUsage.getMdn(), yesterday, dailyUsage.getUserId());
            DailyUsage newDailyUsage = this.dailyUsageRepository.save(new DailyUsage(dailyUsage.getMdn(), dailyUsage.getUserId(), usageDate, dailyUsage.getUsedInMb(), null, previoUsage.getCycle()));
            logger.info("Created new daily usage for user [{}] and mdn [{}] with date [{}]", dailyUsage.getUserId(), dailyUsage.getMdn(), newDailyUsage.getUsageDate());
            return newDailyUsage;
        }
    }
 
    public List<DailyUsage> list() {
        return this.dailyUsageRepository.findAll();
    }
}