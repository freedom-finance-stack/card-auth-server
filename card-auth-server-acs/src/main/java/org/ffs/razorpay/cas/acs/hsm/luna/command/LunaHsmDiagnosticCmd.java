package org.ffs.razorpay.cas.acs.hsm.luna.command;

import org.ffs.razorpay.cas.acs.hsm.luna.domain.InternalHsmMsg;

public class LunaHsmDiagnosticCmd implements ILunaHsmCmdInstanceCreator {

    @Override
    public HsmCommand getInstance(InternalHsmMsg internalMsg) {
        return new Diagnostic01Command();
    }
}
