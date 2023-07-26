package org.ffs.razorpay.cas.acs.hsm.luna.command;

import org.ffs.razorpay.cas.acs.hsm.luna.domain.InternalHsmMsg;

public interface ILunaHsmCmdInstanceCreator {
    HsmCommand getInstance(InternalHsmMsg internalMsg);
}
