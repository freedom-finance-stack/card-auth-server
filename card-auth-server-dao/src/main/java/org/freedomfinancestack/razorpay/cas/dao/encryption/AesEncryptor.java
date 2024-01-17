package org.freedomfinancestack.razorpay.cas.dao.encryption;

import org.freedomfinancestack.extensions.crypto.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AesEncryptor {
    private static EncryptionUtils encryptionUtil;

    @Autowired
    public AesEncryptor(@Qualifier("aes256Encryption") EncryptionUtils encryptionUtil) {
        AesEncryptor.encryptionUtil = encryptionUtil;
    }

    static String encrypt(String textToEncrypt) {
        return encryptionUtil.encrypt(textToEncrypt);
    }

    static String decrypt(String textToDecrypt) {
        return encryptionUtil.decrypt(textToDecrypt);
    }
}
