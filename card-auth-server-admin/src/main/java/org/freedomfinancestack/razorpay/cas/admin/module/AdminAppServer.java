package org.freedomfinancestack.razorpay.cas.admin.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
            "org.freedomfinancestack.razorpay.cas.admin",
            "org.freedomfinancestack.razorpay.cas.dao",
            "org.freedomfinancestack.extensions"
        },
        exclude = {JacksonAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"org.freedomfinancestack.razorpay.cas.dao.repository"})
@EntityScan(basePackages = {"org.freedomfinancestack.razorpay.cas.dao.model"})
@EnableConfigurationProperties
public class AdminAppServer {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(AdminAppServer.class, args);
    }
}
