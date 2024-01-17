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

    //    public void setEncryptedData(String data) {
    //        this.encryptedData = AesEncryptor.encrypt(data);
    //    }

    public static SecureWrapper parse(String data) {
        return new SecureWrapper(data);
    }
}
