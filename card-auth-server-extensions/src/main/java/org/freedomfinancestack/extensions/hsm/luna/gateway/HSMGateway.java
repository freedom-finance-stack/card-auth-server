package org.freedomfinancestack.extensions.hsm.luna.gateway;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;

public class HSMGateway {

    private AbstractClientConnectionFactory tcpClientConnectionFactory;

    private TcpSendingMessageHandler tcpSendingMessageHandler;

    private TcpReceivingChannelAdapter tcpReceivingChannelAdapter;

    private boolean isRunning;

    private volatile boolean isConnected = false;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public boolean isConnected() {
        r.lock();
        try {
            return isConnected;
        } finally {
            r.unlock();
        }
    }

    public void setConnected(boolean isConnected) {
        w.lock();
        try {
            this.isConnected = isConnected;
        } finally {
            w.unlock();
        }
    }

    public HSMGateway(
            AbstractClientConnectionFactory clientConnectionFactory,
            TcpSendingMessageHandler tcpSendingMessageHandler,
            TcpReceivingChannelAdapter tcpReceivingChannelAdapter) {
        super();
        this.tcpClientConnectionFactory = clientConnectionFactory;
        this.tcpSendingMessageHandler = tcpSendingMessageHandler;
        this.tcpReceivingChannelAdapter = tcpReceivingChannelAdapter;
    }

    public HSMGateway() {
        isRunning = false;
        isConnected = false;
    }

    public AbstractClientConnectionFactory getClientConnectionFactory() {
        return tcpClientConnectionFactory;
    }

    public void setClientConnectionFactory(
            AbstractClientConnectionFactory clientConnectionFactory) {
        this.tcpClientConnectionFactory = clientConnectionFactory;
    }

    public AbstractClientConnectionFactory getTcpClientConnectionFactory() {
        return tcpClientConnectionFactory;
    }

    public TcpSendingMessageHandler getTcpSendingMessageHandler() {
        return tcpSendingMessageHandler;
    }

    public TcpReceivingChannelAdapter getTcpReceivingChannelAdapter() {
        return tcpReceivingChannelAdapter;
    }
}
