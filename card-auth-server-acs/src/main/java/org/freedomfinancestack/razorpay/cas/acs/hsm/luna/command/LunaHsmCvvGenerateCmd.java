package org.freedomfinancestack.razorpay.cas.acs.hsm.luna.command;

import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.InternalHsmMsg;

public class LunaHsmCvvGenerateCmd implements ILunaHsmCmdInstanceCreator {

    @Override
    public HsmCommand getInstance(InternalHsmMsg internalMsg) {
        HsmCommand hsmCommand = null;

        hsmCommand = new EE0802Command();

        hsmCommand.initialize(internalMsg);
        return hsmCommand;
    }
}
