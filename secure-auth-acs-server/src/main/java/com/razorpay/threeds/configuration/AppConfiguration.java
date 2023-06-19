package com.razorpay.threeds.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
@Setter
public class AppConfiguration {
    private AppProperties app;

    @Getter
    @Setter
    public static class AppProperties {
        private int port;
        private String hostName;
        private AcsProperties acs;
    }

    @Getter
    @Setter
    public static class AcsProperties {
        private String referenceNumber;
        private OperatorId operatorId;
    }

    @Getter
    @Setter
    public static class OperatorId {
        private String visa;
        private String mastercard;
        private String amex;
    }



}


