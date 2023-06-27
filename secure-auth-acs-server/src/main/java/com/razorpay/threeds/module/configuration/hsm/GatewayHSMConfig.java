package com.razorpay.threeds.module.configuration.hsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
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
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.threeds.hsm.luna.domain.GatewayHSM;
import com.razorpay.threeds.hsm.luna.domain.HSMByteArraySerializer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.razorpay.threeds.constant.LunaHSMConstants.LUNA_HSM;
import static com.razorpay.threeds.hsm.luna.service.impl.HSMGatewayFacade.LUNA_EFT_HSM;

@Configuration
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConfigurationProperties(prefix = "hsm.gateway.luna")
@Getter
@Setter
public class GatewayHSMConfig {

  private String ip;
  private int port;

  private final Environment environment;

  // Creates a connection factory for LUNA HSM
  @Bean
  @Transactional
  public GatewayHSM gatewayHSM() {

    GatewayHSM gatewayHSM = new GatewayHSM();

    if (LUNA_HSM.equals(environment.getProperty("hsm.enabled_gateway"))) {
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
    clientConnectionFactory.setBeanName(LUNA_EFT_HSM);
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
