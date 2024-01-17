package org.freedomfinancestack.razorpay.cas.dao.encryption;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SecureWrapper {
    private String encryptedData;

    public String getEncrypted() {
        return encryptedData;
    }

    public String getDecrypted() {
        return AesEncryptor.decrypt(this.encryptedData);
    }

    public static SecureWrapper parseEncrypted(String data) {
        return new SecureWrapper(data);
    }

    public static SecureWrapper parseDecrypted(String data) {
        return new SecureWrapper(AesEncryptor.encrypt(data));
    }
}
