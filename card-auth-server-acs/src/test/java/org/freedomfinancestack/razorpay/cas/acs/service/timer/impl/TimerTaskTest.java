package org.freedomfinancestack.razorpay.cas.acs.service.timer.impl;

import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

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
        Span mockSpan = mock(Span.class);
        Tracer.SpanInScope mockSpanInScope = mock(Tracer.SpanInScope.class);

        when(tracer.nextSpan()).thenReturn(mockSpan);
        when(tracer.withSpan(mockSpan)).thenReturn(mockSpanInScope);
        when(mockSpan.name(any())).thenReturn(mockSpan);
        when(mockSpan.start()).thenReturn(mockSpan);

        timerTask = new TimerTask(timerTaskId, transactionTimerService, tracer);
        doNothing().when(transactionTimerService).performTask(timerTaskId);
        // Act
        timerTask.run();

        // Assert
        verify(transactionTimerService, times(1)).performTask(timerTaskId);
        verify(mockSpan, times(1)).end();
        verify(mockSpanInScope, times(1)).close();
    }
}
