package org.freedomfinancestack.extensions.hsm.message;

import lombok.Data;
import lombok.NonNull;

@Data
public class HSMMessage {

    private String kcv;

    private String data;

    private String hsmResponse;

    public HSMMessage(@NonNull final String kcv, @NonNull final String data) {
        this.kcv = kcv;
        this.data = data;
    }
}
