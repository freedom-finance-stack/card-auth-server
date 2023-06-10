package com.razorpay.threeds.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(
        scanBasePackages = {"com.razorpay.threeds","com.razorpay.acs.dao"})
//        exclude = {JacksonAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"com.razorpay.acs.dao.repository"})
@EntityScan(basePackages = {"com.razorpay.acs.dao.model"})
@EnableConfigurationProperties
public class AppServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(AppServer.class, args);
    }
}
