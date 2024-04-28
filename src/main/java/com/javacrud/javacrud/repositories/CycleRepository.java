package com.javacrud.javacrud.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.javacrud.javacrud.documents.Cycle;

public interface CycleRepository  extends MongoRepository<Cycle, String>{
    
    @Query("{'startDate': {'$lte': new Date()}, 'userId': ?0, 'mdn': ?1}")
    Cycle getCurrentCycle(String userId, String mdn);

    @Query("{'userId': ?0, 'mdn': ?1}")
    List<Cycle> getCycleHistory(String userId, String mdn);
}
