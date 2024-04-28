package com.javacrud.javacrud.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return this.userRepository.save(user);
    }

    public User get(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        return user;
    }

    public List<User> list() {
        return this.userRepository.findAll();
    }
}
