package org.freedomfinancestack.razorpay.cas.acs.validation;

import java.util.Arrays;

import org.freedomfinancestack.extensions.validation.enums.DataLengthType;
import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.extensions.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.validation.validator.basic.IsUUID.isUUID;
import static org.freedomfinancestack.extensions.validation.validator.basic.IsValidObject.isValidObject;
import static org.freedomfinancestack.extensions.validation.validator.basic.NotBlank.notBlank;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsIn.isIn;
import static org.freedomfinancestack.extensions.validation.validator.enriched.LengthValidator.lengthValidator;
import static org.freedomfinancestack.extensions.validation.validator.rule.IsListValid.isListValid;
import static org.freedomfinancestack.extensions.validation.validator.rule.When.when;
import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.YES;

/**
 * Validates the challenge request {@link CREQ}.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
public class ChallengeRequestValidator implements ThreeDSValidator<CREQ> {
    /**
     * Validates the authentication request {@link CREQ}.
     *
     * @param incomingCreq The authentication request {@link CREQ} to be validated.
     * @throws ACSValidationException If the request fails validation.
     */
    @Override
    public void validateRequest(CREQ incomingCreq, Transaction transaction)
            throws ACSValidationException {
        if (incomingCreq == null) {
            throw new ACSValidationException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, "Transaction data missing");
        }
        try {
            validateChallengeRequest(incomingCreq, transaction);
        } catch (ValidationException vex) {
            throw new ACSValidationException(vex);
        }
    }

    private void validateChallengeRequest(CREQ incomingCreq, Transaction transaction)
            throws ValidationException {
        validateMandatoryFields(incomingCreq, transaction);
        validateOptionalFields(incomingCreq, transaction);
    }

    private void validateMandatoryFields(CREQ incomingCreq, Transaction transaction)
            throws ValidationException {
        Validation.validate(
                ThreeDSDataElement.MESSAGE_TYPE.getFieldName(),
                incomingCreq.getMessageType(),
                notBlank(),
                isIn(new String[] {MessageType.CReq.toString()}));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_VERSION.getFieldName(),
                incomingCreq.getMessageVersion(),
                notBlank(),
                isIn(ThreeDSDataElement.MESSAGE_VERSION.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(),
                incomingCreq.getThreeDSServerTransID(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID, transaction),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 36));

        Validation.validate(
                ThreeDSDataElement.ACS_TRANS_ID.getFieldName(),
                incomingCreq.getAcsTransID(),
                notBlank(),
                lengthValidator(DataLengthType.VARIABLE, 36));

        Validation.validate(
                ThreeDSDataElement.CHALLENGE_WINDOW_SIZE.getFieldName(),
                incomingCreq.getChallengeWindowSize(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.CHALLENGE_WINDOW_SIZE, transaction),
                        notBlank()),
                isIn(ThreeDSDataElement.CHALLENGE_WINDOW_SIZE.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.SDK_COUNTER_STOA.getFieldName(),
                incomingCreq.getSdkCounterStoA(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.SDK_COUNTER_STOA, transaction),
                        notBlank()),
                lengthValidator(DataLengthType.FIXED, 3));
        Validation.validate(
                ThreeDSDataElement.SDK_TRANS_ID.getFieldName(),
                incomingCreq.getSdkTransID(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.SDK_TRANS_ID, transaction),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 36),
                isUUID());
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_APP_URL.getFieldName(),
                incomingCreq.getThreeDSRequestorAppURL(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_REQUESTOR_APP_URL, transaction),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 256));
    }

    protected void validateOptionalFields(CREQ incomingCreq, Transaction transaction)
            throws ValidationException {

        Validation.validate(
                ThreeDSDataElement.RESEND_CHALLENGE.getFieldName(),
                incomingCreq.getResendChallenge(),
                isIn(ThreeDSDataElement.RESEND_CHALLENGE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.CHALLENGE_CANCEL.getFieldName(),
                incomingCreq.getChallengeCancel(),
                isIn(ThreeDSDataElement.CHALLENGE_CANCEL.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_EXTENSION.getFieldName(),
                incomingCreq.getMessageExtension(),
                isListValid(isValidObject()));

        if (transaction.getTransactionSdkDetail().getAcsUiType() != null) {
            String acsUiType = transaction.getTransactionSdkDetail().getAcsUiType();
            boolean conditionForChallengeDataEntry =
                    (Arrays.asList("01", "02", "03").contains(acsUiType)
                                    && !YES.equals(incomingCreq.getResendChallenge()))
                            && Util.isNullorBlank(incomingCreq.getChallengeCancel())
                            && ((!Util.isNullorBlank(incomingCreq.getChallengeNoEntry())
                                    && !YES.equals(incomingCreq.getChallengeNoEntry())));
            Validation.validate(
                    ThreeDSDataElement.CHALLENGE_DATA_ENTRY.getFieldName(),
                    incomingCreq.getChallengeDataEntry(),
                    when(conditionForChallengeDataEntry, notBlank()),
                    lengthValidator(DataLengthType.VARIABLE, 45));

            boolean conditionForChallengeHTMLDataEntry =
                    (Arrays.asList("01", "02", "03", "04", "05").contains(acsUiType))
                            && !Util.isNullorBlank(incomingCreq.getChallengeCancel());
            Validation.validate(
                    ThreeDSDataElement.CHALLENGE_HTML_DATA_ENTRY.getFieldName(),
                    incomingCreq.getChallengeHTMLDataEntry(),
                    when(conditionForChallengeHTMLDataEntry, notBlank()));
        }

        if (transaction.getTransactionSdkDetail().getAcsUiType() != null) {
            String acsUiType = transaction.getTransactionSdkDetail().getAcsUiType();
            boolean conditionForChallengeNoEntry =
                    shouldValidateThreeDSDataElement(
                                    ThreeDSDataElement.CHALLENGE_NO_ENTRY, transaction)
                            && !(Arrays.asList("01", "02", "03").contains(acsUiType))
                            && Util.isNullorBlank(incomingCreq.getChallengeDataEntry())
                            && Util.isNullorBlank(incomingCreq.getChallengeCancel())
                            && Util.isNullorBlank(incomingCreq.getResendChallenge());
            Validation.validate(
                    ThreeDSDataElement.CHALLENGE_NO_ENTRY.getFieldName(),
                    incomingCreq.getChallengeNoEntry(),
                    when(
                            conditionForChallengeNoEntry,
                            isIn(ThreeDSDataElement.CHALLENGE_NO_ENTRY.getAcceptedValues())));
        }
    }

    private static boolean shouldValidateThreeDSDataElement(
            @NonNull final ThreeDSDataElement element, @NonNull final Transaction transaction) {
        return validateDeviceChannel(element, transaction)
                && validateMessageCategory(element, transaction)
                && validateMessageVersion(element, transaction);
    }

    private static boolean validateDeviceChannel(
            @NonNull final ThreeDSDataElement element, @NonNull final Transaction transaction) {
        return Arrays.stream(element.getSupportedChannel())
                .anyMatch(sc -> sc.getChannel().equals(transaction.getDeviceChannel()));
    }

    private static boolean validateMessageCategory(
            @NonNull final ThreeDSDataElement element, @NonNull final Transaction transaction) {
        return Arrays.stream(element.getSupportedCategory())
                .anyMatch(sc -> sc.equals(transaction.getMessageCategory()));
    }

    private static boolean validateMessageVersion(
            @NonNull final ThreeDSDataElement element, @NonNull final Transaction transaction) {
        return Arrays.stream(element.getSupportedMessageVersion())
                .anyMatch(version -> version.equals(transaction.getMessageVersion()));
    }
}
