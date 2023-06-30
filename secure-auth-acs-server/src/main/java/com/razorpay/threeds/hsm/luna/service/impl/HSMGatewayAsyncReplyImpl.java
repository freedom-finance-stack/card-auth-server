package com.razorpay.threeds.hsm.luna.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.razorpay.threeds.hsm.luna.service.HSMGatewayAsyncReply;
import com.razorpay.threeds.hsm.luna.service.HSMGatewayCorrelationStrategy;

import lombok.RequiredArgsConstructor;

@Service("hSMGatewayAsyncReplyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HSMGatewayAsyncReplyImpl implements HSMGatewayAsyncReply<Object, Message<?>> {

  private final ConcurrentHashMap<Object, Message<?>> map = new ConcurrentHashMap<>();
  private final HSMGatewayCorrelationStrategy hsmCorrelationStrategy;

  @Override
  public Message<?> get(Object key) {
    return map.remove(key);
  }

  @Override
  public void put(Message<?> message) {
    this.map.putIfAbsent(hsmCorrelationStrategy.getCorrelationKey(message), message);
  }
}
