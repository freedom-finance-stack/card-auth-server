package org.freedomfinancestack.razorpay.cas.admin.module.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * The {@code AppConfiguration} class is a Spring component responsible for configuring the ACS
 * (Access Control Server) Admin Server module. It sets up the application properties such as port,
 * hostname, and Admin Server specific properties.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author ankitchoudhary2209
 */
@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfiguration {
    private int port;
    private String hostname;
}
