package com.razorpay.threeds.hsm.luna.command;

import java.util.HashMap;

import com.razorpay.threeds.hsm.luna.domain.InternalHsmMsg;

import static com.razorpay.threeds.constant.LunaHSMConstants.INTERNAL_CVV_GENERATE_CMD;
import static com.razorpay.threeds.constant.LunaHSMConstants.INTERNAL_DIAGNOSTIC_CMD;

public class LunaHsmCmdFactory {
  private static final HashMap<String, ILunaHsmCmdInstanceCreator> registeredCmdCreators =
      new HashMap<>();

  static {
    registeredCmdCreators.put(INTERNAL_DIAGNOSTIC_CMD, new LunaHsmDiagnosticCmd());
    registeredCmdCreators.put(INTERNAL_CVV_GENERATE_CMD, new LunaHsmCvvGenerateCmd());
  }

  public HsmCommand getHsmCommand(InternalHsmMsg internalMsg) {
    String cmd = internalMsg.getCmdCode();
    HsmCommand hsmCommand = null;

    ILunaHsmCmdInstanceCreator cmdCreator = registeredCmdCreators.get(cmd);
    if (cmdCreator != null) {
      hsmCommand = cmdCreator.getInstance(internalMsg);
    }
    return hsmCommand;
  }
}
