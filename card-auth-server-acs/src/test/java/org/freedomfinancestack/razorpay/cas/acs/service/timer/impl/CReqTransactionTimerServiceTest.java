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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CReqTransactionTimerServiceTest {

    @Mock private TimerService timerService;

    @Mock private AReqTransactionTimerService aReqTransactionTimeoutService;

    @Mock private AppConfiguration appConfiguration;

    @Mock private TransactionTimeOutService transactionTimeOutService;

    @InjectMocks private CReqTransactionTimerService cReqTransactionTimerService;

    @Test
    void testScheduleTask() throws TaskAlreadyExistException {
        // Arrange
        String transactionId = "sampleTransactionId";
        TransactionStatus transactionStatus = TransactionStatus.CHALLENGE_REQUIRED;
        getAppConfiguration();
        when(timerService.scheduleTimeoutTask(anyString(), any(), anyLong(), any()))
                .thenReturn(mock(ScheduledFuture.class));

        // Act
        cReqTransactionTimerService.scheduleTask(transactionId, transactionStatus, null);

        // Assert
        verify(aReqTransactionTimeoutService, times(1))
                .cancelTask(
                        AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY
                                + "["
                                + transactionId
                                + "]");
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                CReqTransactionTimerService.CREQ_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"),
                        any(TimerTask.class),
                        eq(50L),
                        eq(TimeUnit.SECONDS));
    }

    @Test
    void testCancelTask() {
        // Arrange
        String transactionId = "sampleTransactionId";
        when(timerService.removeTimeoutTask(any())).thenReturn(true);

        // Act
        cReqTransactionTimerService.cancelTask(transactionId);

        // Assert
        verify(timerService, times(1)).removeTimeoutTask(any());
    }

    @Test
    void testCancelTaskWithNonExistingTask() {
        // Arrange
        String transactionId = "nonExistingTransactionId";
        when(timerService.removeTimeoutTask(any())).thenReturn(false);

        // Act
        cReqTransactionTimerService.cancelTask(transactionId);

        // Assert
        verify(timerService, times(1)).removeTimeoutTask(any());
        // Add assertions for the log statements or other behavior
    }

    @Test
    void testPerformTask() {
        // Arrange
        String timerTaskId = "CREQ_TIMER_TASK[sampleTimerTaskId]";
        doNothing()
                .when(transactionTimeOutService)
                .performTimeOutWaitingForChallengeCompletion(any());

        // Act
        cReqTransactionTimerService.performTask(timerTaskId);

        // Assert
        verify(transactionTimeOutService, times(1))
                .performTimeOutWaitingForChallengeCompletion(eq("sampleTimerTaskId"));
    }

    @Test
    void testPerformTaskWithException() {
        // Arrange
        String timerTaskId = "CREQ_TIMER_TASK[exceptionTimerTaskId]";
        doThrow(new RuntimeException("Test exception"))
                .when(transactionTimeOutService)
                .performTimeOutWaitingForChallengeCompletion(any());

        // Act and Assert
        cReqTransactionTimerService.performTask(timerTaskId);
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
        cReqTransactionTimerService.scheduleTask(transactionId, transactionStatus, "5");

        // Assert
        verify(aReqTransactionTimeoutService, times(1))
                .cancelTask(
                        AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY
                                + "["
                                + transactionId
                                + "]");
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                CReqTransactionTimerService.CREQ_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"),
                        any(TimerTask.class),
                        eq(50L),
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
        cReqTransactionTimerService.scheduleTask(transactionId, transactionStatus, null);

        // Assert
        verify(aReqTransactionTimeoutService, times(1))
                .cancelTask(
                        AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY
                                + "["
                                + transactionId
                                + "]");
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                CReqTransactionTimerService.CREQ_TIMER_TASK_IDENTIFIER_KEY
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
        cReqTransactionTimerService.scheduleTask(transactionId, transactionStatus, null);

        // Assert
        verify(aReqTransactionTimeoutService, times(1))
                .cancelTask(
                        AReqTransactionTimerService.AREQ_TIMER_TASK_IDENTIFIER_KEY
                                + "["
                                + transactionId
                                + "]");
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                CReqTransactionTimerService.CREQ_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"),
                        any(TimerTask.class),
                        eq(50L),
                        eq(TimeUnit.SECONDS));
        // Add assertions for the log statements or other behavior
    }

    private void getAppConfiguration() {
        AppConfiguration.AcsProperties acsProperties = new AppConfiguration.AcsProperties();
        AppConfiguration.AcsProperties.TimeoutConfig timeoutConfig =
                new AppConfiguration.AcsProperties.TimeoutConfig();
        timeoutConfig.setChallengeCompletion(50);
        acsProperties.setTimeout(timeoutConfig);
        when(appConfiguration.getAcs()).thenReturn(acsProperties);
    }
}
