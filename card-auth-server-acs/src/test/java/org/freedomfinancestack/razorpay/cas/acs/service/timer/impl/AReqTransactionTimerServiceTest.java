package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.freedomfinancestack.extensions.scheduledTask.exception.TaskAlreadyExistException;
import org.freedomfinancestack.extensions.timer.TimerService;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AReqTransactionTimerServiceTest {

    @Mock private TimerService timerService;

    @Mock private AppConfiguration appConfiguration;

    @Mock private TransactionTimeOutService transactionTimeOutService;

    @InjectMocks private AReqTransactionTimerService aReqTransactionTimerService;

    //    @BeforeEach
    //    void setUp() {
    //        appConfiguration = getAppConfiguration();
    //    }

    @Test
    void testScheduleTask() throws TaskAlreadyExistException {
        // Arrange
        String transactionId = "sampleTransactionId";
        TransactionStatus transactionStatus = TransactionStatus.CHALLENGE_REQUIRED;
        getAppConfiguration();
        when(timerService.scheduleTimeoutTask(anyString(), any(), anyLong(), any()))
                .thenReturn(mock(ScheduledFuture.class));

        // Act
        aReqTransactionTimerService.scheduleTask(transactionId, transactionStatus, null);

        // Assert
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"),
                        any(TimerTask.class),
                        eq(50L),
                        eq(TimeUnit.SECONDS));
    }

    @Test
    void testScheduleTaskWithDecoupledTimeout() throws TaskAlreadyExistException {
        // Arrange
        String transactionId = "sampleTransactionId";
        TransactionStatus transactionStatus = TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED;
        getAppConfiguration();
        when(timerService.scheduleTimeoutTask(anyString(), any(), anyLong(), any()))
                .thenReturn(mock(ScheduledFuture.class));

        // Act
        aReqTransactionTimerService.scheduleTask(transactionId, transactionStatus, "5");

        // Assert
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"),
                        any(TimerTask.class),
                        eq(300L),
                        eq(TimeUnit.SECONDS));
    }

    @Test
    void testScheduleTaskWithDecoupledTimeoutWIthTimeOurParam() throws TaskAlreadyExistException {
        // Arrange
        String transactionId = "sampleTransactionId";
        TransactionStatus transactionStatus = TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED;
        getAppConfiguration();
        when(timerService.scheduleTimeoutTask(anyString(), any(), anyLong(), any()))
                .thenReturn(mock(ScheduledFuture.class));

        // Act
        aReqTransactionTimerService.scheduleTask(transactionId, transactionStatus, null);

        // Assert
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"),
                        any(TimerTask.class),
                        eq(50L),
                        eq(TimeUnit.SECONDS));
    }

    @Test
    void testScheduleTaskWithExistingTask() throws TaskAlreadyExistException {
        // Arrange
        String transactionId = "sampleTransactionId";
        TransactionStatus transactionStatus = TransactionStatus.CHALLENGE_REQUIRED;
        getAppConfiguration();
        when(timerService.scheduleTimeoutTask(anyString(), any(), anyLong(), any()))
                .thenThrow(TaskAlreadyExistException.class);

        // Act
        aReqTransactionTimerService.scheduleTask(transactionId, transactionStatus, null);

        // Assert
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"),
                        any(TimerTask.class),
                        eq(50L),
                        eq(TimeUnit.SECONDS));
        // Add assertions for the log statements or other behavior
    }

    public void getAppConfiguration() {
        AppConfiguration.AcsProperties acsProperties = new AppConfiguration.AcsProperties();
        AppConfiguration.AcsProperties.TimeoutConfig timeoutConfig =
                new AppConfiguration.AcsProperties.TimeoutConfig();
        timeoutConfig.setDecoupledChallengeCompletion(50);
        timeoutConfig.setChallengeRequest(50);
        acsProperties.setTimeout(timeoutConfig);
        when(appConfiguration.getAcs()).thenReturn(acsProperties);
    }

    @Test
    void testCancelTask() {
        // Arrange
        String transactionId = "sampleTransactionId";
        when(timerService.removeTimeoutTask(any())).thenReturn(true);

        // Act
        aReqTransactionTimerService.cancelTask(transactionId);

        // Assert
        verify(timerService, times(1)).removeTimeoutTask(any());
    }

    @Test
    void testCancelTaskWithNonExistingTask() {
        // Arrange
        String transactionId = "nonExistingTransactionId";
        when(timerService.removeTimeoutTask(any())).thenReturn(false);

        // Act
        aReqTransactionTimerService.cancelTask(transactionId);

        // Assert
        verify(timerService, times(1)).removeTimeoutTask(any());
        // Add assertions for the log statements or other behavior
    }

    @Test
    void testPerformTask() throws Exception {
        // Arrange
        String timerTaskId = "AREQ_TIMER_TASK[sampleTimerTaskId]";
        doNothing().when(transactionTimeOutService).performTimeOutWaitingForCreq(any());

        // Act
        aReqTransactionTimerService.performTask(timerTaskId);

        // Assert
        verify(transactionTimeOutService, times(1))
                .performTimeOutWaitingForCreq(eq("sampleTimerTaskId"));
    }

    @Test
    void testPerformTaskWithException() throws Exception {
        // Arrange
        String timerTaskId = "AREQ_TIMER_TASK[exceptionTimerTaskId]";
        doThrow(new RuntimeException("Test exception"))
                .when(transactionTimeOutService)
                .performTimeOutWaitingForCreq(any());

        // Act and Assert
        aReqTransactionTimerService.performTask(timerTaskId);
    }
}
