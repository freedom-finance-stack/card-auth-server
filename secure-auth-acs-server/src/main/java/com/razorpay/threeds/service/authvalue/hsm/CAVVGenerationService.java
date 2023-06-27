package com.razorpay.threeds.service.authvalue.hsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.hsm.CavvHSM;
import com.razorpay.threeds.hsm.luna.LunaCavvHSMImpl;
import com.razorpay.threeds.hsm.noop.NoOpCavvHSMImpl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.razorpay.threeds.constant.InternalConstants.NO_OP_HSM;
import static com.razorpay.threeds.constant.LunaHSMConstants.LUNA_HSM;

@Slf4j
@Service(value = "cAVVGenerationService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CAVVGenerationService {

  private final ApplicationContext applicationContext;

  @Value("${hsm.enabled_gateway}")
  private String enabledHSMGateway;

  public String generateCavv(@NonNull final Transaction transaction, @NonNull final String data)
      throws ACSException {
    CavvHSM cavvHSM = getCAVVHSMImpl(enabledHSMGateway);
    return cavvHSM.generateCavv(transaction, data);
  }

  private CavvHSM getCAVVHSMImpl(String hsmConfigured) {
    if (NO_OP_HSM.equals(hsmConfigured)) {
      return applicationContext.getBean(NoOpCavvHSMImpl.class);
    } else if (LUNA_HSM.equals(hsmConfigured)) {
      return applicationContext.getBean(LunaCavvHSMImpl.class);
    }

    throw new IllegalArgumentException("Invalid hsm configuration for hsm: {}" + hsmConfigured);
  }
}
