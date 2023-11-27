package org.nimbusds.jose.crypto;

import com.nimbusds.jose.crypto.impl.ConcatKDF;

public class CustomConcatKDF extends ConcatKDF {

    public CustomConcatKDF(String alg) {
        super(alg);
    }
}
