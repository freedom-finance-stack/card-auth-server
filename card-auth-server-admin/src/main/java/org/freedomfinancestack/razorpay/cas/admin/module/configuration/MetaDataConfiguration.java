package org.freedomfinancestack.razorpay.cas.admin.module.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "admin-metadata")
@Getter
@Setter
public class MetaDataConfiguration {
    private String[] supportedMessageVersions;

    private Short[] isoCountryCode;

    private String[] supportedTimezone;
}
