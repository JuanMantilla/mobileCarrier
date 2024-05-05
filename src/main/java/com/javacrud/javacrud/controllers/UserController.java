package com.javacrud.javacrud.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.javacrud.javacrud.DTOs.UserDTO;
import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.services.UserService;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public UserDTO createUser(@RequestBody User user) {
        this.userService.create(user);
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable String userId) {
        User user = this.userService.get(userId);

        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        List<User> users = this.userService.list();
        List<UserDTO> payload = new ArrayList<>();
        for (User user : users) {
            UserDTO userResponse = new UserDTO(user.getId(), user.getFirstName(),
                    user.getLastName(), user.getEmail());
            payload.add(userResponse);
        }
        return payload;
    }

    @PatchMapping(value = "/{userId}")
    public UserDTO updateUser(@PathVariable String userId, @RequestBody UserDTO userDto) {
        User response = userService.updateUser(userId, userDto);
        return new UserDTO(response.getId(), response.getFirstName(), response.getLastName(),
                response.getEmail());
    }
}
