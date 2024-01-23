package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.freedomfinancestack.extensions.scheduledTask.exception.TaskAlreadyExistException;
import org.freedomfinancestack.extensions.timer.TimerService;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.GenerateECIRequest;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.DecoupledAuthenticationService;
import org.freedomfinancestack.razorpay.cas.acs.service.ECommIndicatorService;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecoupledAuthenticationAsyncServiceTest {

    @Mock private TimerService timerService;

    @Mock private AppConfiguration appConfiguration;

    @Mock private DecoupledAuthenticationService decoupledAuthenticationService;

    @Mock private TransactionService transactionService;

    @Mock private AuthValueGeneratorService authValueGeneratorService;

    @Mock private ResultRequestService resultRequestService;

    @Mock private ECommIndicatorService eCommIndicatorService;

    @InjectMocks private DecoupledAuthenticationAsyncService decoupledAuthenticationAsyncService;

    @Test
    void testScheduleTask() throws TaskAlreadyExistException {
        // Arrange
        String transactionId = "sampleTransactionId";
        getAppConfiguration();
        when(timerService.scheduleTimeoutTask(anyString(), any(), anyLong(), any()))
                .thenReturn(mock(ScheduledFuture.class));

        // Act
        decoupledAuthenticationAsyncService.scheduleTask(
                transactionId, TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED, null);

        // Assert
        verify(timerService, times(1))
                .scheduleTimeoutTask(
                        eq(
                                DecoupledAuthenticationAsyncService
                                                .DECOUPLED_AUTH_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"),
                        any(TimerTask.class),
                        eq(100L),
                        eq(TimeUnit.SECONDS));
    }

    @Test
    void testScheduleTaskWithChallengeRequiredStatus() throws TaskAlreadyExistException {
        // Arrange
        String transactionId = "sampleTransactionId";
        // Act
        decoupledAuthenticationAsyncService.scheduleTask(
                transactionId, TransactionStatus.SUCCESS, null);
        // Assert
        verify(timerService, never()).scheduleTimeoutTask(any(), any(), anyLong(), any());
    }

    @Test
    void testCancelTask() {
        // Arrange
        String transactionId = "sampleTransactionId";
        when(timerService.removeTimeoutTask(any())).thenReturn(true);

        // Act
        decoupledAuthenticationAsyncService.cancelTask(transactionId);

        // Assert
        verify(timerService, times(1))
                .removeTimeoutTask(
                        eq(
                                DecoupledAuthenticationAsyncService
                                                .DECOUPLED_AUTH_TIMER_TASK_IDENTIFIER_KEY
                                        + "["
                                        + transactionId
                                        + "]"));
    }

    @Test
    void testPerformTask() throws Exception {
        // Arrange
        String timerTaskId = "DECOUPLED_AUTH_TIMER_TASK[sampleTimerTaskId]";

        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setPhase(Phase.ARES);
        when(decoupledAuthenticationService.processAuthenticationRequest(any(), any()))
                .thenReturn(new DecoupledAuthenticationResponse(true));

        when(transactionService.findById(any())).thenReturn(transaction);

        // Act
        decoupledAuthenticationAsyncService.performTask(timerTaskId);

        assertEquals(TransactionStatus.SUCCESS, transaction.getTransactionStatus());
        assertEquals(Phase.RREQ, transaction.getPhase());
        // Assert
        verify(transactionService, times(1)).findById(anyString());
        verify(eCommIndicatorService, times(1)).generateECI(any(GenerateECIRequest.class));
        verify(authValueGeneratorService, times(1)).getAuthValue(any());
        verify(resultRequestService, times(1)).handleRreq(any());
        verify(transactionService, times(1)).saveOrUpdate(any());
    }

    @Test
    void testScheduleTaskWithTaskAlreadyExistException() throws TaskAlreadyExistException {
        // Arrange
        String transactionId = "sampleTransactionId";
        getAppConfiguration();
        when(timerService.scheduleTimeoutTask(anyString(), any(), anyLong(), any()))
                .thenThrow(TaskAlreadyExistException.class);

        // Act
        decoupledAuthenticationAsyncService.scheduleTask(
                transactionId, TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED, null);

        // Assert
        verify(timerService, times(1)).scheduleTimeoutTask(any(), any(), anyLong(), any());
    }

    @Test
    void testPerformTaskWithExceptionInRReq() throws Exception {
        // Arrange
        String timerTaskId = "DECOUPLED_AUTH_TIMER_TASK[sampleTimerTaskId]";

        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction.setPhase(Phase.ARES);
        when(decoupledAuthenticationService.processAuthenticationRequest(any(), any()))
                .thenReturn(new DecoupledAuthenticationResponse(true));

        when(transactionService.findById(any())).thenReturn(transaction);
        doThrow(new RuntimeException("Test exception from handleRreq"))
                .when(resultRequestService)
                .handleRreq(any());

        // Act
        decoupledAuthenticationAsyncService.performTask(timerTaskId);

        assertEquals(TransactionStatus.SUCCESS, transaction.getTransactionStatus());
        assertEquals(Phase.RREQ, transaction.getPhase());
        // Assert
        verify(transactionService, times(1)).findById(anyString());
        verify(eCommIndicatorService, times(1)).generateECI(any(GenerateECIRequest.class));
        verify(authValueGeneratorService, times(1)).getAuthValue(any());
        verify(resultRequestService, times(1)).handleRreq(any());
        verify(transactionService, times(1)).saveOrUpdate(any());
    }

    @Test
    void testPerformTaskWithProcessAuthenticationRequestException() throws Exception {
        // Arrange
        String timerTaskId = "DECOUPLED_AUTH_TIMER_TASK[sampleTimerTaskId]";
        when(transactionService.findById(anyString())).thenReturn(mock(Transaction.class));
        when(decoupledAuthenticationService.processAuthenticationRequest(any(), any()))
                .thenThrow(
                        new RuntimeException("Test exception from processAuthenticationRequest"));

        // Act and Assert
        assertDoesNotThrow(() -> decoupledAuthenticationAsyncService.performTask(timerTaskId));
    }

    @Test
    void testPerformTask_FailedTransaction() throws Exception {
        // Arrange
        String timerTaskId = "DECOUPLED_AUTH_TIMER_TASK[sampleTimerTaskId]";

        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        when(decoupledAuthenticationService.processAuthenticationRequest(any(), any()))
                .thenReturn(new DecoupledAuthenticationResponse(false));

        when(transactionService.findById(any())).thenReturn(transaction);
        transaction.setPhase(Phase.ARES);

        // Act
        decoupledAuthenticationAsyncService.performTask(timerTaskId);

        assertEquals(TransactionStatus.FAILED, transaction.getTransactionStatus());
        assertEquals(Phase.RREQ, transaction.getPhase());
        // Assert
        verify(transactionService, times(1)).findById(anyString());
        verify(eCommIndicatorService, times(1)).generateECI(any(GenerateECIRequest.class));
        verify(resultRequestService, times(1)).handleRreq(any());
        verify(transactionService, times(1)).saveOrUpdate(any());
    }

    @Test
    void testCancelTaskWithNonExistingTask() {
        // Arrange
        String transactionId = "nonExistingTransactionId";
        when(timerService.removeTimeoutTask(any())).thenReturn(false);

        // Act
        decoupledAuthenticationAsyncService.cancelTask(transactionId);

        // Assert
        verify(timerService, times(1)).removeTimeoutTask(any());
        // Add assertions for the log statements or other behavior
    }

    private void getAppConfiguration() {
        AppConfiguration.AcsProperties acsProperties = new AppConfiguration.AcsProperties();
        AppConfiguration.AcsProperties.TimeoutConfig timeoutConfig =
                new AppConfiguration.AcsProperties.TimeoutConfig();
        timeoutConfig.setDecoupledAuthDelay(100);
        acsProperties.setTimeout(timeoutConfig);
        when(appConfiguration.getAcs()).thenReturn(acsProperties);
    }
}
