package org.freedomfinancestack.razorpay.cas.acs.service.timer.locator;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.OperationNotSupportedException;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.impl.AReqTransactionTimerService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.impl.CReqTransactionTimerService;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionTimeoutServiceLocatorTest {

    @Mock private AReqTransactionTimerService aReqTransactionTimeoutService;

    @Mock private CReqTransactionTimerService cReqTransactionTimeoutService;

    @InjectMocks private TransactionTimeoutServiceLocator locator;

    @BeforeEach
    void setUp() {
        // Your setup code, if any
    }

    @Test
    void testLocateServiceForAReq() throws OperationNotSupportedException {
        // Arrange
        MessageType messageType = MessageType.AReq;

        // Act
        TransactionTimerService result = locator.locateService(messageType);

        // Assert
        assertNotNull(result);
        assertEquals(aReqTransactionTimeoutService, result);
    }

    @Test
    void testLocateServiceForCReq() throws OperationNotSupportedException {
        // Arrange
        MessageType messageType = MessageType.CReq;

        // Act
        TransactionTimerService result = locator.locateService(messageType);

        // Assert
        assertNotNull(result);
        assertEquals(cReqTransactionTimeoutService, result);
    }

    @Test
    void testLocateServiceInvalidMessageType() {
        // Arrange
        MessageType messageType = MessageType.Erro;

        // Act/Assert
        assertThrows(
                OperationNotSupportedException.class, () -> locator.locateService(messageType));
    }
}
