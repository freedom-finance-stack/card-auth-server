package org.freedomfinancestack.razorpay.cas.acs.service.authvalue;

import org.freedomfinancestack.extensions.hsm.cvv.CVVFacade;
import org.freedomfinancestack.extensions.hsm.exception.HSMException;
import org.freedomfinancestack.extensions.hsm.message.HSMMessage;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CVVGenerationServiceTest {

    @Mock private CVVFacade cvvFacade;

    @InjectMocks private CVVGenerationService cvvGenerationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCVV() throws ACSException, HSMException {
        // Mock dependencies
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        String fakeCVV = "123";

        when(cvvFacade.generateCVV(any())).thenReturn(fakeCVV);

        // Test the service method
        String generatedCVV = cvvGenerationService.generateCVV(transaction, "fakeData");

        // Verify the result
        assertNotNull(generatedCVV);
        assertEquals(fakeCVV, generatedCVV);

        // Verify that generateCVV was called with the correct HSMMessage
        verify(cvvFacade).generateCVV(any(HSMMessage.class));
    }

    @Test
    void testGenerateCVVException() throws HSMException {
        // Mock dependencies
        Transaction transaction = TransactionTestData.createSampleAppTransaction();

        // Simulate an exception during CVV generation
        when(cvvFacade.generateCVV(any())).thenThrow(new RuntimeException("Simulated error"));

        // Test the service method
        assertThrows(
                ACSException.class,
                () -> cvvGenerationService.generateCVV(transaction, "fakeData"),
                "HSM_INTERNAL_EXCEPTION");
    }
}
