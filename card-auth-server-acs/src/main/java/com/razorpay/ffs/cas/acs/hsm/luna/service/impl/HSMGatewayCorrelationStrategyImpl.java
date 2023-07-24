package com.razorpay.ffs.cas.acs.hsm.luna.service.impl;

import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.razorpay.ffs.cas.acs.hsm.luna.domain.HSMTransactionMessage;
import com.razorpay.ffs.cas.acs.hsm.luna.service.HSMGatewayCorrelationStrategy;

@Component
public class HSMGatewayCorrelationStrategyImpl implements HSMGatewayCorrelationStrategy {

    @CorrelationStrategy
    @Override
    public Object getCorrelationKey(Message<?> message) {
        if (message instanceof HSMTransactionMessage) {
            return ((HSMTransactionMessage) message).getMsgSerialNo();
        } else {
            return ((HSMTransactionMessage) message).getMsgSerialNo();
        }
    }
}
