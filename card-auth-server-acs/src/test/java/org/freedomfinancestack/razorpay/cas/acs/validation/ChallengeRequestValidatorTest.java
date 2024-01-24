package org.freedomfinancestack.razorpay.cas.acs.validation;

import java.util.Map;
import java.util.stream.Stream;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.data.CREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeDSRequestorChallengeInd;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData.createAuthConfigDto;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChallengeRequestValidatorTest {

    // TODO: add message Extension
    @InjectMocks private ChallengeRequestValidator challengeRequestValidator;

    @BeforeEach
    void setUp() {
        new AesEncryptor(NoOpEncryption.INSTANCE);
    }

    @Test
    void validateRequest_IncomingCReq_Null() {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();

        assertThrows(
                ACSValidationException.class,
                () -> challengeRequestValidator.validateRequest(null, transaction));
    }

    @ParameterizedTest
    @MethodSource("provideSuccessData")
    void validateRequest_Success(String name, CREQ creq, Transaction transaction)
            throws ACSValidationException {
        challengeRequestValidator.validateRequest(creq, transaction);
    }

    @ParameterizedTest
    @MethodSource("provideFailureData")
    void validateRequest_Failure(
            String name,
            CREQ creq,
            Transaction transaction,
            ACSValidationException expectedException) {

        ACSValidationException exception =
                assertThrows(
                        ACSValidationException.class,
                        () -> challengeRequestValidator.validateRequest(creq, transaction));

        assertEquals(expectedException.getMessage(), exception.getMessage());
        assertEquals(expectedException.getInternalErrorCode(), exception.getInternalErrorCode());
        assertEquals(
                expectedException.getThreeDSecureErrorCode(), exception.getThreeDSecureErrorCode());
    }

    @ParameterizedTest
    @CsvSource({"Y", "N"})
    void isWhitelistingDataValid_Success(String whitelistingDataEntry)
            throws ACSValidationException {
        CREQ creq =
                CREQTestData.createCREQ(
                        Map.of(
                                "whitelistingDataEntry",
                                whitelistingDataEntry,
                                "sdkCounterStoA",
                                "001"));
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction
                .getTransactionReferenceDetail()
                .setThreeDSRequestorChallengeInd(
                        ThreeDSRequestorChallengeInd
                                .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                .getValue());
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);

        boolean isWhitelistingDataValid =
                challengeRequestValidator.isWhitelistingDataValid(transaction, creq, authConfigDto);

        assertTrue(isWhitelistingDataValid);
    }

    @Test
    void isWhitelistingDataValid_Failure_Blank() {
        CREQ creq = CREQTestData.createCREQ(Map.of("sdkCounterStoA", "001"));
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction
                .getTransactionReferenceDetail()
                .setThreeDSRequestorChallengeInd(
                        ThreeDSRequestorChallengeInd
                                .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                .getValue());
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);

        ACSValidationException exception =
                assertThrows(
                        ACSValidationException.class,
                        () ->
                                challengeRequestValidator.isWhitelistingDataValid(
                                        transaction, creq, authConfigDto));

        assertEquals("Invalid value for whitelistingDataEntry", exception.getMessage());
        assertEquals(InternalErrorCode.INVALID_REQUEST, exception.getInternalErrorCode());
        assertEquals(
                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                exception.getThreeDSecureErrorCode());
    }

    @Test
    void isWhitelistingDataValid_Failure_Incorrect_value() {
        CREQ creq =
                CREQTestData.createCREQ(
                        Map.of(
                                "sdkCounterStoA", "001",
                                "whitelistingDataEntry", "A"));
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        transaction
                .getTransactionReferenceDetail()
                .setThreeDSRequestorChallengeInd(
                        ThreeDSRequestorChallengeInd
                                .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                .getValue());
        AuthConfigDto authConfigDto =
                createAuthConfigDto(null, true, true, AuthType.OTP, AuthType.UNKNOWN);

        ACSValidationException exception =
                assertThrows(
                        ACSValidationException.class,
                        () ->
                                challengeRequestValidator.isWhitelistingDataValid(
                                        transaction, creq, authConfigDto));

        assertEquals("Invalid value for whitelistingDataEntry", exception.getMessage());
        assertEquals(InternalErrorCode.INVALID_REQUEST, exception.getInternalErrorCode());
        assertEquals(
                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, exception.getThreeDSecureErrorCode());
    }

    private static Stream<Arguments> provideSuccessData() {
        new AesEncryptor(NoOpEncryption.INSTANCE);
        return Stream.of(
                Arguments.of(
                        "OOB UiType",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "oobContinue", "true")),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(),
                                "01",
                                "04",
                                null)),
                Arguments.of(
                        "APP Based TEXT UiType, 2.2.0",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "challengeDataEntry", "0000")),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(),
                                "01",
                                "01",
                                null)),
                Arguments.of(
                        "APP Based HTML UiType, 2.2.0",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "challengeHTMLDataEntry", "0000")),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(),
                                "02",
                                "01",
                                null)),
                Arguments.of(
                        (Object) "APP Based Native UiType, 2.1.0",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "messageVersion", "2.1.0",
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID)),
                        updateTransactionMessageVersion(
                                updatedTransactionDetail(
                                        TransactionTestData.createSampleAppTransaction(),
                                        "01",
                                        "01",
                                        null),
                                "2.1.0")));
    }

    private static Stream<Arguments> provideFailureData() {
        new AesEncryptor(NoOpEncryption.INSTANCE);
        return Stream.of(
                Arguments.of(
                        "messageType incorrect",
                        CREQTestData.createCREQ(Map.of("messageType", MessageType.AReq.toString())),
                        TransactionTestData.createSampleBrwTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for messageType")),
                Arguments.of(
                        "messageVersion incorrect",
                        CREQTestData.createCREQ(Map.of("messageVersion", "2.0.0")),
                        TransactionTestData.createSampleBrwTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.MESSAGE_VERSION_NUMBER_NOT_SUPPORTED,
                                "Invalid value for messageVersion")),
                Arguments.of(
                        "messageVersion mis-match",
                        CREQTestData.createCREQ(Map.of("messageVersion", "2.1.0")),
                        TransactionTestData.createSampleBrwTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for messageVersion")),
                Arguments.of(
                        "threeDSServerTransID mis-match",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "threeDSServerTransID",
                                        "2ddc9891-d085-411d-b068-933e30de8233")),
                        TransactionTestData.createSampleBrwTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED,
                                "Invalid value for threeDSServerTransID")),
                Arguments.of(
                        "threeDSServerTransID incorrect length",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "threeDSServerTransID",
                                        "2ddc9891-d085-411d-b068-933e30de82")),
                        TransactionTestData.createSampleBrwTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                                "Invalid value for threeDSServerTransID")),
                Arguments.of(
                        "acsTransID incorrect length",
                        CREQTestData.createCREQ(
                                Map.of("acsTransID", "279301f0-b090-4dfe-b7dc-c7861ea5c1c")),
                        TransactionTestData.createSampleBrwTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                                "Invalid value for acsTransID")),
                Arguments.of(
                        "challengeWindowSize Blank",
                        CREQTestData.createCREQ(Map.of()),
                        TransactionTestData.createSampleBrwTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for challengeWindowSize")),
                Arguments.of(
                        "challengeWindowSize incorrect value",
                        CREQTestData.createCREQ(Map.of("challengeWindowSize", "06")),
                        TransactionTestData.createSampleBrwTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for challengeWindowSize")),
                Arguments.of(
                        "sdkCounterStoA Blank",
                        CREQTestData.createCREQ(Map.of()),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for sdkCounterStoA")),
                Arguments.of(
                        "sdkCounterStoA incorrect length",
                        CREQTestData.createCREQ(Map.of("sdkCounterStoA", "00")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                                "Invalid value for sdkCounterStoA")),
                Arguments.of(
                        "sdkCounterStoA incorrect value",
                        CREQTestData.createCREQ(Map.of("sdkCounterStoA", "000")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                                "Invalid value for sdkCounterStoA")),
                Arguments.of(
                        "sdkTransID Blank",
                        CREQTestData.createCREQ(Map.of("sdkCounterStoA", "001")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for sdkTransID")),
                Arguments.of(
                        "sdkTransID incorrect length",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", "sdkTransID")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                                "Invalid value for sdkTransID")),
                Arguments.of(
                        "sdkTransID incorrect value type",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", "123456789012345678900987654321123456")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for sdkTransID")),
                Arguments.of(
                        "sdkTransID mis-match",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", "279301f0-b090-4dfe-b7dc-c7861ea5c1cd")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for sdkTransID")),
                Arguments.of(
                        "challengeCancel incorrect value",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "challengeCancel", "09")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for challengeCancel")),
                Arguments.of(
                        "threeDSRequestorAppURL Blank",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "challengeDataEntry", "0000")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for threeDSRequestorAppURL")),
                Arguments.of(
                        "OOB oobContinue Blank",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA",
                                        "001",
                                        "sdkTransID",
                                        TransactionTestData.SAMPLE_SDK_TRANS_ID)),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(), "01", "04", null),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for oobContinue")),
                Arguments.of(
                        "OOB oobContinue incorrect value",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "oobContinue", "false")),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(), "01", "04", null),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for oobContinue")),
                Arguments.of(
                        "OOB resendChallenge incorrect value",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "resendChallenge", "A")),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(), "01", "04", null),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for resendChallenge")),
                Arguments.of(
                        "OOB challengeCancel incorrect value",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "resendChallenge", "N",
                                        "challengeCancel", "09")),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(), "01", "04", null),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for challengeCancel")),
                Arguments.of(
                        "OOB incorrect condition",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "resendChallenge", "Y",
                                        "challengeCancel", "01")),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(), "01", "04", null),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid Format - Value")),
                Arguments.of(
                        "challengeDataEntry Blank",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "resendChallenge", "N")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for challengeDataEntry")),
                Arguments.of(
                        "challengeHTMLDataEntry Blank",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA",
                                        "001",
                                        "sdkTransID",
                                        TransactionTestData.SAMPLE_SDK_TRANS_ID)),
                        updatedTransactionDetail(
                                TransactionTestData.createSampleAppTransaction(), "02", "01", null),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for challengeHTMLDataEntry")),
                Arguments.of(
                        "challengeNoEntry incorrect value",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "resendChallenge", "N",
                                        "challengeNoEntry", "N")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for challengeNoEntry")),
                Arguments.of(
                        "resendChallenge incorrect value",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "resendChallenge", "NO")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for resendChallenge")),
                Arguments.of(
                        "challengeCancel incorrect value",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "challengeCancel", "09")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for challengeCancel")),
                Arguments.of(
                        "incorrect condition count",
                        CREQTestData.createCREQ(
                                Map.of(
                                        "sdkCounterStoA", "001",
                                        "sdkTransID", TransactionTestData.SAMPLE_SDK_TRANS_ID,
                                        "challengeCancel", "01",
                                        "resendChallenge", "Y")),
                        TransactionTestData.createSampleAppTransaction(),
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid Format - Value")));
    }

    private static Transaction updatedTransactionDetail(
            Transaction transaction,
            String acsInterface,
            String acsUiTemplate,
            String threeDSRequestorAppURL) {
        transaction.getTransactionSdkDetail().setAcsInterface(acsInterface);
        transaction.getTransactionSdkDetail().setAcsUiTemplate(acsUiTemplate);
        transaction.getTransactionSdkDetail().setThreeDSRequestorAppURL(threeDSRequestorAppURL);
        return transaction;
    }

    private static Transaction updateTransactionMessageVersion(
            Transaction transaction, String messageVersion) {
        transaction.setMessageVersion(messageVersion);
        return transaction;
    }
}
