package org.freedomfinancestack.razorpay.cas.acs.hsm.luna.command;

import java.util.HashMap;

import org.freedomfinancestack.razorpay.cas.acs.constant.LunaHSMConstants;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.InternalHsmMsg;

public class LunaHsmCmdFactory {
    private static final HashMap<String, ILunaHsmCmdInstanceCreator> registeredCmdCreators =
            new HashMap<>();

    static {
        registeredCmdCreators.put(
                LunaHSMConstants.INTERNAL_DIAGNOSTIC_CMD, new LunaHsmDiagnosticCmd());
        registeredCmdCreators.put(
                LunaHSMConstants.INTERNAL_CVV_GENERATE_CMD, new LunaHsmCvvGenerateCmd());
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
