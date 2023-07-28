package org.ffs.razorpay.cas.acs.module.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.undertow.conduits.GzipStreamSourceConduit;
import io.undertow.server.handlers.encoding.RequestEncodingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Primary
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AppModule {

    private final AppConfiguration appConfiguration;

    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory() {
        log.info(
                "AppConfiguration ReferenceNumber: "
                        + appConfiguration.getAcs().getReferenceNumber());
        UndertowServletWebServerFactory undertowServletWebServerFactory =
                new UndertowServletWebServerFactory();
        undertowServletWebServerFactory.addDeploymentInfoCustomizers(
                (deploymentInfo) -> {
                    deploymentInfo.addInitialHandlerChainWrapper(
                            handler ->
                                    new RequestEncodingHandler(handler)
                                            .addEncoding("gzip", GzipStreamSourceConduit.WRAPPER));
                });
        undertowServletWebServerFactory.setPort(appConfiguration.getPort());
        return undertowServletWebServerFactory;
    }
}
