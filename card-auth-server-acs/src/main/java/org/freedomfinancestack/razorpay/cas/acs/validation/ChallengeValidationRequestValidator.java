package org.freedomfinancestack.razorpay.cas.acs.validation;

import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.extensions.validation.validator.Validation;
import org.freedomfinancestack.extensions.validation.validator.enriched.LengthValidator;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.validation.validator.basic.NotNull.notNull;
import static org.freedomfinancestack.extensions.validation.validator.enriched.LengthValidator.lengthValidator;

/**
 * Validates the challenge validation request (CVREQ)
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
public class ChallengeValidationRequestValidator {

    /**
     * Validates the authentication request (CVREQ).
     *
     * @param incomingCvreq The authentication request (CVREQ) to be validated.
     * @throws ValidationException If the request fails validation.
     */
    public void validateRequest(CVReq incomingCvreq) throws ACSValidationException {
        if (incomingCvreq == null) {
            throw new ACSValidationException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, "Transaction data missing");
        }
        validateChallengeValidationRequest(incomingCvreq);
    }

    private void validateChallengeValidationRequest(CVReq incomingCvreq)
            throws ACSValidationException {

        try {
            Validation.validate(
                    ThreeDSDataElement.ACS_TRANS_ID.getFieldName(),
                    incomingCvreq.getTransactionId(),
                    notNull(),
                    lengthValidator(LengthValidator.DataLengthType.FIXED, 36));
        } catch (ValidationException ex) {
            throw new ACSValidationException(ex);
        }
    }
}
