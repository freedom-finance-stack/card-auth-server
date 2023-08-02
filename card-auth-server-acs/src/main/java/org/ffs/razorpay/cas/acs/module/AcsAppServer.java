package org.ffs.razorpay.cas.acs.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
            "org.ffs.razorpay.cas.acs",
            "org.ffs.razorpay.cas.dao",
            "org.freedomfinancestack.extensions"
        },
        exclude = {JacksonAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"org.ffs.razorpay.cas.dao.repository"})
@EntityScan(basePackages = {"org.ffs.razorpay.cas.dao.model"})
@EnableConfigurationProperties
public class AcsAppServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(AcsAppServer.class, args);
    }
}
