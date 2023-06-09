package com.razorpay.threeds.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfiguration {
    private int port;
    private String hostName;
}

