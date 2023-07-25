package com.razorpay.ffs.cas.acs.hsm.luna.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.exception.HSMConnectionException;
import com.razorpay.ffs.cas.acs.exception.InternalErrorCode;
import com.razorpay.ffs.cas.acs.hsm.luna.domain.GatewayHSM;
import com.razorpay.ffs.cas.acs.hsm.luna.domain.HSMTransactionMessage;
import com.razorpay.ffs.cas.acs.hsm.luna.service.HSMGatewayAsyncReply;
import com.razorpay.ffs.cas.acs.hsm.luna.service.HSMGatewayService;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.razorpay.ffs.cas.acs.constant.LunaHSMConstants.setHSMEchoTime;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HSMGatewayServiceImpl implements HSMGatewayService<GatewayHSM, HSMTransactionMessage> {

    private final HSMGatewayAsyncReply<Object, Message<?>> hsmgatewayAsyncReply;

    @Override
    public void sendRequest(GatewayHSM handler, HSMTransactionMessage message)
            throws HSMConnectionException {

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
            throw new HSMConnectionException(
                    ThreeDSecureErrorCode.SYSTEM_CONNECTION_FAILURE,
                    InternalErrorCode.HSM_CONNECTOR_CONNECTION_CLOSE,
                    InternalErrorCode.HSM_CONNECTOR_CONNECTION_CLOSE.getDefaultErrorMessage());
        }
    }

    @Override
    public byte[] getResponse(Object correlationKey) throws HSMConnectionException {

        String str;
        byte byteArray[] = null;

        Message<?> message = hsmgatewayAsyncReply.get(correlationKey);
        if (null != message) {
            byteArray = (byte[]) message.getPayload();
        } else {
            log.error("Exception in receiving transaction ");
            throw new HSMConnectionException(
                    ThreeDSecureErrorCode.SYSTEM_CONNECTION_FAILURE,
                    InternalErrorCode.HSM_CONNECTOR_REQUEST_TIMEOUT,
                    InternalErrorCode.HSM_CONNECTOR_REQUEST_TIMEOUT.getDefaultErrorMessage());
        }
        return byteArray;
    }
}
