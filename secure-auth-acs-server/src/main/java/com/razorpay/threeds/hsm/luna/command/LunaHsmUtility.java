package com.razorpay.threeds.hsm.luna.command;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.razorpay.threeds.hsm.luna.domain.InternalHsmMsg;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service("lunaHsmUtility")
public class LunaHsmUtility {
    private final LunaHsmCmdFactory lunaHsmCmdFactory = new LunaHsmCmdFactory();

    public InternalHsmMsg getInternalHsmMsg(String cmd, String cvkIndex, String cvvData) {
        InternalHsmMsg internalMsg = new InternalHsmMsg();

        internalMsg.setCmdCode(cmd);
        if (StringUtils.isNotBlank(cvkIndex)) {
            internalMsg.setCvvKey(cvkIndex);
        }
        if (StringUtils.isNotBlank(cvvData)) {
            internalMsg.setCvvData(cvvData);
        }
        return internalMsg;
    }

    public HsmCommand getHsmCommand(InternalHsmMsg internalMsg) {

        return lunaHsmCmdFactory.getHsmCommand(internalMsg);
    }
}
