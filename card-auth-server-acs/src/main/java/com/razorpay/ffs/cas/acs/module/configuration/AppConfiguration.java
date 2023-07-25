package com.razorpay.ffs.cas.acs.module.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfiguration {
    private int port;
    private String hostname;
    private AcsProperties acs;

    @Getter
    @Setter
    public static class AcsProperties {
        private String referenceNumber;
        private OperatorIdProperties operatorId;

        @Getter
        @Setter
        public static class OperatorIdProperties {
            private String visa;
            private String mastercard;
            private String amex;
        }
    }
}
