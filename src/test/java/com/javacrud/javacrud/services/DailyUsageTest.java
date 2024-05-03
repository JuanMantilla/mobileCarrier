package com.javacrud.javacrud.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import com.javacrud.javacrud.documents.Cycle;
import com.javacrud.javacrud.documents.DailyUsage;
import com.javacrud.javacrud.documents.User;
import com.javacrud.javacrud.mockedObjects.MockedObjects;
import com.javacrud.javacrud.repositories.CycleRepository;
import com.javacrud.javacrud.repositories.DailyUsageRepository;
import com.javacrud.javacrud.repositories.UserRepository;
import com.javacrud.javacrud.util.DailyUsageDTO;

class DailyUsageServiceTest {

    @Mock
    private DailyUsageRepository dailyUsageRepository;

    @Mock
    private CycleRepository cycleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DailyUsageService dailyUsageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrUpdate_ValidDailyUsage() {
        // Mocking dependencies
        String strToday = "2024-05-02";
        String strYesterday = "2024-05-01";
        String strCycleEndDate = "2024-06-01";

        Date today = MockedObjects.getMockDate(strToday);
        Date yesterday = MockedObjects.getMockDate(strYesterday);
        Date cycleEndDate = MockedObjects.getMockDate(strCycleEndDate);
        User mockedUser = MockedObjects.getMockUser();
        Cycle currentCycle = MockedObjects.getMockedCycle(today, cycleEndDate, mockedUser.getId());
        DailyUsageDTO dailyUsageDTO =
                MockedObjects.getMockedDailyUsageDTO(mockedUser.getId(), today, 100, null);
        DailyUsage previousUsage = MockedObjects.getMockedDailyUsage(mockedUser.getId(), yesterday,
        100, null, currentCycle);
        DailyUsage newDailyUsage = MockedObjects.getMockedDailyUsage(mockedUser.getId(), yesterday,
        100, null, currentCycle);

        when(userRepository.findById(mockedUser.getId()))
                .thenReturn(java.util.Optional.of(mockedUser));
        when(dailyUsageRepository.findByMdnAndUsageDateAndUserId(anyString(), any(Date.class),
                anyString())).thenReturn(null);
        when(dailyUsageRepository.findByMdnAndUsageDateAndUserId(anyString(), any(Date.class),
                anyString())).thenReturn(previousUsage);
        when(dailyUsageRepository.save(any(DailyUsage.class))).thenReturn(newDailyUsage);

        // Calling the method under test
        DailyUsage result = dailyUsageService.createOrUpdate(dailyUsageDTO);

        // Assertions
        assertNotNull(result);
        assertEquals(newDailyUsage, result);
    }

    @Test
    void testCreateOrUpdate_ValidDailyUsageAddUsage() {
        // Mocking dependencies
        String strToday = "2024-05-02";
        String strCycleEndDate = "2024-06-01";

        Date today = MockedObjects.getMockDate(strToday);
        Date cycleEndDate = MockedObjects.getMockDate(strCycleEndDate);
        User mockedUser = MockedObjects.getMockUser();

        Cycle currentCycle = MockedObjects.getMockedCycle(today, cycleEndDate, mockedUser.getId());
        DailyUsageDTO dailyUsageDTO = MockedObjects.getMockedDailyUsageDTO(mockedUser.getId(), today, 100,
                null);
        DailyUsage dailyUsage = MockedObjects.getMockedDailyUsage(mockedUser.getId(), today, 100, null,
                currentCycle);
        when(userRepository.findById(mockedUser.getId())).thenReturn(java.util.Optional.of(mockedUser));
        when(dailyUsageRepository.findByMdnAndUsageDateAndUserId(anyString(), any(Date.class),
                anyString())).thenReturn(dailyUsage);
        when(dailyUsageRepository.save(any(DailyUsage.class))).thenReturn(dailyUsage);

        // Calling the method under test
        DailyUsage result = dailyUsageService.createOrUpdate(dailyUsageDTO);

        // Assertions
        assertNotNull(result);
        assertEquals(dailyUsageDTO.getUsedInMb().longValue() + 100, result.getUsedInMb());
    }

    @Test
    void testCreateOrUpdate_ValidDailyUsageLastDayOfCycle() {
        // Mocking dependencies
        String strToday = "2024-05-02";
        String strCurrentCycleEndDate = strToday;
        String strNextCycleStartDate = "2024-05-03";
        String strNextCycleEndDate = "2024-06-03";

        Date today = MockedObjects.getMockDate(strToday);
        Date currentCycleEndDate = MockedObjects.getMockDate(strCurrentCycleEndDate);
        Date nextCycleStartDate = MockedObjects.getMockDate(strNextCycleStartDate);
        Date nextCycleEndDate = MockedObjects.getMockDate(strNextCycleEndDate);
        User mockedUser = MockedObjects.getMockUser();

        Cycle currentCycle = MockedObjects.getMockedCycle(today, currentCycleEndDate, mockedUser.getId());
        Cycle nextCycle = MockedObjects.getMockedCycle(nextCycleStartDate, nextCycleEndDate, mockedUser.getId());
        DailyUsageDTO dailyUsageDTO = MockedObjects.getMockedDailyUsageDTO(mockedUser.getId(), today, 100,
                null);
        DailyUsage currentDailyUsage = MockedObjects.getMockedDailyUsage(mockedUser.getId(), today, 100, null,
                currentCycle);

        when(userRepository.findById(mockedUser.getId())).thenReturn(java.util.Optional.of(mockedUser));
        when(dailyUsageRepository.findByMdnAndUsageDateAndUserId(anyString(), any(Date.class),
                anyString())).thenReturn(currentDailyUsage);

        when(cycleRepository.save(any(Cycle.class))).thenReturn(nextCycle);

        when(dailyUsageRepository.save(any(DailyUsage.class))).thenReturn(currentDailyUsage);

        // Calling the method under test
        DailyUsage result = dailyUsageService.createOrUpdate(dailyUsageDTO);

        // Assertions
        assertNotNull(result);
        assertEquals(dailyUsageDTO.getUsedInMb().longValue() + 100, result.getUsedInMb());
        assertEquals(nextCycle.getId(), currentDailyUsage.getNextCycleId());
    }

    @Test
    void testCreateOrUpdate_UserNotFound() {
        // Mocking dependencies
        String strToday = "2024-05-02";

        Date today = MockedObjects.getMockDate(strToday);
        DailyUsageDTO dailyUsageDTO = MockedObjects.getMockedDailyUsageDTO("user123", today, 100,
        null);
        User mockedUser = MockedObjects.getMockUser();
        when(userRepository.findById(mockedUser.getId())).thenReturn(java.util.Optional.empty());

        // Calling the method under test and asserting the exception
        assertThrows(ResponseStatusException.class,
                () -> dailyUsageService.createOrUpdate(dailyUsageDTO));
    }

    // Add more test cases as needed
}

