package com.javacrud.javacrud.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.repositories.UserRepository;
import com.javacrud.javacrud.services.CycleService;
import com.javacrud.javacrud.services.UserService;
import com.javacrud.javacrud.util.DateUsagePair;
import com.javacrud.javacrud.util.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody User user) {
        this.userService.create(user);
        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail()
        );
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable String userId) {
        User user = this.userService.get(userId);
        
        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail()
        );
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        List<User> users = this.userService.list();
        List<UserResponse> payload = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
            );
            payload.add(userResponse);
        }
        return payload;
    }  
}
