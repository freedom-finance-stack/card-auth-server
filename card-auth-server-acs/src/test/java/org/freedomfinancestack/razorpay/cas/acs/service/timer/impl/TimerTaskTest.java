package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import io.micrometer.tracing.Tracer;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimerTaskTest {

    @Mock private TransactionTimerService transactionTimerService;

    @Mock private Tracer tracer;
    @InjectMocks private TimerTask timerTask;

    @Test
    void testRun() {
        // Arrange
        String timerTaskId = "sampleTimerTaskId";
        timerTask = new TimerTask(timerTaskId, transactionTimerService, tracer);
        doNothing().when(transactionTimerService).performTask(timerTaskId);
        // Act
        timerTask.run();

        // Assert
        verify(transactionTimerService, times(1)).performTask(timerTaskId);
    }
}
