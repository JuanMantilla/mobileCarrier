package com.javacrud.javacrud.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.repositories.DailyUsageRepository;
import com.javacrud.javacrud.repositories.UserRepository;

@Service
public class DailyUsageService {

    private DailyUsageRepository dailyUsageRepository;
    private UserRepository userRepository;

    public DailyUsageService(DailyUsageRepository dailyUsageRepository, UserRepository userRepository) {
        this.dailyUsageRepository = dailyUsageRepository;
        this.userRepository = userRepository;
    }

    public DailyUsage create(DailyUsage dailyUsage) {
        // Validate user existence
        if (!userRepository.existsById(dailyUsage.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with ID " + dailyUsage.getUserId() + " does not exist.");
        }
        
        return this.dailyUsageRepository.save(dailyUsage);
    }

    public List<DailyUsage> list() {
        return this.dailyUsageRepository.findAll();
    }
}