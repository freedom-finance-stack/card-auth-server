package org.freedomfinancestack.extensions.hsm.luna.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.freedomfinancestack.extensions.hsm.luna.utils.HexDump;
import org.freedomfinancestack.extensions.hsm.luna.utils.HexUtil;
import org.springframework.integration.ip.tcp.serializer.AbstractPooledBufferByteArraySerializer;
import org.springframework.integration.ip.tcp.serializer.SoftEndOfStreamException;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class HSMByteArraySerializer extends AbstractPooledBufferByteArraySerializer {

    @Override
    public void serialize(byte[] object, OutputStream outputStream) throws IOException {
        outputStream.write(object);
    }

    @Override
    protected byte[] doDeserialize(InputStream inputStream, byte[] buffer) throws IOException {

        byte[] hsmResponse = null;

        byte[] byteHeader = new byte[6];
        byte[] byteResponse = null;
        int msgLength = 0;
        int len = inputStream.read(byteHeader, 0, 6);
        if (len < 0) {
            throw new SoftEndOfStreamException("Stream closed between payloads");
        }

        byte[] respLen = new byte[2];
        respLen[0] = byteHeader[4];
        respLen[1] = byteHeader[5];
        log.debug("Received Header is " + HexDump.getHexDump(byteHeader));
        msgLength = HexUtil.byteToInt(respLen);
        byteResponse = new byte[msgLength];
        len = inputStream.read(byteResponse, 0, msgLength);
        hsmResponse = new byte[msgLength + 6];
        System.arraycopy(byteHeader, 0, hsmResponse, 0, byteHeader.length);
        System.arraycopy(byteResponse, 0, hsmResponse, byteHeader.length, byteResponse.length);
        log.debug("Received response is " + HexDump.getHexDump(hsmResponse));

        return hsmResponse;
    }

    protected void writeHeader(OutputStream outputStream, int length) throws IOException {
        ByteBuffer lengthPart = ByteBuffer.allocate(2);
        lengthPart.putShort((short) length);
        outputStream.write(lengthPart.array());
    }
}
