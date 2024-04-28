package com.javacrud.javacrud.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.services.DailyUsageService;

@RestController
@RequestMapping("/daily-usage")
public class DailyUsageController {

    private final DailyUsageService dailyUsageService;

    public DailyUsageController(DailyUsageService dailyUsageService) {
        this.dailyUsageService = dailyUsageService;
    }

    @PostMapping
    public DailyUsage createDailyUsage(@RequestBody DailyUsage dailyUsage) {
        return dailyUsageService.create(dailyUsage);
    }
    
    @GetMapping
    public List<DailyUsage> getDailyUsages() {
        return dailyUsageService.list();
    }  
}
