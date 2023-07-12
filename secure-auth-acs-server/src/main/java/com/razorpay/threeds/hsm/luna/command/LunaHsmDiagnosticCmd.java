package com.razorpay.threeds.hsm.luna.command;

import com.razorpay.threeds.hsm.luna.domain.InternalHsmMsg;

public class LunaHsmDiagnosticCmd implements ILunaHsmCmdInstanceCreator {

    @Override
    public HsmCommand getInstance(InternalHsmMsg internalMsg) {
        return new Diagnostic01Command();
    }
}
