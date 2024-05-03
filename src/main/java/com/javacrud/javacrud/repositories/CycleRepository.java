package com.javacrud.javacrud.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javacrud.javacrud.documents.Cycle;

@Repository
public interface CycleRepository  extends MongoRepository<Cycle, String>{
    List<Cycle> findByUserIdAndMdn(String userId, String mdn);
}
