package com.razorpay.acs.monitoring;

import io.micrometer.core.instrument.MeterRegistry;

public interface IMicrometer {

    MeterRegistry providesMeterRegistry();
}
