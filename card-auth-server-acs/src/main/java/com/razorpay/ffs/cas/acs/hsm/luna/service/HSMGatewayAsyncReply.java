package com.razorpay.ffs.cas.acs.hsm.luna.service;

public interface HSMGatewayAsyncReply<K, V> {

    V get(K k);

    void put(V v);
}
