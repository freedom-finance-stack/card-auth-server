package org.freedomfinancestack.extensions.hsm.luna.message;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.extensions.hsm.luna.utils.HexDump;
import org.freedomfinancestack.extensions.hsm.luna.utils.HexUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.hsm.luna.utils.LunaConstants.HSM_VERSION_HEADER;

@Slf4j
@Getter
@Setter
public class HSMTransactionMessage implements Message<byte[]>, Serializable {
    private static final long serialVersionUID = 1L;

    private String HSMMessage;
    private byte[] cmdMsg;
    private String msgSerialNo;
    private int cmdLength;
    private String hsmName;
    private int headerLength;

    public HSMTransactionMessage() {

        this.HSMMessage = "";

        this.msgSerialNo = "";
    }

    public HSMTransactionMessage(
            byte[] cmdMsg, int len, Integer hsmCounter, String hsmName, int headerLen) {
        super();

        // String hsmRequest = null;
        this.msgSerialNo = hsmCounter.toString();
        this.cmdMsg = cmdMsg;
        cmdLength = len;
        this.hsmName = hsmName;
        this.headerLength = headerLen;
        addHSMRequestHeader();
    }

    public void addHSMRequestHeader() {
        byte[] hsmRequest = null;
        Integer nSerialNo;
        String strHexSerialNo;

        try {
            byte[] byteVer = HexUtil.hexStringToByteArray(HSM_VERSION_HEADER);
            nSerialNo = Integer.parseInt(msgSerialNo);
            strHexSerialNo = String.format("%02X", nSerialNo);
            byte[] seq = strHexSerialNo.getBytes();
            byte[] bLen = new byte[2]; // HexUtility.intTobyteArray(cmdLength);
            bLen[0] = 0;
            String hexString = Integer.toHexString(cmdLength);
            bLen[1] = new BigInteger(hexString, 16).byteValue();

            hsmRequest = new byte[byteVer.length + seq.length + bLen.length + cmdMsg.length];
            int offset = 0;

            System.arraycopy(byteVer, 0, hsmRequest, offset, byteVer.length);
            offset = offset + byteVer.length;
            System.arraycopy(seq, 0, hsmRequest, offset, seq.length);
            offset = offset + seq.length;
            System.arraycopy(bLen, 0, hsmRequest, offset, bLen.length);
            offset = offset + bLen.length;
            System.arraycopy(cmdMsg, 0, hsmRequest, offset, cmdMsg.length);

            cmdMsg = new byte[hsmRequest.length];
            System.arraycopy(hsmRequest, 0, cmdMsg, 0, hsmRequest.length);

            this.HSMMessage = new String(hsmRequest, 0, hsmRequest.length);

            log.debug(HexDump.getHexDump(hsmRequest));

        } catch (Exception exp) {
            this.HSMMessage = null;
            log.error("addHSMRequestHeader() Error Occurred: ", exp);
        }
    }

    public void setHSMResponse(byte[] response) {

        String hsmReponse;
        hsmReponse = HexUtil.hexValue(response, 0, response.length);
        String seqHexVal = hsmReponse.substring(4, 8);
        String strSeqValue = HexUtil.hexToAscii(seqHexVal);
        int intSeqValue = HexUtil.hex2Decimal(strSeqValue);
        strSeqValue = Integer.toString(intSeqValue);

        this.HSMMessage = hsmReponse;
        this.msgSerialNo = strSeqValue;

        this.cmdMsg = new byte[response.length];
        System.arraycopy(response, 0, this.cmdMsg, 0, response.length);
    }

    @Override
    public MessageHeaders getHeaders() {
        Map<String, Object> map = new HashMap<>();
        map.put("SerialNo", this.msgSerialNo);
        return new MessageHeaders(map);
    }

    @Override
    public byte[] getPayload() {
        return this.cmdMsg;
    }

    @Override
    public String toString() {
        return this.HSMMessage;
    }
}
