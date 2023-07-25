package com.razorpay.ffs.cas.acs.hsm.luna.command;

import com.razorpay.ffs.cas.acs.hsm.luna.domain.InternalHsmMsg;

public interface ILunaHsmCmdInstanceCreator {
    HsmCommand getInstance(InternalHsmMsg internalMsg);
}
