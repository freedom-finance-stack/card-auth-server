package org.freedomfinancestack.extensions.hsm.message;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.NonNull;

@Data
@Service("hSMMessageValidator")
public class HSMMessageValidator {
    public void validateHSMMessage(@NonNull final HSMMessage hsmMessage) throws Exception {
        if (StringUtils.isBlank(hsmMessage.getKcv())) {
            throw new Exception("keyCheckValue cannot be null or empty in HSM Message");
        }
        if (StringUtils.isBlank(hsmMessage.getData())) {
            throw new Exception("data cannot be null or empty in HSM Message");
        }
    }
}
