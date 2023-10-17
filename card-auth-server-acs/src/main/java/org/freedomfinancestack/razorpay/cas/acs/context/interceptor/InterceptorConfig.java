package org.freedomfinancestack.razorpay.cas.acs.context.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration("interceptorConfig")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InterceptorConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            log.info("addInterceptors() Adding registries");
            registry.addInterceptor(requestInterceptor).order(1);
            WebMvcConfigurer.super.addInterceptors(registry);
        } catch (Exception exception) {
            log.error(
                    "InterceptorConfiguration.addInterceptors Error While adding interceptors",
                    exception);
        }
    }
}
