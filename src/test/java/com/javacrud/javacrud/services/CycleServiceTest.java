package com.javacrud.javacrud.services;

import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.mockedObjects.MockedObjects;
import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CycleServiceTest {

    @Mock
    private CycleRepository cycleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CycleService cycleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        // Mocking dependencies
        Date today = MockedObjects.getCurrentDate();
        Date cycleEndDate = MockedObjects.addMonthToDate(today,1);

        User mockedUser = MockedObjects.getMockUser();
        Cycle currentCycle = MockedObjects.getMockedCycle(today, cycleEndDate, mockedUser.getId());

        when(userRepository.existsById(currentCycle.getUserId())).thenReturn(true);
        when(cycleRepository.save(currentCycle)).thenReturn(currentCycle);
        assertEquals(currentCycle, cycleService.create(currentCycle));
    }

    @Test
    public void testList() {
        // Mocking dependencies
        Date today = MockedObjects.getCurrentDate();
        Date cycleEndDate = MockedObjects.addMonthToDate(today,1);

        Date newCycleStartDate = cycleEndDate;
        Date newCycleEndDate = MockedObjects.addMonthToDate(newCycleStartDate, 1);

        User mockedUser = MockedObjects.getMockUser();
        Cycle cycle1 = MockedObjects.getMockedCycle(today, cycleEndDate, mockedUser.getId());
        Cycle cycle2 = MockedObjects.getMockedCycle(newCycleStartDate, newCycleEndDate,
                mockedUser.getId());
        List<Cycle> cycles = Arrays.asList(cycle1, cycle2);

        when(cycleRepository.findAll()).thenReturn(cycles);
        assertEquals(cycles, cycleService.list());
    }

    @Test
    public void testGetLastCycleForUserAndMdn() {
        // Mocking dependencies
        Date today = MockedObjects.getCurrentDate();
        Date cycleEndDate = MockedObjects.addMonthToDate(today,1);

        User mockedUser = MockedObjects.getMockUser();
        Cycle cycle = MockedObjects.getMockedCycle(today, cycleEndDate, mockedUser.getId());
        Query lastCycleQuery = new Query();

        lastCycleQuery.addCriteria(Criteria.where("userId").is(mockedUser.getId()).and("startDate")
                .lte(today).and("mdn").is(cycle.getMdn()));
        lastCycleQuery.with(Sort.by(Sort.Direction.ASC, "startDate"));

        when(userRepository.findById(mockedUser.getId()))
                .thenReturn(java.util.Optional.of(mockedUser));
        when(mongoTemplate.findOne(lastCycleQuery, Cycle.class)).thenReturn(cycle);

        Cycle result = cycleService.getLastCycleForUserAndMdn(mockedUser.getId(), cycle.getMdn());

        assertEquals(cycle, result);
    }

    @Test
    public void testGetCurrentCycleDailyUsage() {
        // Mocking dependencies
        Date today = MockedObjects.getCurrentDate();
        Date cycleEndDate = MockedObjects.addMonthToDate(today,1);
        Date tomorrow = MockedObjects.addDaysToDate(today, 1);

        User mockedUser = MockedObjects.getMockUser();
        Cycle cycle = MockedObjects.getMockedCycle(today, cycleEndDate, mockedUser.getId());
        Query dailyUsagesQuery = new Query();
        DailyUsage dailyUsage1 =
                MockedObjects.getMockedDailyUsage(mockedUser.getId(), today, 100, null, cycle);
        DailyUsage dailyUsage2 =
                MockedObjects.getMockedDailyUsage(mockedUser.getId(), tomorrow, 100, null, cycle);
        List<DailyUsage> expected = Arrays.asList(dailyUsage1, dailyUsage2);

        dailyUsagesQuery.addCriteria(Criteria.where("userId").is(mockedUser.getId()).and("mdn")
                .is(cycle.getMdn()).and("usageDate").gte(cycle.getStartDate()));

        when(userRepository.findById(mockedUser.getId()))
                .thenReturn(java.util.Optional.of(mockedUser));
        when(cycleService.getLastCycleForUserAndMdn(mockedUser.getId(), cycle.getMdn()))
                .thenReturn(cycle);
        when(mongoTemplate.find(dailyUsagesQuery, DailyUsage.class)).thenReturn(expected);

        List<DailyUsage> result =
                cycleService.getCurrentCycleDailyUsage(mockedUser.getId(), cycle.getMdn());

        assertEquals(expected, result);
    }

    @Test
    public void testGetCurrentCycleDailyUsageNoCurrentCycle() {
        // Mocking dependencies
        Date today = MockedObjects.getCurrentDate();
        Date cycleEndDate = MockedObjects.addMonthToDate(today,1);
        Date tomorrow = MockedObjects.addDaysToDate(today, 1);

        User mockedUser = MockedObjects.getMockUser();
        Cycle cycle = MockedObjects.getMockedCycle(tomorrow, cycleEndDate, mockedUser.getId());
        Query dailyUsagesQuery = new Query();

        // Query to find daily usages within the current cycle
        dailyUsagesQuery.addCriteria(Criteria.where("userId").is(mockedUser.getId()).and("mdn")
                .is(cycle.getMdn()).and("usageDate").gte(cycle.getStartDate()));
        when(userRepository.findById(mockedUser.getId()))
                .thenReturn(java.util.Optional.of(mockedUser));
        when(cycleService.getLastCycleForUserAndMdn(mockedUser.getId(), cycle.getMdn()))
                .thenReturn(null);

        List<DailyUsage> result =
                cycleService.getCurrentCycleDailyUsage(mockedUser.getId(), cycle.getMdn());

        assertEquals(new ArrayList<>(), result);
    }

    // @Test
    // public void testGetCycleHistory() {
    // String userId = "user1";
    // String mdn = "mdn1";
    // Cycle cycle1 = new Cycle();
    // Cycle cycle2 = new Cycle();
    // List<Cycle> cycles = Arrays.asList(cycle1, cycle2);
    // when(cycleRepository.findByUserIdAndMdn(userId, mdn)).thenReturn(cycles);
    // assertEquals(cycles, cycleService.getCycleHistory(userId, mdn));
    // }
}
