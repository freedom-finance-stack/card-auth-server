package org.freedomfinancestack.razorpay.cas.dao.encryption;

import org.freedomfinancestack.extensions.crypto.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;

@Component
public class Encrypt implements AttributeConverter<String, String> {
    private final EncryptionUtils encryptionUtil;

    @Autowired
    public Encrypt(@Qualifier("aes256Encryption") EncryptionUtils encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        return encryptionUtil.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return encryptionUtil.decrypt(s);
    }
}
