package org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.GenerateECIRequest;
import org.freedomfinancestack.razorpay.cas.acs.service.impl.EcommIndicatorServiceImpl;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeRIInd;
import org.freedomfinancestack.razorpay.cas.dao.enums.ECI;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class EcommIndicatorServiceImplTest {

    @InjectMocks private EcommIndicatorServiceImpl ecommIndicatorService;

    @ParameterizedTest
    @CsvSource({
        "VISA, SUCCESS, null, 05",
        "VISA, CREATED, null, 05",
        "VISA, ATTEMPT, null, 06",
        "VISA, FAILED, null, 07",
        "VISA, UNABLE_TO_AUTHENTICATE, null, 07",
        "VISA, REJECTED, null, 07",
        "MASTERCARD, SUCCESS, PA, 02",
        "MASTERCARD, SUCCESS, NPA, N2",
        "MASTERCARD, CREATED, PA, 02",
        "MASTERCARD, CREATED, NPA, N2",
        "MASTERCARD, SUCCESS, PVPA, 02",
        "MASTERCARD, SUCCESS, PVNPA, N2",
        "MASTERCARD, SUCCESS, PA, 02",
        "MASTERCARD, INFORMATIONAL, null, 06",
        "MASTERCARD, ATTEMPT, null, 01",
        "MASTERCARD, FAILED, PA, 00",
        "MASTERCARD, FAILED, NPA, N0",
        "MASTERCARD, FAILED, PVPA, 00",
        "MASTERCARD, FAILED, PVNPA, N0",
        "MASTERCARD, REJECTED, null, 00",
        "AMEX, SUCCESS, null, 05",
        "AMEX, ATTEMPT, null, 06",
        "AMEX, FAILED, null, 07",
        "AMEX, UNABLE_TO_AUTHENTICATE, null, 07",
        "DISCOVER, SUCCESS, null, 05",
        "DISCOVER, ATTEMPT, null, 06",
        "DISCOVER, FAILED, null, 07",
        "DISCOVER, UNABLE_TO_AUTHENTICATE, null, 07",
        "DISCOVER, REJECTED, null, 07"
    })
    public void testGenerateECIForMastercardWithRecurringTransaction(
            String networkName, String status, String messageCategory, String expectedValue) {
        Network network = Network.valueOf(networkName);
        TransactionStatus transactionStatus = TransactionStatus.valueOf(status);
        MessageCategory msgCategory =
                !messageCategory.equals("null") ? MessageCategory.valueOf(messageCategory) : null;

        GenerateECIRequest generateECIRequest =
                new GenerateECIRequest(transactionStatus, network.getValue(), msgCategory);
        String result = ecommIndicatorService.generateECI(generateECIRequest);
        assertEquals(expectedValue, result);
    }

    @Test
    public void testGenerateECIForMastercardWithoutRecurringTransaction() {
        GenerateECIRequest generateECIRequest =
                new GenerateECIRequest(
                        TransactionStatus.CREATED,
                        Network.MASTERCARD.getValue(),
                        MessageCategory.PA);
        generateECIRequest.setThreeRIInd(ThreeRIInd.RECURRING_TRANSACTION.getValue());
        String result = ecommIndicatorService.generateECI(generateECIRequest);
        assertEquals(ECI.MC_SUCCESS_RPA.getValue(), result);
    }

    @Test
    public void testGenerateECIForUnknownNetwork() {
        GenerateECIRequest generateECIRequest =
                new GenerateECIRequest(
                        TransactionStatus.CREATED, Byte.parseByte("5"), MessageCategory.PA);
        String result = ecommIndicatorService.generateECI(generateECIRequest);
        assertNull(result);
    }
}
