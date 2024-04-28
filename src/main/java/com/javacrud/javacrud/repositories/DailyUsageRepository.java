package com.javacrud.javacrud.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.javacrud.javacrud.documents.DailyUsage;

public interface DailyUsageRepository  extends MongoRepository<DailyUsage, String>{
    @Query("{'usageDate': {'$gte': ?0}, 'userId': ?1, 'mdn': ?2}")
    List<DailyUsage> getUsage(LocalDate startDate, String userId, String mdn);
}
