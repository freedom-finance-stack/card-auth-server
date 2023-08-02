package org.freedomfinancestack.razorpay.cas.admin.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(
        scanBasePackages = {"org.freedomfinancestack.razorpay.cas.admin"},
        exclude = {JacksonAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class AdminAppServer {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(AdminAppServer.class, args);
    }
}
