package com.razorpay.ffs.cas.acs.hsm.luna.command;

import com.razorpay.ffs.cas.acs.hsm.luna.domain.InternalHsmMsg;

public class LunaHsmDiagnosticCmd implements ILunaHsmCmdInstanceCreator {

    @Override
    public HsmCommand getInstance(InternalHsmMsg internalMsg) {
        return new Diagnostic01Command();
    }
}
