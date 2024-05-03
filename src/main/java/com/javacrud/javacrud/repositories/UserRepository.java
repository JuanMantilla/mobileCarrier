package com.javacrud.javacrud.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javacrud.javacrud.documents.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
}