package com.javacrud.javacrud.services;

import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.UserRepository;
import com.javacrud.javacrud.util.CycleHistory;
import com.javacrud.javacrud.util.DateManilpulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class CycleService {

    private CycleRepository cycleRepository;
    private UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CycleService.class);

    public CycleService(CycleRepository cycleRepository, UserRepository userRepository,
            MongoTemplate mongoTemplate) {
        this.cycleRepository = cycleRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Cycle create(Cycle cycle) {
        // Validate user existence
        if (!this.userRepository.existsById(cycle.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with ID " + cycle.getUserId() + " does not exist.");
        }

        return this.cycleRepository.save(cycle);
    }

    public List<Cycle> list() {
        return this.cycleRepository.findAll();
    }

    public Cycle getLastCycleForUserAndMdn(String userId, String mdn) {
        try {
            userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with ID " + userId + " does not exist.");
        }

        Date currentDate = DateManilpulation.resetTimeToMidnight(new Date());
        Query lastCycleQuery = new Query();

        // Query to find the most current billing cycle
        lastCycleQuery.addCriteria(Criteria.where("userId").is(userId).and("startDate")
                .lte(currentDate).and("mdn").is(mdn));

        lastCycleQuery.with(Sort.by(Sort.Direction.ASC, "startDate"));

        // Retrieve the current billing cycle
        Cycle lastCycle = this.mongoTemplate.findOne(lastCycleQuery, Cycle.class);
        return lastCycle;
    }

    /**
     * Retrieves daily usage data for the current billing cycle.
     *
     * @param userId The unique identifier for the user.
     * @param mdn The mobile device number associated with the user.
     * @return A list of DateUsagePair objects representing usage data for each day.
     */
    public List<DailyUsage> getCurrentCycleDailyUsage(String userId, String mdn) {
        try {
            userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with ID " + userId + " does not exist.");
        }

        logger.info("Getting current daily usage for user ID [{}] and mdn [{}]", userId, mdn);

        Cycle mostRecentCycle = this.getLastCycleForUserAndMdn(userId, mdn);
        Date currentDate = new Date();

        if (mostRecentCycle == null || mostRecentCycle.getStartDate().after(currentDate)
                || mostRecentCycle.getEndDate().before(currentDate)) {
            logger.info("Didn't find a current cycle for user [{}] and mdn [{}]", userId, mdn);
            return new ArrayList<>();
        }

        Cycle currentUserCycle = mostRecentCycle;

        Query dailyUsagesQuery = new Query();

        // Query to find daily usages within the current cycle
        dailyUsagesQuery.addCriteria(Criteria.where("userId").is(userId).and("mdn").is(mdn)
                .and("usageDate").gte(currentUserCycle.getStartDate()));

        // Retrieve daily usage data
        List<DailyUsage> dailyUsages = this.mongoTemplate.find(dailyUsagesQuery, DailyUsage.class);
        logger.info("Got [{}] current daily usages for user ID [{}] and mdn [{}].",
                dailyUsages.size(), userId, mdn);

        return dailyUsages;
    }

    public List<CycleHistory> getCycleHistory(String userId, String mdn) {
        List<Cycle> cycles = this.cycleRepository.findByUserIdAndMdn(userId, mdn);
        List<CycleHistory> payload = new ArrayList<>();
        for (Cycle cycle : cycles) {
            CycleHistory cycleHistory =
                    new CycleHistory(cycle.getId(), cycle.getStartDate(), cycle.getEndDate());
            payload.add(cycleHistory);
        }
        return payload;
    }
}
