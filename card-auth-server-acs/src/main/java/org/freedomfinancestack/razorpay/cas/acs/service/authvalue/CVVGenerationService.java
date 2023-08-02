package org.freedomfinancestack.razorpay.cas.acs.service.authvalue;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.LunaHSMConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.HSMConnectionException;
import org.freedomfinancestack.razorpay.cas.acs.hsm.CvvHSM;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.LunaCvvHSMImpl;
import org.freedomfinancestack.razorpay.cas.acs.hsm.noop.NoOpCvvHSMImpl;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "cvvGenerationService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CVVGenerationService {

    private final ApplicationContext applicationContext;

    @Value("${hsm.enabled_gateway}")
    private String enabledHSMGateway;

    public String generateCVV(@NonNull final Transaction transaction, @NonNull final String data)
            throws ACSException {
        CvvHSM cvvHSM = getCvvHSMImpl(enabledHSMGateway);
        try {
            return cvvHSM.generateCVV(transaction, data);
        } catch (HSMConnectionException hsmConnectionException) {
            throw new ACSException(InternalErrorCode.HSM_INTERNAL_EXCEPTION);
        }
    }

    /**
     * Factory Method to fetch correct HSM configured
     *
     * @param hsmConfigured
     * @return {@link CvvHSM}
     */
    private CvvHSM getCvvHSMImpl(String hsmConfigured) {
        if (InternalConstants.NO_OP_HSM.equals(hsmConfigured)) {
            return applicationContext.getBean(NoOpCvvHSMImpl.class);
        } else if (LunaHSMConstants.LUNA_HSM.equals(hsmConfigured)) {
            return applicationContext.getBean(LunaCvvHSMImpl.class);
        }

        throw new IllegalArgumentException("Invalid hsm configuration for hsm: {}" + hsmConfigured);
    }
}
