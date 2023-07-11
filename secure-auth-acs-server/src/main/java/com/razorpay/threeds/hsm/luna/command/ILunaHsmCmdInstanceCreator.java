package com.razorpay.threeds.hsm.luna.command;

import com.razorpay.threeds.hsm.luna.domain.InternalHsmMsg;

public interface ILunaHsmCmdInstanceCreator {
  HsmCommand getInstance(InternalHsmMsg internalMsg);
}
