package org.freedomfinancestack.extensions.hsm.luna.gateway;

public interface HSMGatewayAsyncReply<K, V> {

    V get(K k);

    void put(V v);
}
