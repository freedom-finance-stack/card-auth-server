package org.freedomfinancestack.razorpay.cas.acs.hsm.luna.command;

import org.freedomfinancestack.razorpay.cas.acs.constant.LunaHSMConstants;
import org.freedomfinancestack.razorpay.cas.acs.hsm.luna.domain.InternalHsmMsg;
import org.freedomfinancestack.razorpay.cas.acs.utils.HexUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Diagnostic01Command extends HsmCommand {

    public Diagnostic01Command() {
        this.cmdCode = LunaHSMConstants.DIAGNOSTIC_CMD_01;
    }

    @Override
    public void initialize(InternalHsmMsg internalMsg) {}

    @Override
    public byte[] encode() {
        return null;
    }

    @Override
    public byte[] decode() {
        return null;
    }

    @Override
    public int processResponse(byte[] respMsg) {
        String outputData = HexUtil.hexValue(respMsg, 0, respMsg.length);
        String respCode = outputData.substring(2, 4);
        if (respCode.equals(LunaHSMConstants.HSM_SUCCESSFUL_RESPONSE)) {
            log.debug("processResponse() Received successful diagnostic command");
        } else {
            log.error(
                    "processResponse() Received failed diagnostic command with response code: {}",
                    respCode);
            return -1;
        }
        return 0;
    }

    @Override
    public byte[] serialize() {
        cmdLength = 1;
        return HexUtil.hexStringToByteArray(cmdCode);
    }

    @Override
    public int getCmdLength() {
        return cmdLength;
    }
}
