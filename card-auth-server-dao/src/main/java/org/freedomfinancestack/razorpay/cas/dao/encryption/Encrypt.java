package org.freedomfinancestack.razorpay.cas.dao.encryption;

import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;

@Component
public class Encrypt implements AttributeConverter<SecureWrapper, String> {

    @Override
    public String convertToDatabaseColumn(SecureWrapper s) {
        return s.getEncrypted();
    }

    @Override
    public SecureWrapper convertToEntityAttribute(String s) {
        return SecureWrapper.parseEncrypted(s);
    }
}
