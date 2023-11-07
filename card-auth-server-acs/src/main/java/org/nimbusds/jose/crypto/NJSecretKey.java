package org.nimbusds.jose.crypto;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.impl.ConcatKDF;
import com.nimbusds.jose.crypto.impl.ECDH;

public class NJSecretKey {

    public NJSecretKey() {
        // TODO Auto-generated constructor stub
    }

    public static SecretKey generateKDF(SecretKey Z, String sdkReferenceNumber)
            throws JOSEException {

        String algIdString = ""; // A128GCM, A128CBC-HS256

        byte[] algoId =
                CustomConcatKDF.encodeDataWithLength(algIdString.getBytes(StandardCharsets.UTF_8));
        byte[] sdkRefNo =
                CustomConcatKDF.encodeDataWithLength(
                        sdkReferenceNumber.getBytes(StandardCharsets.UTF_8));
        int keylength = 128;

        ConcatKDF concatKDF = new ConcatKDF("SHA-256");

        // SecretKey Z = ECDH.deriveSharedSecret(pub, priv, null);

        SecretKey derivedKey =
                concatKDF.deriveKey(
                        Z,
                        keylength,
                        algoId,
                        CustomConcatKDF.encodeDataWithLength(new byte[0]),
                        sdkRefNo,
                        CustomConcatKDF.encodeIntData(keylength),
                        CustomConcatKDF.encodeNoData());

        /*
         * SecretKey derivedKey = concatKDF.deriveKey( Z, 256,
         * ConcatKDF.encodeStringData(null),
         * ConcatKDF.encodeDataWithLength((Base64URL) null),
         * ConcatKDF.encodeDataWithLength(Base66URL.encode(acsId)),
         * ConcatKDF.encodeIntData(256), ConcatKDF.encodeNoData());
         */

        return derivedKey;
    }

    public static SecretKey deriveSharedKey(final JWEHeader header, final SecretKey Z)
            throws JOSEException {

        ConcatKDF concatKDF = new ConcatKDF("SHA-256");

        return ECDH.deriveSharedKey(header, Z, concatKDF);
    }
}
