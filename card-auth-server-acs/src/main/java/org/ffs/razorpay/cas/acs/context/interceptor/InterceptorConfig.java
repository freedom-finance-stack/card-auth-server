package org.ffs.razorpay.cas.acs.context.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration("interceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            log.info("addInterceptors() Adding registries");
            registry.addInterceptor(new RequestInterceptor()).order(1);
            WebMvcConfigurer.super.addInterceptors(registry);
        } catch (Exception exception) {
            log.error(
                    "InterceptorConfiguration.addInterceptors Error While adding interceptors",
                    exception);
        }
    }
}
