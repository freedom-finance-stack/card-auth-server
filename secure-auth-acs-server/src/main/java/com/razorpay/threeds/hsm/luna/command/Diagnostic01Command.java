package com.razorpay.threeds.hsm.luna.command;

import com.razorpay.threeds.hsm.luna.domain.InternalHsmMsg;
import com.razorpay.threeds.utils.HexUtil;

import lombok.extern.slf4j.Slf4j;

import static com.razorpay.threeds.constant.LunaHSMConstants.DIAGNOSTIC_CMD_01;
import static com.razorpay.threeds.constant.LunaHSMConstants.HSM_SUCCESSFUL_RESPONSE;

@Slf4j
public class Diagnostic01Command extends HsmCommand {

    public Diagnostic01Command() {
        this.cmdCode = DIAGNOSTIC_CMD_01;
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
        if (respCode.equals(HSM_SUCCESSFUL_RESPONSE)) {
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
