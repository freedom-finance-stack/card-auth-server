package org.freedomfinancestack.razorpay.cas.acs.service.authvalue;

import org.freedomfinancestack.extensions.hsm.cvv.CVVFacade;
import org.freedomfinancestack.extensions.hsm.message.HSMMessage;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "cvvGenerationService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CVVGenerationService {

    private final CVVFacade cvvFacade;

    public String generateCVV(@NonNull final Transaction transaction, @NonNull final String data)
            throws ACSException {
        try {
            return cvvFacade.generateCVV(createHSMMessage(transaction, data));
        } catch (Exception e) {
            log.error("generateCVV() Exception Occurred in generating CVV", e);
            throw new ACSException(InternalErrorCode.HSM_INTERNAL_EXCEPTION);
        }
    }

    private HSMMessage createHSMMessage(
            @NonNull final Transaction transaction, @NonNull final String data) {

        // todo - fix logic for fetching hsm kcv from database
        return new HSMMessage("hsm-key", data);
    }
}
