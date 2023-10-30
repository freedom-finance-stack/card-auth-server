package org.freedomfinancestack.razorpay.cas.acs.module.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.Getter;

@Configuration
@ConfigurationProperties(prefix = "otp")
@Data
public class OtpCommunicationConfiguration {
    private SmsProperties sms;
    private EmailProperties email;

    @Getter
    public static class SmsProperties {
        private String content;
    }

    @Getter
    public static class EmailProperties {
        private String from;
        private String templateName;
        private String subjectText;
    }
}