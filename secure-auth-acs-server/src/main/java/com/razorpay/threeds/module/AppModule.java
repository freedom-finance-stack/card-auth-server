package com.razorpay.threeds.module;

import com.razorpay.threeds.configuration.AppConfiguration;
import io.undertow.conduits.GzipStreamSourceConduit;
import io.undertow.server.handlers.encoding.RequestEncodingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppModule {

    private final AppConfiguration appConfiguration;
    final com.razorpay.threeds.configuration.SwaggerConfiguration SwaggerConfiguration;
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
        undertowServletWebServerFactory.setPort(appConfiguration.getApp().getPort());
        return undertowServletWebServerFactory;
    }
}
