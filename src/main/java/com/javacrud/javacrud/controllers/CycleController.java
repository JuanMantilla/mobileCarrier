package com.javacrud.javacrud.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.services.CycleService;
import com.javacrud.javacrud.util.CycleHistory;
import com.javacrud.javacrud.util.DateUsagePair;

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
    
    @PostMapping
    public Cycle createCycle(@RequestBody Cycle cycle) {
        return cycleService.create(cycle);
    }

    @GetMapping
    public List<Cycle> getCycles() {
        return cycleService.list();
    }

    @GetMapping("/{userId}/{mdn}")
    public List<DateUsagePair> getCurrentCycleDailyUsage(@PathVariable String userId, @PathVariable String mdn) {
        return cycleService.getCurrentCycleDailyUsage(userId, mdn);
    }

    @GetMapping("/history/{userId}/{mdn}")
    public List<CycleHistory> getCycleHistory(@PathVariable String userId, @PathVariable String mdn) {
        return cycleService.getCycleHistory(userId, mdn);
    }
    
}
