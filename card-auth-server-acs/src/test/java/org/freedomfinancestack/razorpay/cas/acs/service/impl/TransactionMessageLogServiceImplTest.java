package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.dao.repository.TransactionMessageLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionMessageLogServiceImplTest {

    @Mock private TransactionMessageLogRepository transactionMessageLogRepository;

    @InjectMocks private TransactionMessageLogServiceImpl messageLogService;

    //  incorrect Data found
    @Test
    void testCreateAndSave() {
        // Arrange
        ARES ares = AREQTestData.createSampleARES();
        String transactionId = "sampleTransactionId";
        // Act
        messageLogService.createAndSave(ares, transactionId);
        // Assert
        verify(transactionMessageLogRepository, times(1)).save(any());
    }

    @Test
    void testCreateAndSaveForNull() {
        // Arrange
        String transactionId = "sampleTransactionId";
        // Act
        messageLogService.createAndSave(null, transactionId);
    }
}
