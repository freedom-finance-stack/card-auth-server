package org.freedomfinancestack.extensions.hsm.cvv;

import org.freedomfinancestack.extensions.hsm.message.HSMMessage;

import lombok.NonNull;

public interface CVVFacade {

    String generateCVV(@NonNull final HSMMessage hsmMessage) throws Exception;
}
