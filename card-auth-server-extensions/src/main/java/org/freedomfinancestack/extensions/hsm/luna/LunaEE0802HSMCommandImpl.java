package org.freedomfinancestack.extensions.hsm.luna;

import java.util.concurrent.atomic.AtomicInteger;

import org.freedomfinancestack.extensions.hsm.command.HSMCommand;
import org.freedomfinancestack.extensions.hsm.command.enums.HSMCommandType;
import org.freedomfinancestack.extensions.hsm.luna.config.HSMInitialisationConfig;
import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGateway;
import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGatewayCorrelationStrategy;
import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGatewayService;
import org.freedomfinancestack.extensions.hsm.luna.message.HSMTransactionMessage;
import org.freedomfinancestack.extensions.hsm.luna.utils.HexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.hsm.luna.utils.LunaConstants.EE0802_CMD;
import static org.freedomfinancestack.extensions.hsm.luna.utils.LunaConstants.FUNC_MODIFIER_00;
import static org.freedomfinancestack.extensions.hsm.luna.utils.LunaConstants.HSM_HEADER_LENGTH;
import static org.freedomfinancestack.extensions.hsm.luna.utils.LunaConstants.HSM_SUCCESSFUL_RESPONSE;
import static org.freedomfinancestack.extensions.hsm.luna.utils.LunaConstants.LUNA_EFT_HSM;

@Slf4j
@Service(HSMCommandType.HSMCommandTypeConstants.LUNA_HSM)
@ConditionalOnProperty(
        name = "hsm.enabled_gateway",
        havingValue = HSMCommandType.HSMCommandTypeConstants.LUNA_HSM)
public class LunaEE0802HSMCommandImpl extends HSMCommand {

    private static final AtomicInteger hsmCounter = new AtomicInteger(251);
    private final HSMGateway hsmGateway;

    private final HSMGatewayService<HSMGateway, HSMTransactionMessage> hsmGatewayService;

    private final HSMGatewayCorrelationStrategy hsmGatewayCorrelationStrategy;

    private final HSMInitialisationConfig.HSMMessagingGateway hsmMessagingGateway;

    @Autowired
    public LunaEE0802HSMCommandImpl(
            @NonNull final @Qualifier("hsmGateway") HSMGateway hsmGateway,
            @NonNull final HSMGatewayService<HSMGateway, HSMTransactionMessage> hsmGatewayService,
            @NonNull final HSMGatewayCorrelationStrategy hsmGatewayCorrelationStrategy,
            @NonNull final HSMInitialisationConfig.HSMMessagingGateway hsmMessagingGateway) {
        this.hsmGateway = hsmGateway;
        this.hsmGatewayService = hsmGatewayService;
        this.hsmGatewayCorrelationStrategy = hsmGatewayCorrelationStrategy;
        this.hsmMessagingGateway = hsmMessagingGateway;
    }

    @Override
    public void initialize() {
        String cvvIndex = hsmMessage.getKcv();
        int index = Integer.parseInt(cvvIndex);
        if (index < 99) {
            if (index > 9) {
                hsmMessage.setKcv("0200" + index);
            } else {
                hsmMessage.setKcv("02000" + index);
            }
        }
        int len = hsmMessage.getData().length();
        hsmMessage.setData(hsmMessage.getData() + "0".repeat(Math.max(0, 32 - len)));
    }

    @Override
    public byte[] serialize() {
        String cmdBuffer;
        cmdBuffer = EE0802_CMD + FUNC_MODIFIER_00;
        cmdBuffer = cmdBuffer + hsmMessage.getKcv() + hsmMessage.getData();

        return HexUtil.hexStringToByteArray(cmdBuffer);
    }

    @Override
    public byte[] sendRequest(byte[] requestMessage) throws Exception {
        // Create HSMTransactionMessage from requestMessage
        HSMTransactionMessage hsmTransactionMessage = createHSMTransactionMessage(requestMessage);

        // Send Request to Luna HSM
        hsmGatewayService.sendRequest(hsmGateway, hsmTransactionMessage);

        // Wait for response from Luna HSM
        hsmMessagingGateway.waitForResponse(hsmTransactionMessage);

        // Fetch response from Luna HSM in byte[]
        Object correlationKey =
                hsmGatewayCorrelationStrategy.getCorrelationKey(hsmTransactionMessage);

        return hsmGatewayService.fetchResponse(correlationKey);
    }

    @Override
    public void processResponse(byte[] responseMessage) {
        // Remove Header from Luna HSM Response
        byte[] responseMessageBytes = new byte[responseMessage.length - 6];
        System.arraycopy(responseMessage, 6, responseMessageBytes, 0, responseMessageBytes.length);

        String outputData = HexUtil.hexValue(responseMessageBytes, 0, responseMessageBytes.length);
        String hsmResponse = null;
        try {
            String responseCode = outputData.substring(6, 8);
            if (responseCode.equals(HSM_SUCCESSFUL_RESPONSE)) {
                // todo - check if the HSM Response retrieved is correct.
                hsmResponse = outputData.substring(8);
                hsmMessage.setHsmResponse(hsmResponse);
                log.debug("processResponse() Cvv generation is successful");
            } else {
                hsmMessage.setHsmResponse(hsmResponse);
                log.error(
                        "processResponse() Cvv generation is failed with response code: {} ",
                        responseCode);
            }
        } catch (Exception exp) {
            hsmMessage.setHsmResponse(hsmResponse);
            log.error(
                    "processResponse() Cvv generation is failed with exception "
                            + HexUtil.getStackTrace(exp));
        }
    }

    private HSMTransactionMessage createHSMTransactionMessage(byte[] requestMessage) {
        return new HSMTransactionMessage(
                requestMessage,
                requestMessage.length,
                getHSMNextCounter(),
                LUNA_EFT_HSM,
                HSM_HEADER_LENGTH);
    }

    private static Integer getHSMNextCounter() {
        if (hsmCounter.get() == 254) {
            hsmCounter.set(0);
        }
        return hsmCounter.incrementAndGet();
    }
}
