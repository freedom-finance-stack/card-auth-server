package org.freedomfinancestack.razorpay.cas.admin.module;

import org.freedomfinancestack.razorpay.cas.admin.module.config.ConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.undertow.conduits.GzipStreamSourceConduit;
import io.undertow.server.handlers.encoding.RequestEncodingHandler;
import lombok.RequiredArgsConstructor;

@Primary
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppModule {

    private final ConfigProvider configProvider;

    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory() {
        UndertowServletWebServerFactory undertowServletWebServerFactory =
                new UndertowServletWebServerFactory();
        undertowServletWebServerFactory.addDeploymentInfoCustomizers(
                (deploymentInfo) -> {
                    deploymentInfo.addInitialHandlerChainWrapper(
                            handler ->
                                    new RequestEncodingHandler(handler)
                                            .addEncoding("gzip", GzipStreamSourceConduit.WRAPPER));
                });
        undertowServletWebServerFactory.setPort(configProvider.getServerPort());
        return undertowServletWebServerFactory;
    }
}
