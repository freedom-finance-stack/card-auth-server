package org.freedomfinancestack.extensions.hsm.luna.gateway.impl;

import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGatewayCorrelationStrategy;
import org.freedomfinancestack.extensions.hsm.luna.message.HSMTransactionMessage;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class HSMGatewayCorrelationStrategyImpl implements HSMGatewayCorrelationStrategy {

    @CorrelationStrategy
    @Override
    public Object getCorrelationKey(Message<?> message) {
        if (message instanceof HSMTransactionMessage) {
            return ((HSMTransactionMessage) message).getMsgSerialNo();
        } else {
            return null;
        }
    }
}
