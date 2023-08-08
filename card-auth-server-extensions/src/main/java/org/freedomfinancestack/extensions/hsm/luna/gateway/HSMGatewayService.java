package org.freedomfinancestack.extensions.hsm.luna.gateway;

public interface HSMGatewayService<H, M> {

    public void sendRequest(H gatewayHandler, M message) throws Exception;

    public byte[] fetchResponse(Object correlationKey) throws Exception;
}
