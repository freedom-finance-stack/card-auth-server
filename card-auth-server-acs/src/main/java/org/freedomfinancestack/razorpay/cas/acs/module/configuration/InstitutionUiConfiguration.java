package org.freedomfinancestack.razorpay.cas.acs.module.configuration;

import java.util.EnumMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "institution-ui")
@Getter
@Setter
@RequiredArgsConstructor
public class InstitutionUiConfiguration {
    private final ApplicationContext applicationContext;

    private String institutionUrl;
    private String mediumLogo;
    private String highLogo;
    private String extraHighLogo;
    private String institutionCssUrl;
    private String htmlOtpTemplate;
    private Map<Network, UiConfig> networkUiConfig = new EnumMap<>(Network.class);

    @Getter
    @Setter
    public static class UiConfig {
        String mediumPs;
        String highPs;
        String extraHighPs;
    }
}
