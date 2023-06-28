package com.razorpay.threeds.hsm.luna.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.razorpay.threeds.exception.InternalErrorCode;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.hsm.luna.domain.GatewayHSM;
import com.razorpay.threeds.hsm.luna.domain.HSMTransactionMessage;
import com.razorpay.threeds.hsm.luna.service.HSMGatewayAsyncReply;
import com.razorpay.threeds.hsm.luna.service.HSMGatewayService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.razorpay.threeds.constant.LunaHSMConstants.setHSMEchoTime;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HSMGatewayServiceImpl implements HSMGatewayService<GatewayHSM, HSMTransactionMessage> {

  private final HSMGatewayAsyncReply<Object, Message<?>> hsmgatewayAsyncReply;

  @Override
  public void sendRequest(GatewayHSM handler, HSMTransactionMessage message) throws ACSException {

    try {

      TcpSendingMessageHandler sendHandler = handler.getTcpSendingMessageHandler();

      if (null != sendHandler) {
        sendHandler.handleMessage(message);
        setHSMEchoTime();
      } else {
        log.debug("Send Message Handler is Null");
      }

    } catch (Exception e) {
      log.error("Exception in sending transaction to HSM", e);
      throw new ACSException(InternalErrorCode.HSM_CONNECTOR_CONNECTION_CLOSE);
    }
  }

  @Override
  public byte[] getResponse(Object correlationKey) throws ACSException {

    String str;
    byte byteArray[] = null;

    try {

      Message<?> message = hsmgatewayAsyncReply.get(correlationKey);
      if (null != message) {
        byteArray = (byte[]) message.getPayload();
      } else {
        log.error("Exception in receiving transaction ");
        throw new ACSException(InternalErrorCode.HSM_CONNECTOR_REQUEST_TIMEOUT);
      }

    } catch (ACSException e) {
      throw new ACSException(InternalErrorCode.HSM_CONNECTOR_REQUEST_TIMEOUT);
    }
    return byteArray;
  }
}
