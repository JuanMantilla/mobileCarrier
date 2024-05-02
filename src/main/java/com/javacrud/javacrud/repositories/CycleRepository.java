package com.javacrud.javacrud.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.javacrud.javacrud.documents.Cycle;

public interface CycleRepository  extends MongoRepository<Cycle, String>{
    List<Cycle> findByUserIdAndMdn(String userId, String mdn);
}
