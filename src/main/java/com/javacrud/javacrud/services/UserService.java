package com.javacrud.javacrud.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.DailyUsageRepository;
import com.javacrud.javacrud.repositories.UserRepository;
import com.javacrud.javacrud.util.DateManilpulation;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CycleRepository cycleRepository;
    private final DailyUsageRepository dailyUsageRepository;

    public UserService(UserRepository userRepository, CycleRepository cycleRepository, DailyUsageRepository dailyUsageRepository) {
        this.userRepository = userRepository;
        this.cycleRepository = cycleRepository;
        this.dailyUsageRepository = dailyUsageRepository;
    }

    /**
     * Creates a new user and associated data.
     *
     * @param user The user object to be saved.
     * @return The newly created user.
     */
    public User create(User user) {
        // TODO: In a production environment, replace this random MDN generation with a proper pool mechanism.
        String mdn = Long.toString((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
        User newUser = this.userRepository.save(user);
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 30);

        Date startDate = DateManilpulation.resetTimeToMidnight(new Date());
        Date endDate = DateManilpulation.resetTimeToMidnight(c.getTime());

        Cycle newCycle = new Cycle(
            mdn,
            startDate,
            endDate,
            newUser.getId()
        );

        DailyUsage newDailyUsage = new DailyUsage(
            mdn, user.getId(), startDate, 0, null, newCycle);

        cycleRepository.save(newCycle);
        dailyUsageRepository.save(newDailyUsage);
        newDailyUsage.setCycle(newCycle);

        return newUser;
    }

    public User get(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        return user;
    }

    public List<User> list() {
        return this.userRepository.findAll();
    }

    public User updateUser(User user) {
        try {
            User userDoc = userRepository.findById(user.getId()).get();
            userDoc.setFirstName(user.getFirstName());
            userDoc.setLastName(user.getLastName());
            userDoc.setEmail(user.getEmail());
            return this.userRepository.save(userDoc);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with ID " + user.getId() + " does not exist.");
        }
    }
}
