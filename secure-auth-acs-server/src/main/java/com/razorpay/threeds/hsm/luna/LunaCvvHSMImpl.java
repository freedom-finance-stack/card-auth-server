package com.razorpay.threeds.hsm.luna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.model.HSMConfigPK;
import com.razorpay.acs.dao.model.HsmConfig;
import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.acs.dao.repository.HSMConfigRepository;
import com.razorpay.threeds.exception.HSMConnectionException;
import com.razorpay.threeds.hsm.CvvHSM;
import com.razorpay.threeds.hsm.luna.command.HsmCommand;
import com.razorpay.threeds.hsm.luna.command.LunaHsmUtility;
import com.razorpay.threeds.hsm.luna.domain.InternalHsmMsg;
import com.razorpay.threeds.hsm.luna.service.impl.HSMGatewayFacade;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.razorpay.threeds.constant.LunaHSMConstants.INTERNAL_CVV_GENERATE_CMD;

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