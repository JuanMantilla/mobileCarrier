package com.javacrud.javacrud.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.javacrud.javacrud.documents.User;

public interface UserRepository extends MongoRepository<User, String>{
}