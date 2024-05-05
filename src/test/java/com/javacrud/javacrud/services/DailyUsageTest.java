package com.javacrud.javacrud.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
                Date today = MockedObjects.getCurrentDate();
                Date yesterday = MockedObjects.addDaysToDate(today, -1);
                Date cycleEndDate = MockedObjects.addMonthToDate(today, 1);
                User mockedUser = MockedObjects.getMockUser();
                Cycle currentCycle = MockedObjects.getMockedCycle(today, cycleEndDate,
                                mockedUser.getId());
                DailyUsageDTO dailyUsageDTO = MockedObjects
                                .getMockedDailyUsageDTO(mockedUser.getId(), today, 100, null);
                DailyUsage previousUsage = MockedObjects.getMockedDailyUsage(mockedUser.getId(),
                                yesterday, 100, null, currentCycle);
                DailyUsage newDailyUsage = MockedObjects.getMockedDailyUsage(mockedUser.getId(),
                                yesterday, 100, null, currentCycle);

                when(userRepository.findById(mockedUser.getId()))
                                .thenReturn(java.util.Optional.of(mockedUser));
                when(dailyUsageRepository.findByMdnAndUsageDateAndUserId(anyString(),
                                any(Date.class), anyString())).thenReturn(null);
                when(dailyUsageRepository.findByMdnAndUsageDateAndUserId(anyString(),
                                any(Date.class), anyString())).thenReturn(previousUsage);
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
                Date today = MockedObjects.getCurrentDate();
                Date cycleEndDate = MockedObjects.addMonthToDate(today, 1);

                User mockedUser = MockedObjects.getMockUser();
                Cycle currentCycle = MockedObjects.getMockedCycle(today, cycleEndDate,
                                mockedUser.getId());
                DailyUsageDTO dailyUsageDTO = MockedObjects
                                .getMockedDailyUsageDTO(mockedUser.getId(), today, 100, null);
                DailyUsage dailyUsage = MockedObjects.getMockedDailyUsage(mockedUser.getId(), today,
                                100, null, currentCycle);
                when(userRepository.findById(mockedUser.getId()))
                                .thenReturn(java.util.Optional.of(mockedUser));
                when(dailyUsageRepository.findByMdnAndUsageDateAndUserId(anyString(),
                                any(Date.class), anyString())).thenReturn(dailyUsage);
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
                Date today = MockedObjects.getCurrentDate();
                Date currentCycleEndDate = MockedObjects.addMonthToDate(today, 1);
                Date nextCycleStartDate = currentCycleEndDate;
                Date nextCycleEndDate = MockedObjects.addMonthToDate(nextCycleStartDate, 1);

                User mockedUser = MockedObjects.getMockUser();

                Cycle currentCycle = MockedObjects.getMockedCycle(today, currentCycleEndDate,
                                mockedUser.getId());
                Cycle nextCycle = MockedObjects.getMockedCycle(nextCycleStartDate, nextCycleEndDate,
                                mockedUser.getId());
                DailyUsageDTO dailyUsageDTO = MockedObjects
                                .getMockedDailyUsageDTO(mockedUser.getId(), currentCycle.getEndDate(), 100, null);
                DailyUsage currentDailyUsage = MockedObjects.getMockedDailyUsage(mockedUser.getId(),
                                today, 100, null, currentCycle);

                when(userRepository.findById(mockedUser.getId()))
                                .thenReturn(java.util.Optional.of(mockedUser));
                when(dailyUsageRepository.findByMdnAndUsageDateAndUserId(anyString(),
                                any(Date.class), anyString())).thenReturn(currentDailyUsage);
                when(cycleRepository.save(any(Cycle.class))).thenReturn(nextCycle);

                when(dailyUsageRepository.save(any(DailyUsage.class)))
                                .thenReturn(currentDailyUsage);

                // Calling the method under test
                DailyUsage result = dailyUsageService.createOrUpdate(dailyUsageDTO);

                // Assertions
                assertNotNull(result);
                assertEquals(dailyUsageDTO.getUsedInMb().longValue() + 100, result.getUsedInMb());
                assertEquals(nextCycle.getId(), result.getNextCycleId());
        }

        @Test
        void testCreateOrUpdate_UserNotFound() {
                // Mocking dependencies
                Date today = MockedObjects.getCurrentDate();
                DailyUsageDTO dailyUsageDTO =
                                MockedObjects.getMockedDailyUsageDTO("user123", today, 100, null);
                User mockedUser = MockedObjects.getMockUser();
                when(userRepository.findById(mockedUser.getId()))
                                .thenReturn(java.util.Optional.empty());

                // Calling the method under test and asserting the exception
                assertThrows(ResponseStatusException.class,
                                () -> dailyUsageService.createOrUpdate(dailyUsageDTO));
        }

        @Test
        void testListReturnsExpectedList() {
                // Arrange
                DailyUsage usage1 = MockedObjects.getMockedDailyUsage(null, null, null, null, null);
                DailyUsage usage2 = MockedObjects.getMockedDailyUsage(null, null, null, null, null);
                List<DailyUsage> expectedList = Arrays.asList(usage1, usage2);

                // Mock the repository behavior
                when(dailyUsageRepository.findAll()).thenReturn(expectedList);

                // Act
                List<DailyUsage> actualList = dailyUsageService.list();

                // Assert
                assertEquals(expectedList, actualList, "Returned list should match the expected list");
        }
}

