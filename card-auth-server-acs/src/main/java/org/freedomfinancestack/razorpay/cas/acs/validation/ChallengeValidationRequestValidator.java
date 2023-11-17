package org.freedomfinancestack.razorpay.cas.acs.validation;

import org.freedomfinancestack.extensions.validation.enums.DataLengthType;
import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.extensions.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.validation.validator.basic.NotBlank.notBlank;
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
public class ChallengeValidationRequestValidator implements ThreeDSValidator<CVReq> {

    /**
     * Validates the authentication request (CVREQ).
     *
     * @param incomingCvreq The authentication request {@link CVReq} to be validated.
     * @throws ACSValidationException If the request fails validation.
     */
    @Override
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
                    notBlank(),
                    lengthValidator(DataLengthType.FIXED, 36));
        } catch (ValidationException ex) {
            throw new ACSValidationException(ex);
        }
    }
}
