package com.razorpay.ffs.cas.acs.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {"com.razorpay.ffs.cas.acs", "com.razorpay.ffs.cas.dao"},
        exclude = {JacksonAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"com.razorpay.ffs.cas.dao.repository"})
@EntityScan(basePackages = {"com.razorpay.ffs.cas.dao.model"})
@EnableConfigurationProperties
public class AcsAppServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(AcsAppServer.class, args);
    }
}
