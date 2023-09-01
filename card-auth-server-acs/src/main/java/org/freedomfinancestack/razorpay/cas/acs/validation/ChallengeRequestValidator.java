package org.freedomfinancestack.razorpay.cas.acs.validation;

import java.util.Arrays;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.LengthValidator;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.NO;
import static org.freedomfinancestack.razorpay.cas.acs.validation.AuthenticationRequestValidator.validateDeviceChannelAndMessageCategory;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic.IsValidObject.isValidObject;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic.NotNull.notNull;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.IsIn.isIn;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.LengthValidator.lengthValidator;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.rule.IsListValid.isListValid;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.rule.When.when;

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
    public void validateRequest(CREQ incomingCreq, AREQ areq, CRES cres)
            throws ValidationException {
        if (incomingCreq == null || areq == null || cres == null) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, "Transaction data missing");
        }
        validateChallengeRequest(incomingCreq, areq, cres);
    }

    private void validateChallengeRequest(CREQ incomingCreq, AREQ areq, CRES cres)
            throws ValidationException {
        validateMandatoryFields(incomingCreq, areq, cres);
        validateOptionalFields(incomingCreq, areq, cres);
    }

    private void validateMandatoryFields(CREQ incomingCreq, AREQ areq, CRES cres)
            throws ValidationException {
        Validation.validate(
                ThreeDSDataElement.MESSAGE_TYPE.getFieldName(),
                incomingCreq.getMessageType(),
                notNull(),
                isIn(new String[] {"CReq"}));
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
                                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID, areq),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 36));

        Validation.validate(
                ThreeDSDataElement.ACS_TRANS_ID.getFieldName(),
                incomingCreq.getAcsTransID(),
                notNull(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 36));

        Validation.validate(
                ThreeDSDataElement.CHALLENGE_WINDOW_SIZE.getFieldName(),
                incomingCreq.getChallengeWindowSize(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.CHALLENGE_WINDOW_SIZE, areq),
                        notNull()),
                isIn(ThreeDSDataElement.CHALLENGE_WINDOW_SIZE.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.SDK_COUNTER_STOA.getFieldName(),
                incomingCreq.getSdkCounterStoA(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_COUNTER_STOA, areq),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 3));
        Validation.validate(
                ThreeDSDataElement.SDK_TRANS_ID.getFieldName(),
                incomingCreq.getSdkTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_TRANS_ID, areq),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 36));
    }

    protected void validateOptionalFields(CREQ incomingCreq, AREQ areq, CRES cres)
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

        if (cres != null) {
            String acsUiType = cres.getAcsUiType();
            boolean conditionForChallengeDataEntry =
                    (Arrays.asList("01", "02", "03").contains(acsUiType)
                                    && NO.equals(incomingCreq.getResendChallenge()))
                            && Util.isNullorBlank(incomingCreq.getChallengeCancel())
                            && (!Util.isNullorBlank(incomingCreq.getChallengeNoEntry())
                                    && !incomingCreq.getChallengeNoEntry().equals("Y"));
            Validation.validate(
                    ThreeDSDataElement.CHALLENGE_DATA_ENTRY.getFieldName(),
                    incomingCreq.getChallengeDataEntry(),
                    when(conditionForChallengeDataEntry, notNull()),
                    lengthValidator(LengthValidator.DataLengthType.VARIABLE, 45));

            boolean conditionForChallengeHTMLDataEntry =
                    (Arrays.asList("01", "02", "03", "04", "05").contains(acsUiType))
                            && !Util.isNullorBlank(incomingCreq.getChallengeCancel());
            Validation.validate(
                    ThreeDSDataElement.CHALLENGE_HTML_DATA_ENTRY.getFieldName(),
                    incomingCreq.getChallengeHTMLDataEntry(),
                    when(conditionForChallengeHTMLDataEntry, notNull()));
        }
    }
}
