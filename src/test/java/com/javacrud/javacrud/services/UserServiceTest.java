package com.javacrud.javacrud.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.DailyUsageRepository;
import com.javacrud.javacrud.repositories.UserRepository;
import com.javacrud.javacrud.util.UserDTO;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private CycleRepository cycleRepository;
    private DailyUsageRepository dailyUsageRepository;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        cycleRepository = mock(CycleRepository.class);
        dailyUsageRepository = mock(DailyUsageRepository.class);
        userService = new UserService(userRepository, cycleRepository, dailyUsageRepository);
    }

    @Test
    public void testCreateUser() {
        User user = new User("John", "Doe", "john.doe@example.com", "test123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testGetUser() {
        String userId = "1";
        User user = new User(userId, "John", "Doe", "john.doe@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User retrievedUser = userService.get(userId);

        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getFirstName(), retrievedUser.getFirstName());
        assertEquals(user.getLastName(), retrievedUser.getLastName());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
    }

    @Test
    public void testListUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("1", "John", "Doe", "john.doe@example.com"));
        userList.add(new User("2", "Jane", "Smith", "jane.smith@example.com"));
        when(userRepository.findAll()).thenReturn(userList);

        List<User> retrievedUsers = userService.list();

        assertEquals(userList.size(), retrievedUsers.size());
        for (int i = 0; i < userList.size(); i++) {
            assertEquals(userList.get(i).getId(), retrievedUsers.get(i).getId());
            assertEquals(userList.get(i).getFirstName(), retrievedUsers.get(i).getFirstName());
            assertEquals(userList.get(i).getLastName(), retrievedUsers.get(i).getLastName());
            assertEquals(userList.get(i).getEmail(), retrievedUsers.get(i).getEmail());
        }
    }

    @Test
    public void testUpdateUser() {
        String userId = "1";
        UserDTO userDTO = new UserDTO("1","John", "Doe", "john.doe@example.com");
        User updatedUser = new User(userId, "John", "Doe", "john.doe@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(updatedUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User retrievedUser = userService.updateUser(userId, userDTO);

        assertEquals(updatedUser.getId(), retrievedUser.getId());
        assertEquals(updatedUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(updatedUser.getLastName(), retrievedUser.getLastName());
        assertEquals(updatedUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    public void testUpdateUser_NotFound() {
        String userId = "1";
        UserDTO userDTO = new UserDTO("1","John", "Doe", "john.doe@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(userId, userDTO));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
