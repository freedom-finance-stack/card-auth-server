package com.razorpay.ffs.cas.acs.hsm.luna.command;

import com.razorpay.ffs.cas.acs.hsm.luna.domain.InternalHsmMsg;
import com.razorpay.ffs.cas.acs.utils.HexUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.razorpay.ffs.cas.acs.constant.LunaHSMConstants.EE0802_CMD;
import static com.razorpay.ffs.cas.acs.constant.LunaHSMConstants.FUNC_MODIFIER_00;
import static com.razorpay.ffs.cas.acs.constant.LunaHSMConstants.HSM_SUCCESSFUL_RESPONSE;

@Slf4j
@Getter
@Setter
public class EE0802Command extends HsmCommand {

    private String cvvKey;
    private String cvvData;

    public EE0802Command() {
        this.cmdCode = EE0802_CMD;
        this.functionModifier = FUNC_MODIFIER_00;
    }

    @Override
    public void initialize(InternalHsmMsg internalMsg) {
        this.internalMsg = internalMsg;

        String cvvIndex = internalMsg.getCvvKey();
        int index = Integer.parseInt(cvvIndex);
        if (index < 99) {
            if (index > 9) {
                setCvvKey("0200" + index);
            } else {
                setCvvKey("02000" + index);
            }
        }
        int len = internalMsg.getCvvData().length();
        setCvvData(internalMsg.getCvvData() + "0".repeat(Math.max(0, 32 - len)));
    }

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
        String cvv = null;
        try {
            String respCode = outputData.substring(6, 8);
            if (respCode.equals(HSM_SUCCESSFUL_RESPONSE)) {
                cvv =
                        outputData.substring(
                                8, 11); // HexUtility.hexToAscii(outputData.substring(26));
                internalMsg.setCvv(cvv);
                log.debug("processResponse() Cvv generation is successful");
            } else {
                internalMsg.setCvv(cvv);
                log.error(
                        "processResponse() Cvv generation is failed with response code: {} ",
                        respCode);
                return -1;
            }
        } catch (Exception exp) {
            internalMsg.setCvv(cvv);
            log.error(
                    "processResponse() Cvv generation is failed with exception "
                            + HexUtil.getStackTrace(exp));
            return -1;
        }
        return 0;
    }

    @Override
    public byte[] serialize() {

        String cmdBuffer;
        cmdBuffer = cmdCode + functionModifier;
        cmdBuffer = cmdBuffer + getCvvKey() + getCvvData();

        byte[] cmdByte = HexUtil.hexStringToByteArray(cmdBuffer);

        cmdLength = cmdByte.length;

        return cmdByte;
    }

    @Override
    public int getCmdLength() {
        return cmdLength;
    }
}
