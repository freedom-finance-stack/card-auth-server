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

import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.validation.validator.basic.IsValidObject.isValidObject;
import static org.freedomfinancestack.extensions.validation.validator.basic.NotNull.notNull;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsIn.isIn;
import static org.freedomfinancestack.extensions.validation.validator.enriched.LengthValidator.lengthValidator;
import static org.freedomfinancestack.extensions.validation.validator.rule.IsListValid.isListValid;
import static org.freedomfinancestack.extensions.validation.validator.rule.When.when;
import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.YES;
import static org.freedomfinancestack.razorpay.cas.acs.validation.AuthenticationRequestValidator.validateDeviceChannelAndMessageCategory;

/**
 * Validates the challenge request (CREQ)
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
public class ChallengeRequestValidator {
    // todo : not implementation of threeDSValidator as it requires few more fields

    /**
     * Validates the authentication request (CREQ).
     *
     * @param incomingCreq The authentication request (CREQ) to be validated.
     * @throws ValidationException If the request fails validation.
     */
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
                notNull(),
                isIn(new String[] {MessageType.CReq.toString()}));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_VERSION.getFieldName(),
                incomingCreq.getMessageVersion(),
                notNull(),
                isIn(ThreeDSDataElement.MESSAGE_VERSION.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(),
                incomingCreq.getThreeDSServerTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID, transaction),
                        notNull()),
                lengthValidator(DataLengthType.VARIABLE, 36));

        Validation.validate(
                ThreeDSDataElement.ACS_TRANS_ID.getFieldName(),
                incomingCreq.getAcsTransID(),
                notNull(),
                lengthValidator(DataLengthType.VARIABLE, 36));

        Validation.validate(
                ThreeDSDataElement.CHALLENGE_WINDOW_SIZE.getFieldName(),
                incomingCreq.getChallengeWindowSize(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.CHALLENGE_WINDOW_SIZE, transaction),
                        notNull()),
                isIn(ThreeDSDataElement.CHALLENGE_WINDOW_SIZE.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.SDK_COUNTER_STOA.getFieldName(),
                incomingCreq.getSdkCounterStoA(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_COUNTER_STOA, transaction),
                        notNull()),
                lengthValidator(DataLengthType.FIXED, 3));
        Validation.validate(
                ThreeDSDataElement.SDK_TRANS_ID.getFieldName(),
                incomingCreq.getSdkTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_TRANS_ID, transaction),
                        notNull()),
                lengthValidator(DataLengthType.VARIABLE, 36));
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
                            && (!Util.isNullorBlank(incomingCreq.getChallengeNoEntry())
                                    && !YES.equals(incomingCreq.getChallengeNoEntry()));
            Validation.validate(
                    ThreeDSDataElement.CHALLENGE_DATA_ENTRY.getFieldName(),
                    incomingCreq.getChallengeDataEntry(),
                    when(conditionForChallengeDataEntry, notNull()),
                    lengthValidator(DataLengthType.VARIABLE, 45));

            boolean conditionForChallengeHTMLDataEntry =
                    (Arrays.asList("01", "02", "03", "04", "05").contains(acsUiType))
                            && !Util.isNullorBlank(incomingCreq.getChallengeCancel());
            Validation.validate(
                    ThreeDSDataElement.CHALLENGE_HTML_DATA_ENTRY.getFieldName(),
                    incomingCreq.getChallengeHTMLDataEntry(),
                    when(conditionForChallengeHTMLDataEntry, notNull()));
        }
    }

    public static boolean validateDeviceChannelAndMessageCategory(
            ThreeDSDataElement element, Transaction transaction) {
        return validateDeviceChannel(element, transaction)
                && validateMessageCategory(element, transaction);
    }

    public static boolean validateDeviceChannel(
            ThreeDSDataElement element, Transaction transaction) {
        return Arrays.stream(element.getSupportedChannel())
                .anyMatch(sc -> sc.getChannel().equals(transaction.getDeviceChannel()));
    }

    public static boolean validateMessageCategory(
            ThreeDSDataElement element, Transaction transaction) {
        return Arrays.stream(element.getSupportedCategory())
                .anyMatch(sc -> sc.equals(transaction.getMessageCategory()));
    }
}
