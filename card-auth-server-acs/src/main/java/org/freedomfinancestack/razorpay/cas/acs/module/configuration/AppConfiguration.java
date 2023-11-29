package org.freedomfinancestack.razorpay.cas.acs.module.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * The {@code AppConfiguration} class is a Spring component responsible for configuring the ACS
 * (Access Control Server) module. It sets up the application properties such as port, hostname, and
 * ACS specific properties like reference number and operator IDs.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfiguration {
    private int port;
    private String hostname;
    private AcsProperties acs;
    private JavaProperties java;

    @Getter
    @Setter
    public static class JavaProperties {
        private String home;
        private String cacerts;
    }

    @Getter
    @Setter
    public static class AcsProperties {
        private String referenceNumber;
        private OperatorIdProperties operatorId;
        private TimeoutConfig timeout;

        @Getter
        @Setter
        public static class OperatorIdProperties {
            private String visa;
            private String mastercard;
            private String amex;
        }

        @Getter
        @Setter
        public static class TimeoutConfig {
            private int challengeRequest;
            private int decoupledChallengeCompletion;
            private int challengeCompletion;
            private int challengeValidation;
        }
    }
}
