package org.freedomfinancestack.razorpay.cas.acs.module.configuration.hsm;

import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.HSMBarrierMessageHandlerWithLateGoodResponse;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.HSMTransactionMessage;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.service.HSMGatewayAsyncReply;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.service.HSMGatewayCorrelationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableIntegration
@Configuration
@Slf4j
@IntegrationComponentScan(basePackageClasses = {GatewayHSMConfig.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HSMInitialisationConfig {
    private final HSMGatewayAsyncReply<Object, Message<?>> hsmgatewayAsyncReply;

    private final HSMGatewayCorrelationStrategy hsmCorrelationStrategy;

    @Value("${hsm.gateway.luna.timeout}")
    private long timeout;

    @MessagingGateway(defaultRequestChannel = "hsmsend")
    public interface HSMGateway {
        void waitForResponse(HSMTransactionMessage transaction);
    }

    @Bean
    public MessageChannel hsmsend() {
        DirectChannel channel = new DirectChannel();
        return channel;
    }

    @Bean
    @ServiceActivator(inputChannel = "hsmsend")
    public HSMBarrierMessageHandlerWithLateGoodResponse hsmbarrier() {
        long hsmTimeout = timeout * 1000;
        HSMBarrierMessageHandlerWithLateGoodResponse hsmbarrier =
                new HSMBarrierMessageHandlerWithLateGoodResponse(
                        hsmTimeout, this.hsmCorrelationStrategy);
        hsmbarrier.setAsync(true);
        hsmbarrier.setOutputChannel(hsmout());
        hsmbarrier.setDiscardChannel(hsmLateGoodresponseChannel());
        return hsmbarrier;
    }

    @Bean
    public MessageChannel hsmprocess() {
        QueueChannel channel = new QueueChannel();
        return channel;
    }

    @Bean
    public MessageChannel hsmout() {
        DirectChannel channel = new DirectChannel();
        return channel;
    }

    @ServiceActivator(inputChannel = "hsmout")
    public void printMessage(Message<?> message) {}

    @Transformer(inputChannel = "hsmreceive", outputChannel = "hsmprocess")
    public HSMTransactionMessage convert(byte[] response) {
        HSMTransactionMessage transactionMessage = new HSMTransactionMessage();
        transactionMessage.setHSMResponse(response);
        return transactionMessage;
    }

    @ServiceActivator(inputChannel = "hsmprocess")
    @Bean
    public MessageHandler hsmreleaser() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                try {
                    hsmgatewayAsyncReply.put(message);
                    hsmbarrier().trigger(message);
                } catch (Exception exception) {
                    log.error("Late good response..! and exception is: ", exception);
                    hsmgatewayAsyncReply.get(message);
                    hsmLateGoodresponseChannel().send(message);
                }
            }
        };
    }

    @Bean
    public MessageChannel hsmLateGoodresponseChannel() {
        return new QueueChannel();
    }

    @ServiceActivator(inputChannel = "hsmLateGoodresponseChannel")
    public void handleLateGoodResponse(Message<?> message) {
        log.info("HSM Late good Response Handler...!");
    }
}
