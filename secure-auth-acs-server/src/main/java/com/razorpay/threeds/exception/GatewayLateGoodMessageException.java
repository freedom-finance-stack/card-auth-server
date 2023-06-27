package com.razorpay.threeds.exception;

import org.springframework.messaging.Message;

public class GatewayLateGoodMessageException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private Message<?> lateGoodMessage;

  public GatewayLateGoodMessageException(Message<?> message, String description) {
    super(description);
    this.lateGoodMessage = message;
  }
}
