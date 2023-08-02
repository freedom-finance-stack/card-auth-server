package org.freedomfinancestack.extensions.hsm.command;

import org.freedomfinancestack.extensions.hsm.message.HSMMessage;

import lombok.NonNull;

public abstract class HSMCommand {
    public HSMMessage hsmMessage;

    public abstract void initialize();

    public abstract byte[] serialize();

    public abstract byte[] sendRequest(byte[] requestMessage) throws Exception;

    public abstract void processResponse(byte[] responseMessage);

    public abstract byte[] encode();

    public abstract byte[] decode();

    public void processHSMMessage(@NonNull final HSMMessage hsmMessage) throws Exception {
        this.hsmMessage = hsmMessage;

        initialize();

        byte[] hsmRequest = serialize();

        byte[] hsmResponse = sendRequest(hsmRequest);

        processResponse(hsmResponse);
    }
}
