package org.freedomfinancestack.razorpay.cas.acs.module.configuration.hsm;

import org.freedomfinancestack.razorpay.cas.acs.constant.LunaHSMConstants;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.GatewayHSM;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.HSMByteArraySerializer;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.service.impl.HSMGatewayFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpMessageMapper;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Getter
@Setter
public class GatewayHSMConfig {

    @Value("${hsm.gateway.luna.ip}")
    private String ip;

    @Value("${hsm.gateway.luna.port}")
    private int port;

    @Value("${hsm.enabled_gateway}")
    private String enabledHSM;

    // Creates a connection factory for LUNA HSM
    @Bean
    public GatewayHSM gatewayHSM() {

        GatewayHSM gatewayHSM = new GatewayHSM();

        if (LunaHSMConstants.LUNA_HSM.equals(enabledHSM)) {
            try {
                // 1. Fetch HSM IP and Port
                String strIP = ip;
                int nPort = port;

                // 2. Create object of TcpNetClientConnectionFactory for HSM connection
                AbstractClientConnectionFactory clientConnectionFactory = hsmClientCF(strIP, nPort);

                // 3. Create TcpSendingMessageHandler for the Connection
                TcpSendingMessageHandler outHandler = hsmTcpOutGateway(clientConnectionFactory);

                // 4. Create TcpReceivingChannelAdapter object for the Connection
                // and assign it to receive channel
                TcpReceivingChannelAdapter inHandler = hsmTcpInGateway(clientConnectionFactory);
                // inHandler.setConnectionFactory(clientConnectionFactory);

                // 5. Generate the GatewayHSM object
                gatewayHSM = new GatewayHSM(clientConnectionFactory, outHandler, inHandler);

            } catch (Exception e) {
                log.error("gatewayHSM() Error Occurred while creating hsm gateway: ", e);
            }
        }

        return gatewayHSM;
    }

    public AbstractClientConnectionFactory hsmClientCF(String ip, Integer port) {

        TcpNetClientConnectionFactory clientConnectionFactory =
                new TcpNetClientConnectionFactory(ip, port);
        clientConnectionFactory.setSingleUse(false);
        clientConnectionFactory.setSerializer(new HSMByteArraySerializer());
        clientConnectionFactory.setDeserializer(new HSMByteArraySerializer());

        clientConnectionFactory.setSoKeepAlive(true);
        TcpMessageMapper tcpMessageMapper = new TcpMessageMapper();
        clientConnectionFactory.setMapper(tcpMessageMapper);
        clientConnectionFactory.setBeanName(HSMGatewayFacade.LUNA_EFT_HSM);
        clientConnectionFactory.afterPropertiesSet();
        clientConnectionFactory.start();

        return clientConnectionFactory;
    }

    // This function will create new TcpSendingMessageHandler object for
    // connection factory
    // This MessageHAndler object will be used for sending request to server
    public TcpSendingMessageHandler hsmTcpOutGateway(
            AbstractClientConnectionFactory connectionFactory) {

        TcpSendingMessageHandler messageHandler = new TcpSendingMessageHandler();
        messageHandler.setConnectionFactory(connectionFactory);
        messageHandler.setClientMode(false);
        messageHandler.setTaskScheduler(getHSMTaskScheduler());
        messageHandler.afterPropertiesSet();
        messageHandler.start();

        return messageHandler;
    }

    @Bean
    @Scope(value = "prototype")
    public TaskScheduler getHSMTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    public TcpReceivingChannelAdapter hsmTcpInGateway(
            AbstractClientConnectionFactory connectionFactory) {
        TcpReceivingChannelAdapter messageHandler = new TcpReceivingChannelAdapter();
        messageHandler.setConnectionFactory(connectionFactory);

        messageHandler.setClientMode(true);
        messageHandler.setOutputChannel(hsmreceive());
        messageHandler.setAutoStartup(true);
        messageHandler.setTaskScheduler(getHSMTaskScheduler());
        messageHandler.afterPropertiesSet();
        messageHandler.start();
        return messageHandler;
    }

    @Bean
    public MessageChannel hsmreceive() {
        return new QueueChannel();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return new PollerMetadata();
    }
}
