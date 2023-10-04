package org.freedomfinancestack.razorpay.cas.acs.service.timer.locator;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.OperationNotSupportedException;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.TransactionTimerService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.impl.AReqTransactionTimerService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.impl.CReqTransactionTimerService;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionTimeoutServiceLocator {
    private final @Qualifier("aReqTransactionTimerService") AReqTransactionTimerService
            aReqTransactionTimeoutService;
    private final @Qualifier("cReqTransactionTimerService") CReqTransactionTimerService
            cReqTransactionTimeoutService;

    public TransactionTimerService locateService(MessageType messageType)
            throws OperationNotSupportedException {
        TransactionTimerService transactionTimerService = null;

        switch (messageType) {
            case AReq:
                transactionTimerService = aReqTransactionTimeoutService;
                break;
            case CReq:
                transactionTimerService = cReqTransactionTimeoutService;
                break;
            default:
                throw new OperationNotSupportedException(
                        InternalErrorCode.INVALID_CONFIG, "Invalid messageType Type");
        }
        return transactionTimerService;
    }
}
