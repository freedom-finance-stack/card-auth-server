package org.freedomfinancestack.razorpay.cas.acs.hsm.luna.command;

import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.InternalHsmMsg;

public interface ILunaHsmCmdInstanceCreator {
    HsmCommand getInstance(InternalHsmMsg internalMsg);
}
