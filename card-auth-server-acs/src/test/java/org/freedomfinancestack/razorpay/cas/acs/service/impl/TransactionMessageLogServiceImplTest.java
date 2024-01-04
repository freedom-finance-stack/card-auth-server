package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.RREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSObject;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionMessageLog;
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

    @Test
    void testGetTransactionMessagesByTransactionId() throws ThreeDSException {
        // Arrange
        String transactionId = "sampleTransactionId";

        List<TransactionMessageLog> messageTypeDetails = createSampleMessageLogs(transactionId);
        when(transactionMessageLogRepository.findAllByTransactionId(transactionId))
                .thenReturn(messageTypeDetails);

        // Act
        Map<MessageType, ThreeDSObject> messageMap =
                messageLogService.getTransactionMessagesByTransactionId(transactionId);

        // Assert
        assertNotNull(messageMap);
        assertEquals(4, messageMap.size());
        assertTrue(messageMap.containsKey(MessageType.RReq));
        assertTrue(messageMap.containsKey(MessageType.RRes));
        assertTrue(messageMap.containsKey(MessageType.ARes));
        assertTrue(messageMap.containsKey(MessageType.AReq));
    }

    private List<TransactionMessageLog> createSampleMessageLogs(String transactionId) {
        List<TransactionMessageLog> messageTypeDetails = new ArrayList<>();
        messageTypeDetails.add(
                TransactionMessageLog.builder()
                        .messageType(MessageType.ARes)
                        .transactionId(transactionId)
                        .message(Util.toJson(AREQTestData.createSampleARES()))
                        .id("ARES_ID")
                        .build());
        messageTypeDetails.add(
                TransactionMessageLog.builder()
                        .messageType(MessageType.AReq)
                        .transactionId(transactionId)
                        .message(Util.toJson(AREQTestData.createSampleAREQ()))
                        .id("AREQ_ID")
                        .build());
        messageTypeDetails.add(
                TransactionMessageLog.builder()
                        .messageType(MessageType.RRes)
                        .transactionId(transactionId)
                        .message(Util.toJson(RREQTestData.getValidRRes()))
                        .id("RRES_ID")
                        .build());
        messageTypeDetails.add(
                TransactionMessageLog.builder()
                        .messageType(MessageType.RReq)
                        .transactionId(transactionId)
                        .message(Util.toJson(RREQTestData.getValidRReq()))
                        .id("RREQ_ID")
                        .build());
        return messageTypeDetails;
    }

    @Test
    void testGetTransactionMessagesByTransactionIdNoMessages() throws ThreeDSException {
        // Arrange
        String transactionId = "nonExistentTransactionId";
        when(transactionMessageLogRepository.findAllByTransactionId(transactionId))
                .thenReturn(null);

        // Act
        Map<MessageType, ThreeDSObject> messageMap =
                messageLogService.getTransactionMessagesByTransactionId(transactionId);

        // Assert
        assertNull(messageMap);
    }
}
