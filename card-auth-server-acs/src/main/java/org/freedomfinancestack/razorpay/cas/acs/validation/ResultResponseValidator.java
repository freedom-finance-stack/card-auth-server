package org.freedomfinancestack.razorpay.cas.acs.validation;

import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.extensions.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.validation.validator.basic.IsValidObject.isValidObject;
import static org.freedomfinancestack.extensions.validation.validator.basic.NotNull.notNull;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsIn.isIn;
import static org.freedomfinancestack.extensions.validation.validator.rule.IsListValid.isListValid;

/**
 * Validates the result response (RRES).
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
public class ResultResponseValidator {

    /**
     * Validates the result response (RRES).
     *
     * @param incomingRres The result response (RRES) to be validated.
     * @param rreq the Result request (RREQ) used to get RRES_
     * @throws ValidationException If the request fails validation.
     */
    public void validateRequest(RRES incomingRres, RREQ rreq) throws ACSValidationException {
        if (incomingRres == null || rreq == null) {
            throw new ACSValidationException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, "Transaction data missing");
        }

        try {
            validateResultRequest(incomingRres, rreq);
        } catch (ValidationException vex) {
            throw new ACSValidationException(vex);
        }
    }

    private void validateResultRequest(RRES incomingRres, RREQ rreq) throws ValidationException {

        validateMandatoryFields(incomingRres, rreq);
        validateOptionalFields(incomingRres, rreq);
    }

    private void validateMandatoryFields(RRES incomingRres, RREQ rreq) throws ValidationException {

        Validation.validate(
                ThreeDSDataElement.MESSAGE_TYPE.getFieldName(),
                incomingRres.getMessageType(),
                notNull(),
                isIn(new String[] {MessageType.RRes.toString(), MessageType.Erro.toString()}));

        Validation.validate(
                ThreeDSDataElement.MESSAGE_VERSION.getFieldName(),
                incomingRres.getMessageVersion(),
                notNull(),
                isIn(ThreeDSDataElement.MESSAGE_VERSION.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(),
                incomingRres.getThreeDSServerTransID(),
                isIn(new String[] {rreq.getThreeDSServerTransID()}));

        Validation.validate(
                ThreeDSDataElement.ACS_TRANS_ID.getFieldName(),
                incomingRres.getAcsTransID(),
                notNull(),
                isIn(new String[] {rreq.getAcsTransID()}));

        Validation.validate(
                ThreeDSDataElement.DS_TRANS_ID.getFieldName(),
                incomingRres.getDsTransID(),
                notNull(),
                isIn(new String[] {rreq.getDsTransID()}));

        Validation.validate(
                ThreeDSDataElement.RESULTS_STATUS.getFieldName(),
                incomingRres.getResultsStatus(),
                notNull(),
                isIn(ThreeDSDataElement.RESULTS_STATUS.getAcceptedValues()));
    }

    protected void validateOptionalFields(RRES incomingRres, RREQ rreq) throws ValidationException {
        Validation.validate(
                ThreeDSDataElement.MESSAGE_EXTENSION.getFieldName(),
                incomingRres.getMessageExtension(),
                isListValid(isValidObject()));
    }
}
