package org.freedomfinancestack.razorpay.cas.acs.module.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * The {@code TestConfigProperties}
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Component
@ConfigurationProperties(prefix = "test")
@Getter
@Setter
public class TestConfigProperties {

    private boolean enable;
    private AttemptedRange attemptedRange;
    private boolean enableDecryptionEncryption;

    @Getter
    @Setter
    public static class AttemptedRange {
        private long start;
        private long end;
    }
}
