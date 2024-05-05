package com.javacrud.javacrud.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.javacrud.javacrud.DTOs.DailyUsageDTO;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.services.DailyUsageService;

@RestController
@RequestMapping("/daily-usage")
public class DailyUsageController {

    private final DailyUsageService dailyUsageService;

    public DailyUsageController(DailyUsageService dailyUsageService) {
        this.dailyUsageService = dailyUsageService;
    }

    @PutMapping
    public DailyUsageDTO createDailyUsage(@RequestBody DailyUsageDTO dailyUsage) {
        DailyUsage createdDailyUsage = dailyUsageService.createOrUpdate(dailyUsage);
        return new DailyUsageDTO(
            createdDailyUsage.getMdn(),
            createdDailyUsage.getUserId(),
            createdDailyUsage.getUsageDate(),
            createdDailyUsage.getUsedInMb(),
            createdDailyUsage.getNextCycleId()
        );
    }

    @GetMapping
    public List<DailyUsage> getDailyUsages() {
        return dailyUsageService.list();
    }  
}
