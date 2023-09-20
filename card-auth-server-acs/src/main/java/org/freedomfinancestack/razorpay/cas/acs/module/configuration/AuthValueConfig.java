package org.freedomfinancestack.razorpay.cas.acs.module.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * The {@code AuthValueConfig} class is a Spring configuration class responsible for configuring the
 * keys and variables used by the ACS (Access Control Server) module.. It stores the data which is
 * fetched from environment variables
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author ishanAgrawal
 */
@Configuration
@ConfigurationProperties(prefix = "auth-value")
@Getter
@Setter
public class AuthValueConfig {
    private String masterCardAcsKey;
}
