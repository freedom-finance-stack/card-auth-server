package com.razorpay.threeds.module;

import com.razorpay.threeds.configuration.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(
        scanBasePackages = {"com.razorpay.threeds"},
        exclude = {JacksonAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class AppServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(AppServer.class, args);
    }
}
