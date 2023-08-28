package org.freedomfinancestack.razorpay.cas.acs.context.interceptor;

import org.freedomfinancestack.extensions.hsm.command.enums.HSMCommandType;
import org.freedomfinancestack.razorpay.cas.acs.module.custom.SecurityModuleAWS;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration("interceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer {

    private SecurityModuleAWS securityModuleAWS;

    @Bean
    @ConditionalOnProperty(
            name = "hsm.enabled_gateway",
            havingValue = HSMCommandType.HSMCommandTypeConstants.NO_OP_HSM)
    protected SecurityModuleAWS securityModuleAWS() {
        securityModuleAWS = new SecurityModuleAWS();
        return securityModuleAWS;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            log.info("addInterceptors() Adding registries");
            registry.addInterceptor(new RequestInterceptor(securityModuleAWS)).order(1);
            WebMvcConfigurer.super.addInterceptors(registry);
        } catch (Exception exception) {
            log.error(
                    "InterceptorConfiguration.addInterceptors Error While adding interceptors",
                    exception);
        }
    }
}
