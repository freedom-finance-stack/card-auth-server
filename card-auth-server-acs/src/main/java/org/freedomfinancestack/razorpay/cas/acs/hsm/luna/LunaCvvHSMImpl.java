package org.freedomfinancestack.razorpay.cas.acs.hsm.luna;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.HSMConnectionException;
import org.freedomfinancestack.razorpay.cas.acs.hsm.CvvHSM;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.command.HsmCommand;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.command.LunaHsmUtility;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.InternalHsmMsg;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.service.impl.HSMGatewayFacade;
import org.freedomfinancestack.razorpay.cas.dao.model.HSMConfigPK;
import org.freedomfinancestack.razorpay.cas.dao.model.HsmConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.HSMConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static org.freedomfinancestack.razorpay.cas.acs.constant.LunaHSMConstants.INTERNAL_CVV_GENERATE_CMD;

@Service("lunaCvvHSMImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LunaCvvHSMImpl implements CvvHSM {

    private final LunaHsmUtility lunaHsmUtility;

    private final HSMConfigRepository hsmConfigRepository;

    private final HSMGatewayFacade hsmGatewayFacade;

    @Override
    public String generateCVV(@NonNull final Transaction transaction, @NonNull final String data)
            throws HSMConnectionException {
        String strCVVIndex = getHSMConfig(transaction).getHsmDebitCvvCvcKey();
        if (strCVVIndex.length() == 1) {
            strCVVIndex = String.format("%02d", strCVVIndex);
        }

        InternalHsmMsg internalHsmMsg =
                lunaHsmUtility.getInternalHsmMsg(INTERNAL_CVV_GENERATE_CMD, strCVVIndex, data);
        HsmCommand hsmCommand = lunaHsmUtility.getHsmCommand(internalHsmMsg);
        hsmGatewayFacade.sendMessage(hsmCommand);
        return internalHsmMsg.getCvv();
    }

    private HsmConfig getHSMConfig(@NonNull final Transaction transaction) {
        HSMConfigPK hsmConfigPK = new HSMConfigPK();
        hsmConfigPK.setInstitutionId(transaction.getInstitutionId());
        hsmConfigPK.setNetworkId(transaction.getTransactionCardDetail().getNetworkCode());

        return hsmConfigRepository.findById(hsmConfigPK).orElse(null);
    }
}
