package com.razorpay.ffs.cas.acs.service.authvalue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.exception.HSMConnectionException;
import com.razorpay.ffs.cas.acs.exception.InternalErrorCode;
import com.razorpay.ffs.cas.acs.exception.checked.ACSException;
import com.razorpay.ffs.cas.acs.hsm.CvvHSM;
import com.razorpay.ffs.cas.acs.hsm.luna.LunaCvvHSMImpl;
import com.razorpay.ffs.cas.acs.hsm.noop.NoOpCvvHSMImpl;
import com.razorpay.ffs.cas.dao.model.Transaction;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.razorpay.ffs.cas.acs.constant.InternalConstants.NO_OP_HSM;
import static com.razorpay.ffs.cas.acs.constant.LunaHSMConstants.LUNA_HSM;

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
        if (NO_OP_HSM.equals(hsmConfigured)) {
            return applicationContext.getBean(NoOpCvvHSMImpl.class);
        } else if (LUNA_HSM.equals(hsmConfigured)) {
            return applicationContext.getBean(LunaCvvHSMImpl.class);
        }

        throw new IllegalArgumentException("Invalid hsm configuration for hsm: {}" + hsmConfigured);
    }
}
