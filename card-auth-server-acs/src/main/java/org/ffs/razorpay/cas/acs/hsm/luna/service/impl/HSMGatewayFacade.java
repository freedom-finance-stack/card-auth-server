package org.ffs.razorpay.cas.acs.hsm.luna.service.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.ffs.razorpay.cas.acs.exception.HSMConnectionException;
import org.ffs.razorpay.cas.acs.hsm.luna.command.HsmCommand;
import org.ffs.razorpay.cas.acs.hsm.luna.domain.GatewayHSM;
import org.ffs.razorpay.cas.acs.hsm.luna.domain.HSMTransactionMessage;
import org.ffs.razorpay.cas.acs.hsm.luna.service.HSMGatewayCorrelationStrategy;
import org.ffs.razorpay.cas.acs.hsm.luna.service.HSMGatewayService;
import org.ffs.razorpay.cas.acs.module.configuration.hsm.HSMInitialisationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("hsmGatewayFacade")
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HSMGatewayFacade {
    private static final AtomicInteger hsmCounter = new AtomicInteger(251);

    public static final int HSM_HEADER_LENGTH = 4;

    public static final String LUNA_EFT_HSM = "LunaEFT";

    private final HSMInitialisationConfig.HSMGateway hsmGateway;

    private final HSMGatewayService<GatewayHSM, HSMTransactionMessage> hsmGatewayService;

    private final HSMGatewayCorrelationStrategy hsmGatewayCorrelationStrategy;

    private final GatewayHSM gatewayHSM;

    public HsmCommand sendMessage(HsmCommand hsmCommand) throws HSMConnectionException {

        byte[] hsmRequest = null;
        byte[] hsmResponse = null;
        HSMTransactionMessage hsmTranasctionMessage = null;
        int hsmCmdLength;

        hsmRequest = hsmCommand.serialize();

        // Step 2 - Get HSM Request Counter
        Integer hsmCounter = getHSMNextCounter();

        // Step 3 - Get HSM Command Length
        hsmCmdLength = hsmCommand.getCmdLength();

        // Step 4 - Create TransactionMessage Object with HSMRequest and Counter
        hsmTranasctionMessage =
                new HSMTransactionMessage(
                        hsmRequest, hsmCmdLength, hsmCounter, LUNA_EFT_HSM, HSM_HEADER_LENGTH);

        // Step 6 - Send HSMCommand Request
        hsmGatewayService.sendRequest(gatewayHSM, hsmTranasctionMessage);

        hsmGateway.waitForResponse(hsmTranasctionMessage);

        // Step 7 - Fetch HSM Response byte[]
        hsmResponse =
                hsmGatewayService.getResponse(
                        hsmGatewayCorrelationStrategy.getCorrelationKey(hsmTranasctionMessage));

        // Step 8 - Remove Header and set the Response to HSMCommand
        byte[] bytarrMsg = new byte[hsmResponse.length - 6];
        System.arraycopy(hsmResponse, 6, bytarrMsg, 0, bytarrMsg.length);

        hsmCommand.processResponse(bytarrMsg);

        return hsmCommand;
    }

    public static Integer getHSMNextCounter() {
        if (hsmCounter.get() == 254) {
            hsmCounter.set(0);
        }
        return hsmCounter.incrementAndGet();
    }
}
