package com.javacrud.javacrud.services;

import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.DailyUsageRepository;
import com.javacrud.javacrud.repositories.UserRepository;
import com.javacrud.javacrud.util.CycleHistory;
import com.javacrud.javacrud.util.DateUsagePair;



@Service
public class CycleService {

    private CycleRepository cycleRepository;
    private UserRepository userRepository;
    private DailyUsageRepository dailyUsageRepository;
    private final MongoTemplate mongoTemplate;

    public CycleService(CycleRepository cycleRepository,
                        UserRepository userRepository,
                        DailyUsageRepository dailyUsageRepository,
                        MongoTemplate mongoTemplate) {
        this.cycleRepository = cycleRepository;
        this.userRepository = userRepository;
        this.dailyUsageRepository = dailyUsageRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Cycle create(Cycle cycle) {
        // Validate user existence
        if (!this.userRepository.existsById(cycle.getUserId())) {
            throw new IllegalArgumentException("User with ID " + cycle.getUserId() + " does not exist.");
        }
        
        return this.cycleRepository.save(cycle);
    }

    public List<Cycle> list() {
        return this.cycleRepository.findAll();
    }

    /**
     * Retrieves daily usage data for the current billing cycle.
     *
     * @param userId The unique identifier for the user.
     * @param mdn The mobile device number associated with the user.
     * @return A list of DateUsagePair objects representing usage data for each day.
     */
    public List<DateUsagePair> getCurrentCycleDailyUsage(String userId, String mdn) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Initialize the payload list
        List<DateUsagePair> payload = new ArrayList<>();

        Query currentCycleQuery = new Query();
        Query dailyUsagesQuery = new Query();

        // Query to find the current billing cycle
        currentCycleQuery.addCriteria(Criteria.
            where("userId").is(userId).
            and("mdn").is(mdn).
            and("startDate").
            lte(currentDate)
        );

        // Retrieve the current billing cycle
        Cycle currentCycle = this.mongoTemplate.findOne(currentCycleQuery, Cycle.class);

        // Query to find daily usages within the current cycle
        dailyUsagesQuery.addCriteria(Criteria.
            where("userId").is(userId).
            and("mdn").is(mdn).
            and("usageDate").
            gte(currentCycle.getStartDate())
        );

        // Retrieve daily usage data
        List<DailyUsage> dailyUsages = this.mongoTemplate.find(dailyUsagesQuery, DailyUsage.class);

        // Convert daily usage data to DateUsagePair objects 
        for (DailyUsage dailyUsage : dailyUsages) {
            DateUsagePair dateUsage = new DateUsagePair(dailyUsage.getUsageDate(), dailyUsage.getUsedInMb());
            payload.add(dateUsage);
        }

        return payload;
    }

    public List<CycleHistory> getCycleHistory(String userId, String mdn) {
        List<Cycle> cycles = this.cycleRepository.getCycleHistory(userId, mdn);
        List<CycleHistory> payload = new ArrayList<>();
        for (Cycle cycle : cycles) {
            CycleHistory cycleHistory = new CycleHistory(cycle.getId(), cycle.getStartDate(), cycle.getEndDate());
            payload.add(cycleHistory);
        }
        return payload;
    }
}
