package org.freedomfinancestack.razorpay.cas.dao.encryption;

import org.freedomfinancestack.extensions.crypto.IEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AesEncryptor {
    private static IEncryption encryptionService;

    @Autowired
    public AesEncryptor(@Qualifier("aes256Encryption") IEncryption encryptionUtil) {
        AesEncryptor.encryptionService = encryptionUtil;
    }

    static String encrypt(String textToEncrypt) {
        return encryptionService.encrypt(textToEncrypt);
    }

    static String decrypt(String textToDecrypt) {
        return encryptionService.decrypt(textToDecrypt);
    }
}
