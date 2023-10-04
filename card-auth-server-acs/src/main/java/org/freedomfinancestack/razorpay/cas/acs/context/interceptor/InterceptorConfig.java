package org.freedomfinancestack.razorpay.cas.acs.context.interceptor;

import java.util.Optional;

import org.freedomfinancestack.extensions.externallibs.security.SecurityModuleAWS;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration("interceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer {

    private Optional<SecurityModuleAWS> securityModuleAWSLib;

    /** Since, SecurityModuleAWS is a bean, this can be used across this whole project. */
    @Bean
    @ConditionalOnProperty(
            name = "external-libs.security.SecurityModuleAWS.enabled",
            havingValue = "true")
    protected Optional<SecurityModuleAWS> securityModuleAWSLib() {
        securityModuleAWSLib = Optional.of(new SecurityModuleAWS());
        return securityModuleAWSLib;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            log.info("addInterceptors() Adding registries");
            registry.addInterceptor(new RequestInterceptor(securityModuleAWSLib)).order(1);
            WebMvcConfigurer.super.addInterceptors(registry);
        } catch (Exception exception) {
            log.error(
                    "InterceptorConfiguration.addInterceptors Error While adding interceptors",
                    exception);
        }
    }
}
