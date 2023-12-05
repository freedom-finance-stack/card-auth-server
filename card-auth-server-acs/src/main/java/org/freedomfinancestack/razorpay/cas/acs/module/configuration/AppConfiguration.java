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
            /** Timeout value (in seconds) for getting challenge requests after Ares is sent. */
            private int challengeRequest;

            /** Timeout value (in seconds) for completing decoupled challenge authentication. */
            private int decoupledChallengeCompletion;

            /** Timeout value (in seconds) for completing challenge authentication. */
            private int challengeCompletion;

            /** Timeout value (in seconds) for UI to enter challenge task by user. */
            private int challengeValidation;

            /** Delay (in seconds) to trigger decoupled authentication after Ares is sent. */
            private int decoupledAuthDelay;
        }
    }
}
