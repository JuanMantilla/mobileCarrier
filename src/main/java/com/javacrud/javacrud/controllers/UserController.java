package com.javacrud.javacrud.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.services.UserService;
import com.javacrud.javacrud.util.UserResponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
