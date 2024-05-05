package com.javacrud.javacrud.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.DailyUsageRepository;
import com.javacrud.javacrud.repositories.UserRepository;
import com.javacrud.javacrud.util.DailyUsageDTO;
import com.javacrud.javacrud.util.DateManilpulation;

@Service
public class DailyUsageService {

    @Autowired
    private DailyUsageRepository dailyUsageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CycleRepository cycleRepository;


    private static final Logger logger = LoggerFactory.getLogger(DailyUsageService.class);

    public DailyUsageService(DailyUsageRepository dailyUsageRepository,
            UserRepository userRepository, CycleRepository cycleRepository,
            CycleService cycleService, MongoTemplate mongoTemplate) {
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
    public DailyUsage createOrUpdate(DailyUsageDTO dailyUsageDTO) {
        try {
            // Check if the user exists
            userRepository.findById(dailyUsageDTO.getUserId()).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with ID " + dailyUsageDTO.getUserId() + " does not exist.");
        }

        Date usageDate;
        if (dailyUsageDTO.getUsageDate() != null) {
            // Adjust dailyUsage.UsageDate timezone and set it to midnight
            dailyUsageDTO.setUsageDate(DateManilpulation.resetTimeToMidnight(
                    DateManilpulation.adjustTimezone(dailyUsageDTO.getUsageDate())));
            usageDate = dailyUsageDTO.getUsageDate();
        } else {
            usageDate = DateManilpulation.resetTimeToMidnight(new Date());
        }

        // Get current daily usage
        DailyUsage currDailyUsage = this.dailyUsageRepository.findByMdnAndUsageDateAndUserId(
            dailyUsageDTO.getMdn(), usageDate, dailyUsageDTO.getUserId());
        if (currDailyUsage != null) {
            // Update current daily usage since it already exists
            Number newUsage =
                    dailyUsageDTO.getUsedInMb().longValue() + currDailyUsage.getUsedInMb().longValue();
            logger.info("Updating Mbs used by user [{}] and mdn [{}] to [{} Mbs]",
            dailyUsageDTO.getUserId(), dailyUsageDTO.getMdn(), newUsage);
            currDailyUsage.setUsedInMb(newUsage);
            logger.info("[{}], [{}]", currDailyUsage.getCycle().getEndDate(), usageDate);
            if (currDailyUsage.getCycle().getEndDate().compareTo(usageDate) == 0
                    && currDailyUsage.getNextCycleId() == null) {
                // Today is the last day of the cycle and currDailyUsage doesn't have a next cycle
                // ID, so we need to create the next cycle and dailyUsage
                logger.info("Creating next cycle and daily usage for user [{}] and mdn [{}] ...",
                dailyUsageDTO.getUserId(), dailyUsageDTO.getMdn());
                Date startDate = DateManilpulation.addDaysToDate(usageDate, 1);
                Date endDate = DateManilpulation.addMonthsToDate(startDate, 1);

                Cycle newCycle = this.cycleRepository.save(
                        new Cycle(dailyUsageDTO.getMdn(), startDate, endDate, dailyUsageDTO.getUserId()));
                this.dailyUsageRepository.save(new DailyUsage(dailyUsageDTO.getMdn(),
                dailyUsageDTO.getUserId(), newCycle.getStartDate(), 0, null, newCycle));
                currDailyUsage.setNextCycleId(newCycle.getId());
                logger.info(
                        "Next cycle and daily usage for user [{}] and mdn [{}] created with start date of [{}]",
                        dailyUsageDTO.getUserId(), dailyUsageDTO.getMdn(), newCycle.getStartDate());
            }
            return this.dailyUsageRepository.save(currDailyUsage);
        } else {
            // First dailyUsage update of the day and not the first day of the cycle
            logger.info("Creating new daily usage for user [{}] and mdn [{}]...",
            dailyUsageDTO.getUserId(), dailyUsageDTO.getMdn());
            Calendar today = Calendar.getInstance();
            today.setTime(usageDate);
            // Getting date of yesterday
            today.add(Calendar.DATE, -1);
            Date yesterday = today.getTime();

            DailyUsage previousUsage = this.dailyUsageRepository.findByMdnAndUsageDateAndUserId(
                dailyUsageDTO.getMdn(), yesterday, dailyUsageDTO.getUserId());
            DailyUsage newDailyUsage = this.dailyUsageRepository
                    .save(new DailyUsage(dailyUsageDTO.getMdn(), dailyUsageDTO.getUserId(), usageDate,
                    dailyUsageDTO.getUsedInMb(), null, previousUsage.getCycle()));
            logger.info("Created new daily usage for user [{}] and mdn [{}] with date [{}]",
            dailyUsageDTO.getUserId(), dailyUsageDTO.getMdn(), newDailyUsage.getUsageDate());
            return newDailyUsage;
        }
    }

    public List<DailyUsage> list() {
        return this.dailyUsageRepository.findAll();
    }
}
