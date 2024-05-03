package com.javacrud.javacrud.repositories;

import java.util.Date;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javacrud.javacrud.documents.DailyUsage;

@Repository
public interface DailyUsageRepository  extends MongoRepository<DailyUsage, String>{
    DailyUsage findByMdnAndUsageDateAndUserId( String mdn, Date usageDate, String userId);
}
