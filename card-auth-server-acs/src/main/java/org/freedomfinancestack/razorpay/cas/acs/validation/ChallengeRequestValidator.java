package org.freedomfinancestack.razorpay.cas.acs.validation;

import java.util.Arrays;
import java.util.stream.Stream;

import org.freedomfinancestack.extensions.validation.enums.DataLengthType;
import org.freedomfinancestack.extensions.validation.exception.ValidationErrorCode;
import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.extensions.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.ThreeDSConstant;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.validation.validator.basic.IsUUID.isUUID;
import static org.freedomfinancestack.extensions.validation.validator.basic.IsValidObject.isValidObject;
import static org.freedomfinancestack.extensions.validation.validator.basic.NotBlank.notBlank;
import static org.freedomfinancestack.extensions.validation.validator.basic.NotEmpty.notEmpty;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsEqual.isEqual;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsIn.isIn;
import static org.freedomfinancestack.extensions.validation.validator.enriched.LengthValidator.lengthValidator;
import static org.freedomfinancestack.extensions.validation.validator.rule.IsListValid.isListValid;
import static org.freedomfinancestack.extensions.validation.validator.rule.When.when;
import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.NO;
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
                isIn(ThreeDSDataElement.MESSAGE_VERSION.getAcceptedValues()),
                isEqual(transaction.getMessageVersion()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(),
                incomingCreq.getThreeDSServerTransID(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID, transaction),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 36),
                isEqual(
                        transaction
                                .getTransactionReferenceDetail()
                                .getThreedsServerTransactionId()));

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
                                        ThreeDSDataElement.CHALLENGE_WINDOW_SIZE, transaction)
                                && Util.isNullorBlank(incomingCreq.getChallengeHTMLDataEntry())
                                && Util.isNullorBlank(incomingCreq.getChallengeCancel())
                                && Util.isNullorBlank(incomingCreq.getResendChallenge()),
                        notBlank()),
                isIn(ThreeDSDataElement.CHALLENGE_WINDOW_SIZE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.SDK_COUNTER_STOA.getFieldName(),
                incomingCreq.getSdkCounterStoA(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.SDK_COUNTER_STOA, transaction)
                                & !Util.isNullorBlank(
                                        transaction.getTransactionSdkDetail().getAcsCounterAtoS()),
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
                isUUID(),
                isEqual(transaction.getTransactionSdkDetail().getSdkTransId()));
    }

    protected void validateOptionalFields(CREQ incomingCreq, Transaction transaction)
            throws ValidationException {

        Validation.validate(
                ThreeDSDataElement.CHALLENGE_CANCEL.getFieldName(),
                incomingCreq.getChallengeCancel(),
                isIn(ThreeDSDataElement.CHALLENGE_CANCEL.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_EXTENSION.getFieldName(),
                incomingCreq.getMessageExtension(),
                isListValid(isValidObject()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_APP_URL.getFieldName(),
                incomingCreq.getThreeDSRequestorAppURL(),
                when(
                        incomingCreq.getThreeDSRequestorAppURL() != null
                                && shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.THREEDS_REQUESTOR_APP_URL, transaction),
                        lengthValidator(DataLengthType.VARIABLE, 256)));

        validateAppChallengeData(transaction, incomingCreq);

        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_APP_URL.getFieldName(),
                incomingCreq.getThreeDSRequestorAppURL(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_REQUESTOR_APP_URL, transaction),
                        notEmpty()),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.THREEDS_REQUESTOR_APP_URL, transaction)
                                && !Util.isNullorBlank(
                                        transaction
                                                .getTransactionSdkDetail()
                                                .getThreeDSRequestorAppURL()),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 256));
    }

    private void validateAppChallengeData(Transaction transaction, CREQ incomingCreq)
            throws ValidationException {
        if (DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel())
                && transaction.getTransactionSdkDetail().getAcsUiTemplate() != null
                && !incomingCreq
                        .getSdkCounterStoA()
                        .equals(InternalConstants.INITIAL_ACS_SDK_COUNTER)) {
            String acsUiType = transaction.getTransactionSdkDetail().getAcsUiTemplate();
            String acsInterface = transaction.getTransactionSdkDetail().getAcsInterface();

            if (transaction.getTransactionSdkDetail().getAcsUiType().equals(UIType.OOB.getType())) {

                boolean conditionForOobContinue =
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.OOB_CONTINUE, transaction)
                                && Util.isNullorBlank(incomingCreq.getChallengeCancel())
                                && (incomingCreq.getResendChallenge() == null
                                        || NO.equals(incomingCreq.getResendChallenge()));

                Validation.validate(
                        ThreeDSDataElement.OOB_CONTINUE.getFieldName(),
                        incomingCreq.getOobContinue(),
                        when(conditionForOobContinue, notBlank()),
                        isIn(ThreeDSDataElement.OOB_CONTINUE.getAcceptedValues()));

                boolean conditionForResendChallenge =
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.RESEND_CHALLENGE, transaction)
                                && acsInterface.equals(DeviceInterface.NATIVE.getValue())
                                && Util.isNullorBlank(incomingCreq.getOobContinue())
                                && Util.isNullorBlank(incomingCreq.getChallengeCancel());

                Validation.validate(
                        ThreeDSDataElement.RESEND_CHALLENGE.getFieldName(),
                        incomingCreq.getResendChallenge(),
                        when(conditionForResendChallenge, notBlank()),
                        isIn(ThreeDSDataElement.RESEND_CHALLENGE.getAcceptedValues()));

                boolean conditionForChallengeCancel =
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.CHALLENGE_CANCEL, transaction)
                                && Util.isNullorBlank(incomingCreq.getOobContinue())
                                && (incomingCreq.getResendChallenge() == null
                                        || NO.equals(incomingCreq.getResendChallenge()));

                Validation.validate(
                        ThreeDSDataElement.CHALLENGE_CANCEL.getFieldName(),
                        incomingCreq.getChallengeCancel(),
                        when(conditionForChallengeCancel, notBlank()),
                        isIn(ThreeDSDataElement.CHALLENGE_CANCEL.getAcceptedValues()));

                // Exactly one of the condition should pass
                if (Stream.of(
                                        conditionForChallengeCancel,
                                        conditionForOobContinue,
                                        conditionForResendChallenge)
                                .filter(Boolean::valueOf)
                                .count()
                        != 1) {
                    throw new ValidationException(ValidationErrorCode.INVALID_FORMAT_VALUE);
                }
            } else {
                if (incomingCreq.getMessageVersion().equals(ThreeDSConstant.MESSAGE_VERSION_2_1_0)
                        && (incomingCreq.getResendChallenge() == null
                                || NO.equals(incomingCreq.getResendChallenge()))
                        && incomingCreq.getChallengeCancel() == null
                        && incomingCreq.getChallengeDataEntry() == null
                        && incomingCreq.getChallengeHTMLDataEntry() == null
                        && incomingCreq.getOobContinue() == null
                        && incomingCreq.getChallengeNoEntry() == null
                        && !acsInterface.equals(DeviceInterface.HTML.getValue())) {
                    return;
                }

                boolean conditionForChallengeDataEntry =
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.CHALLENGE_DATA_ENTRY, transaction)
                                && acsInterface.equals(DeviceInterface.NATIVE.getValue())
                                && Arrays.asList("01", "02", "03").contains(acsUiType)
                                && (Util.isNullorBlank(incomingCreq.getChallengeNoEntry())
                                        || !YES.equals(incomingCreq.getChallengeNoEntry()))
                                && Util.isNullorBlank(incomingCreq.getChallengeCancel())
                                && (incomingCreq.getResendChallenge() == null
                                        || NO.equals(incomingCreq.getResendChallenge()));
                Validation.validate(
                        ThreeDSDataElement.CHALLENGE_DATA_ENTRY.getFieldName(),
                        incomingCreq.getChallengeDataEntry(),
                        when(conditionForChallengeDataEntry, notBlank()),
                        lengthValidator(DataLengthType.VARIABLE, 45));

                boolean conditionForChallengeHTMLDataEntry =
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.CHALLENGE_HTML_DATA_ENTRY, transaction)
                                && acsInterface.equals(DeviceInterface.HTML.getValue())
                                && Util.isNullorBlank(incomingCreq.getChallengeCancel());
                Validation.validate(
                        ThreeDSDataElement.CHALLENGE_HTML_DATA_ENTRY.getFieldName(),
                        incomingCreq.getChallengeHTMLDataEntry(),
                        when(conditionForChallengeHTMLDataEntry, notBlank()),
                        lengthValidator(DataLengthType.VARIABLE, 256));

                boolean conditionForChallengeNoEntry =
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.CHALLENGE_NO_ENTRY, transaction)
                                && Arrays.asList("01", "02", "03").contains(acsUiType)
                                && Util.isNullorBlank(incomingCreq.getChallengeDataEntry())
                                && Util.isNullorBlank(incomingCreq.getChallengeHTMLDataEntry())
                                && Util.isNullorBlank(incomingCreq.getChallengeCancel())
                                && (incomingCreq.getResendChallenge() == null
                                        || NO.equals(incomingCreq.getResendChallenge()));
                Validation.validate(
                        ThreeDSDataElement.CHALLENGE_NO_ENTRY.getFieldName(),
                        incomingCreq.getChallengeNoEntry(),
                        when(conditionForChallengeNoEntry, notBlank()),
                        isIn(ThreeDSDataElement.CHALLENGE_NO_ENTRY.getAcceptedValues()));

                boolean conditionForResendChallenge =
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.RESEND_CHALLENGE, transaction)
                                && acsInterface.equals(DeviceInterface.NATIVE.getValue())
                                && Util.isNullorBlank(incomingCreq.getChallengeDataEntry())
                                && Util.isNullorBlank(incomingCreq.getChallengeCancel())
                                && (Util.isNullorBlank(incomingCreq.getChallengeNoEntry())
                                        || !YES.equals(incomingCreq.getChallengeNoEntry()));

                Validation.validate(
                        ThreeDSDataElement.RESEND_CHALLENGE.getFieldName(),
                        incomingCreq.getResendChallenge(),
                        when(conditionForResendChallenge, notBlank()),
                        isIn(ThreeDSDataElement.RESEND_CHALLENGE.getAcceptedValues()));

                boolean conditionForChallengeCancel =
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.CHALLENGE_CANCEL, transaction)
                                && Util.isNullorBlank(incomingCreq.getChallengeDataEntry())
                                && Util.isNullorBlank(incomingCreq.getChallengeHTMLDataEntry())
                                && Util.isNullorBlank(incomingCreq.getResendChallenge())
                                && (Util.isNullorBlank(incomingCreq.getChallengeNoEntry())
                                        || !YES.equals(incomingCreq.getChallengeNoEntry()));

                Validation.validate(
                        ThreeDSDataElement.CHALLENGE_CANCEL.getFieldName(),
                        incomingCreq.getChallengeCancel(),
                        when(conditionForChallengeCancel, notBlank()),
                        isIn(ThreeDSDataElement.CHALLENGE_CANCEL.getAcceptedValues()));

                // Exactly one of the condition should pass
                if (Stream.of(
                                        conditionForChallengeCancel,
                                        conditionForChallengeNoEntry,
                                        conditionForChallengeHTMLDataEntry,
                                        conditionForChallengeDataEntry,
                                        conditionForResendChallenge)
                                .filter(Boolean::valueOf)
                                .count()
                        != 1) {
                    throw new ValidationException(ValidationErrorCode.INVALID_FORMAT_VALUE);
                }
            }
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

    public boolean isWhitelistingDataValid(
            Transaction transaction, CREQ creq, AuthConfigDto authConfigDto)
            throws ACSValidationException {
        try {
            Validation.validate(
                    ThreeDSDataElement.WHITE_LISTING_DATA_ENTRY.getFieldName(),
                    creq.getWhitelistingDataEntry(),
                    when(
                            !Util.isNullorBlank(creq.getSdkCounterStoA())
                                    && !creq.getSdkCounterStoA()
                                            .equals(InternalConstants.INITIAL_ACS_SDK_COUNTER)
                                    && !Util.isNullorBlank(
                                            transaction
                                                    .getTransactionReferenceDetail()
                                                    .getThreeDSRequestorChallengeInd())
                                    && transaction
                                            .getTransactionReferenceDetail()
                                            .getThreeDSRequestorChallengeInd()
                                            .equals(
                                                    ThreeDSRequestorChallengeInd
                                                            .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                                            .getValue())
                                    && authConfigDto
                                            .getChallengeAttemptConfig()
                                            .isWhitelistingAllowed(),
                            notBlank()),
                    isIn(ThreeDSDataElement.WHITE_LISTING_DATA_ENTRY.getAcceptedValues()));
        } catch (ValidationException vex) {
            throw new ACSValidationException(vex);
        }
        return true;
    }
}
