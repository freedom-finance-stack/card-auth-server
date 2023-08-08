package org.freedomfinancestack.extensions.hsm.luna.gateway.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGatewayAsyncReply;
import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGatewayCorrelationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service("hsmGatewayAsyncReplyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HSMGatewayAsyncReplyImpl implements HSMGatewayAsyncReply<Object, Message<?>> {

    private final ConcurrentHashMap<Object, Message<?>> concurrentHashMap =
            new ConcurrentHashMap<>();
    private final HSMGatewayCorrelationStrategy hsmCorrelationStrategy;

    @Override
    public Message<?> get(Object key) {
        return concurrentHashMap.remove(key);
    }

    @Override
    public void put(Message<?> message) {
        concurrentHashMap.putIfAbsent(hsmCorrelationStrategy.getCorrelationKey(message), message);
    }
}
