package org.freedomfinancestack.razorpay.cas.dao.encryption;

import org.freedomfinancestack.extensions.crypto.AES256EncryptionConfig;
import org.freedomfinancestack.extensions.crypto.AES256EncryptionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties(prefix = "encryption.aes")
@Slf4j
@Getter
@Setter
public class EncryptionConfig {

    private String password;
    private String salt;

    @Bean(name = "aes256Encryption")
    AES256EncryptionUtils aes256Encryption() {
        return new AES256EncryptionUtils(new AES256EncryptionConfig(password, salt));
    }
}
