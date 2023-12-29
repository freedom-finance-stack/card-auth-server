package org.freedomfinancestack.razorpay.cas.acs.service.oob;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.OperationNotSupportedException;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.impl.MockOOBService;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.impl.ULTestOOBService;
import org.freedomfinancestack.razorpay.cas.dao.enums.OOBType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OOBServiceLocator {
    private final @Qualifier("ULTestOOBService") ULTestOOBService ulOOBService;
    private final @Qualifier("MockOOBService") MockOOBService mockOOBService;

    public OOBService locateOOBService(OOBType oobType) throws OperationNotSupportedException {
        OOBService oobService = null;

        switch (oobType) {
            case UL_TEST:
                oobService = ulOOBService;
                break;
            case MOCK:
                oobService = mockOOBService;
                break;
            default:
                throw new OperationNotSupportedException(
                        InternalErrorCode.AUTH_CONFIG_NOT_PRESENT, "Invalid OOB Type");
        }
        return oobService;
    }
}
