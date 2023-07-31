package org.ffs.razorpay.cas.acs.hsm.luna.service.impl;

import org.ffs.razorpay.cas.acs.constant.LunaHSMConstants;
import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;
import org.ffs.razorpay.cas.acs.exception.threeds.HSMConnectionException;
import org.ffs.razorpay.cas.acs.hsm.luna.domain.GatewayHSM;
import org.ffs.razorpay.cas.acs.hsm.luna.domain.HSMTransactionMessage;
import org.ffs.razorpay.cas.acs.hsm.luna.service.HSMGatewayAsyncReply;
import org.ffs.razorpay.cas.acs.hsm.luna.service.HSMGatewayService;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
                LunaHSMConstants.setHSMEchoTime();
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
