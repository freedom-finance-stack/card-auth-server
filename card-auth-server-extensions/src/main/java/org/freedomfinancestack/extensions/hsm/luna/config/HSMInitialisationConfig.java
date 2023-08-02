package org.freedomfinancestack.extensions.hsm.luna.config;

import org.freedomfinancestack.extensions.hsm.command.enums.HSMCommandType;
import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGatewayAsyncReply;
import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGatewayCorrelationStrategy;
import org.freedomfinancestack.extensions.hsm.luna.message.HSMBarrierHandlerWithLateGoodResponse;
import org.freedomfinancestack.extensions.hsm.luna.message.HSMTransactionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableIntegration
@IntegrationComponentScan(basePackageClasses = {HSMGatewayConfig.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(
        name = "hsm.enabled_gateway",
        havingValue = HSMCommandType.HSMCommandTypeConstants.LUNA_HSM)
public class HSMInitialisationConfig {
    private final HSMGatewayAsyncReply<Object, Message<?>> hsmgatewayAsyncReply;

    private final HSMGatewayCorrelationStrategy hsmCorrelationStrategy;

    @Value("${hsm.gateway.luna.timeout}")
    private long timeout;

    @MessagingGateway(defaultRequestChannel = "hsmEnd")
    public interface HSMMessagingGateway {
        void waitForResponse(HSMTransactionMessage transaction);
    }

    @Bean
    public MessageChannel hsmEnd() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "hsmEnd")
    public HSMBarrierHandlerWithLateGoodResponse hsmBarrier() {
        long hsmTimeout = timeout * 1000;
        HSMBarrierHandlerWithLateGoodResponse hsmBarrier =
                new HSMBarrierHandlerWithLateGoodResponse(hsmTimeout, this.hsmCorrelationStrategy);
        hsmBarrier.setAsync(true);
        hsmBarrier.setOutputChannel(hsmOut());
        hsmBarrier.setDiscardChannel(hsmLateGoodResponseChannel());
        return hsmBarrier;
    }

    @Bean
    public MessageChannel hsmProcess() {
        return new QueueChannel();
    }

    @Bean
    public MessageChannel hsmOut() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "hsmOut")
    public void printMessage(Message<?> message) {}

    @Transformer(inputChannel = "hsmReceive", outputChannel = "hsmProcess")
    public HSMTransactionMessage convert(byte[] response) {
        HSMTransactionMessage transactionMessage = new HSMTransactionMessage();
        transactionMessage.setHSMResponse(response);
        return transactionMessage;
    }

    @ServiceActivator(inputChannel = "hsmProcess")
    @Bean
    public MessageHandler hsmReleaser() {
        return message -> {
            try {
                hsmgatewayAsyncReply.put(message);
                hsmBarrier().trigger(message);
            } catch (Exception exception) {
                log.error("Late good response..! and exception is: ", exception);
                hsmgatewayAsyncReply.get(message);
                hsmLateGoodResponseChannel().send(message);
            }
        };
    }

    @Bean
    public MessageChannel hsmLateGoodResponseChannel() {
        return new QueueChannel();
    }

    @ServiceActivator(inputChannel = "hsmLateGoodResponseChannel")
    public void handleLateGoodResponse(Message<?> message) {
        log.info("HSM Late good Response Handler...!");
    }

    @Bean(name = "hsmTaskScheduler")
    @Scope(value = "prototype")
    public TaskScheduler getHSMTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean(name = "hsmReceive")
    public MessageChannel hsmReceive() {
        return new QueueChannel();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return new PollerMetadata();
    }
}
