package org.freedomfinancestack.extensions.hsm.luna.config;

import org.freedomfinancestack.extensions.hsm.command.enums.HSMCommandType;
import org.freedomfinancestack.extensions.hsm.luna.gateway.HSMGateway;
import org.freedomfinancestack.extensions.hsm.luna.message.HSMByteArraySerializer;
import org.freedomfinancestack.extensions.hsm.luna.utils.LunaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpMessageMapper;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.TaskScheduler;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@Configuration
@ConditionalOnProperty(
        name = "hsm.enabled_gateway",
        havingValue = HSMCommandType.HSMCommandTypeConstants.LUNA_HSM)
public class HSMGatewayConfig {

    @Value("${hsm.gateway.luna.ip}")
    private String lunaHSMIP;

    @Value("${hsm.gateway.luna.port}")
    private int lunaHSMPort;

    private final TaskScheduler hsmTaskScheduler;

    private final MessageChannel hsmReceiveChannel;

    @Autowired
    public HSMGatewayConfig(
            @NonNull final @Qualifier("hsmTaskScheduler") TaskScheduler hsmTaskScheduler,
            @NonNull final @Qualifier("hsmReceive") MessageChannel hsmReceiveChannel) {
        this.hsmTaskScheduler = hsmTaskScheduler;
        this.hsmReceiveChannel = hsmReceiveChannel;
    }

    @Bean("hsmGateway")
    public HSMGateway hsmGateway() {

        try {

            // Create object of TcpNetClientConnectionFactory for HSM connection
            AbstractClientConnectionFactory clientConnectionFactory =
                    hsmClientCF(lunaHSMIP, lunaHSMPort);

            // Create TcpSendingMessageHandler for the Connection
            TcpSendingMessageHandler outHandler = hsmTcpOutGateway(clientConnectionFactory);

            // Create TcpReceivingChannelAdapter object for the Connection
            // and assign it to receive channel
            TcpReceivingChannelAdapter inHandler = hsmTcpInGateway(clientConnectionFactory);
            // inHandler.setConnectionFactory(clientConnectionFactory);

            // Generate the GatewayHSM object
            return new HSMGateway(clientConnectionFactory, outHandler, inHandler);

        } catch (Exception e) {
            log.error("gatewayHSM() Error Occurred while creating hsm gateway: ", e);
        }

        return null;
    }

    private AbstractClientConnectionFactory hsmClientCF(String ip, Integer port) {

        TcpNetClientConnectionFactory clientConnectionFactory =
                new TcpNetClientConnectionFactory(ip, port);
        clientConnectionFactory.setSingleUse(false);
        clientConnectionFactory.setSerializer(new HSMByteArraySerializer());
        clientConnectionFactory.setDeserializer(new HSMByteArraySerializer());

        clientConnectionFactory.setSoKeepAlive(true);
        TcpMessageMapper tcpMessageMapper = new TcpMessageMapper();
        clientConnectionFactory.setMapper(tcpMessageMapper);
        clientConnectionFactory.setBeanName(LunaConstants.LUNA_EFT_HSM);
        clientConnectionFactory.afterPropertiesSet();
        clientConnectionFactory.start();

        return clientConnectionFactory;
    }

    // This function will create new TcpSendingMessageHandler object for
    // connection factory
    // This MessageHAndler object will be used for sending request to server
    private TcpSendingMessageHandler hsmTcpOutGateway(
            AbstractClientConnectionFactory connectionFactory) {

        TcpSendingMessageHandler messageHandler = new TcpSendingMessageHandler();
        messageHandler.setConnectionFactory(connectionFactory);
        messageHandler.setClientMode(false);
        messageHandler.setTaskScheduler(hsmTaskScheduler);
        messageHandler.afterPropertiesSet();
        messageHandler.start();

        return messageHandler;
    }

    private TcpReceivingChannelAdapter hsmTcpInGateway(
            AbstractClientConnectionFactory connectionFactory) {
        TcpReceivingChannelAdapter messageHandler = new TcpReceivingChannelAdapter();
        messageHandler.setConnectionFactory(connectionFactory);

        messageHandler.setClientMode(true);
        messageHandler.setOutputChannel(hsmReceiveChannel);
        messageHandler.setAutoStartup(true);
        messageHandler.setTaskScheduler(hsmTaskScheduler);
        messageHandler.afterPropertiesSet();
        messageHandler.start();
        return messageHandler;
    }
}
