package com.javacrud.javacrud.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.javacrud.javacrud.DTOs.CycleHistoryDTO;
import com.javacrud.javacrud.DTOs.DateUsagePairDTO;
import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.services.CycleService;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cycles")
public class CycleController {

    private CycleService cycleService;

    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }
    
    /**
     * Create a new cycle
     * @param cycle
     * @return Created cycle
     */
    @PostMapping
    public Cycle createCycle(@RequestBody Cycle cycle) {
        return cycleService.create(cycle);
    }

    /**
     * Get all cycles
     * @return List of cycles
     */
    @GetMapping
    public List<Cycle> getCycles() {
        return cycleService.list();
    }

    /**
     * Get cycle by id
     * @param id
     * @return Cycle
     */
    @GetMapping("/{userId}/{mdn}")
    public List<DateUsagePairDTO> getCurrentCycleDailyUsage(@PathVariable String userId, @PathVariable String mdn) {
        List<DailyUsage> dailyUsages= cycleService.getCurrentCycleDailyUsage(userId, mdn);
        // Convert daily usage data to DateUsagePair objects
         List<DateUsagePairDTO> payload = new ArrayList<>();
        for (DailyUsage dailyUsage : dailyUsages) {
            DateUsagePairDTO dateUsage = new DateUsagePairDTO(dailyUsage.getUsageDate(), dailyUsage.getUsedInMb());
            payload.add(dateUsage);
        }
        return payload;
    }

    /**
     * Get cycle history
     * @param userId
     * @param mdn
     * @return List of cycle history
     */
    @GetMapping("/history/{userId}/{mdn}")
    public List<CycleHistoryDTO> getCycleHistory(@PathVariable String userId, @PathVariable String mdn) {
        return cycleService.getCycleHistory(userId, mdn);
    }
    
}
