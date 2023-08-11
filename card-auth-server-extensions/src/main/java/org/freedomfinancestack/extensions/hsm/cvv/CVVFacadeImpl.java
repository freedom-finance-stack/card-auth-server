package org.freedomfinancestack.extensions.hsm.cvv;

import org.apache.commons.lang3.StringUtils;
import org.freedomfinancestack.extensions.hsm.command.HSMCommand;
import org.freedomfinancestack.extensions.hsm.command.enums.HSMCommandType;
import org.freedomfinancestack.extensions.hsm.command.factory.HSMCommandFactory;
import org.freedomfinancestack.extensions.hsm.message.HSMMessage;
import org.freedomfinancestack.extensions.hsm.message.HSMMessageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.NonNull;

@Service("cvvFacadeImpl")
public class CVVFacadeImpl implements CVVFacade {

    private final HSMMessageValidator hsmMessageValidator;

    private final HSMCommandFactory hsmCommandFactory;

    private final HSMCommandType hsmCommandTypeEnabled;

    @Autowired
    public CVVFacadeImpl(
            @NonNull final HSMMessageValidator hsmMessageValidator,
            @NonNull final @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                    HSMCommandFactory hsmCommandFactory,
            @NonNull final @Qualifier("hsmCommandTypeEnabled") HSMCommandType
                            hsmCommandTypeEnabled) {
        this.hsmMessageValidator = hsmMessageValidator;
        this.hsmCommandFactory = hsmCommandFactory;
        this.hsmCommandTypeEnabled = hsmCommandTypeEnabled;
    }

    @Override
    public String generateCVV(@NonNull final HSMMessage hsmMessage) throws Exception {
        hsmMessageValidator.validateHSMMessage(hsmMessage);

        HSMCommand hsmCommand = hsmCommandFactory.getHSMCommand(hsmCommandTypeEnabled);

        hsmCommand.processHSMMessage(hsmMessage);

        String hsmResponse = hsmMessage.getHsmResponse();

        if (StringUtils.isBlank(hsmResponse) || hsmResponse.length() < 4) {
            throw new Exception(
                    "HSM Response cannot be null or empty and hsmResponse cannot be less than 3"
                            + " digits");
        }
        // returning first 3 digits which as CVV
        return hsmResponse.substring(0, 3);
    }
}
