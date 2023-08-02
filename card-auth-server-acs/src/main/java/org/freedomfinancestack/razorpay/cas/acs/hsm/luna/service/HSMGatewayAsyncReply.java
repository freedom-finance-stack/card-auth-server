package org.freedomfinancestack.razorpay.cas.acs.hsm.luna.service;

public interface HSMGatewayAsyncReply<K, V> {

    V get(K k);

    void put(V v);
}
